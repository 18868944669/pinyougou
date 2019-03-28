package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {
    /**
     * 添加商品到购物车
     *
     * @param cartList  购物车列表
     * @param itemId SKU ID
     * @param num 数量
     * @return
     */

    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);
}
