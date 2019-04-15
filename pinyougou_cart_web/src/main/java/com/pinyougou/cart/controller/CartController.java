package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout = 6000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //从cookie中提取购物车
        String cartList = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartList == null || cartList.equals("")) {
            cartList = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartList, Cart.class);
        if ("anonymousUser".equals(username)) {//未登录
            return cartList_cookie;
        } else {
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);//从redis 中提取
            if (cartList_cookie.size() > 0) {//如果本地存在购物车
                //合并购物车
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                //清除本地 cookie 的数据
                util.CookieUtil.deleteCookie(request, response, "cartList");
                //将合并后的数据存入 redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }
            return cartList_redis;
        }

    }

    @RequestMapping("/addGoodsToCartList")
    //            同意跨域请求的地址                  支持操作cookie
    @CrossOrigin(origins = "http://localhost:9105", allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            //从cookie中提取购物车
            List<Cart> cartList = findCartList();
            //调用服务方法操作购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if ("anonymousUser".equals(username)) { //如果是未登录，保存到 cookie
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                System.out.println("向 cookie 存入数据");
            } else {
                cartService.saveCartListToRedis(username, cartList);
            }
            return new Result(true, "存入购物车成功");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "存入购物车失败");
        }

    }

}
