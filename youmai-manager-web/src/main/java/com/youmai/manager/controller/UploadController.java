package com.youmai.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;


/**
 * @ClassName: UploadController
 * @Description: 文件上传
 * @Author: 泊松
 * @Date: 2018/9/16 20:37
 * @Version: 1.0
 */
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        //获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String etName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        try {
            //创建一个FastDfs的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), etName);
            //图片完整地址
            String url =FILE_SERVER_URL+ path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"上传失败");
        }
    }

}
