package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.minio.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author atguigu-mqx
 */
@RestController
@RequestMapping("admin/product/")
public class FileUploadController {

    //  获取服务器相关的数据！
    //    minio:
    //    endpointUrl: http://192.168.6.100:9000
    //    accessKey: admin
    //    secreKey: admin123456
    //    bucketName: gmall
    @Value("${minio.endpointUrl}")
    private String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    //  http://localhost/admin/product/fileUpload
    //  springmvc 文件上传
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception{
        //  创建一个客户端
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpointUrl)
                        .credentials(accessKey, secreKey)
                        .build();

        // 判断存储桶是否存在！
        boolean found =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            // Make a new bucket called 'asiatrip'.
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } else {
            System.out.println("Bucket 'asiatrip' already exists.");
        }

        //  上传文件：
        //        minioClient.uploadObject(
        //                UploadObjectArgs.builder()
        //                        .bucket("asiatrip")
        //                        .object("asiaphotos-2015.zip")
        //                        .filename("/home/user/Photos/asiaphotos.zip")
        //                        .build());
        //  声明一个文件名称  cat.jpg
        String filename = System.currentTimeMillis()+ UUID.randomUUID().toString();
        //  设置文件的后缀名 截取：
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        System.out.println("ext:"+extension);
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(filename).stream(
                        file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        //  System.out.println("name:\t"+ file.getName());
        //  获取图片返回路径
        String url = endpointUrl+"/"+bucketName+"/"+filename;
        //  试着返回上传之后的url路径！
        return Result.ok(url);
    }
}
