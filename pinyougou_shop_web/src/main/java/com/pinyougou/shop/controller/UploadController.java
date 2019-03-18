package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * 文件上传 Controller
 *
 * @author Administrator
 */

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;//文件服务器地址

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        //获取扩展名
        String extName = filename.substring(filename.lastIndexOf(".") + 1);

        try {
            //创建一个 FastDFS 的客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = client.uploadFile(file.getBytes(), extName);
            String url = FILE_SERVER_URL + path;
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}
