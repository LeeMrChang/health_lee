package com.itheima.health;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName:TestQiNiu
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 14:31
 * @Description: TODO
 */
public class TestQiNiu {

    /**
     * 文件名上传
     */
    @Test
    public void test01() {

        //构造一个带指定 Region 对象的配置类 指定华东地区
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "sQVG3LXtBneKM6DyZ91o0L3vQ3S6dSuYreldXlhy";
        String secretKey = "gLScPIDvjEuqW68JGP1adQV6QUWMlgHI8WuTch8t";
        String bucket = "lee64";

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "D:/qiniu/2m.jpg";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 刪除文件
     */
    @Test
    public void test03(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        String accessKey = "sQVG3LXtBneKM6DyZ91o0L3vQ3S6dSuYreldXlhy";
        String secretKey = "gLScPIDvjEuqW68JGP1adQV6QUWMlgHI8WuTch8t";
        String bucket = "lee64";
        String key = "Fowygs5P0J_mLgGxyUmnAFsuQFz8";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 字节流上传
     */
    @Test
    public void test02(){
//        //构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Zone.zone0());
//        //...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
//        //...生成上传凭证，然后准备上传
//        String accessKey = "sQVG3LXtBneKM6DyZ91o0L3vQ3S6dSuYreldXlhy";
//        String secretKey = "gLScPIDvjEuqW68JGP1adQV6QUWMlgHI8WuTch8t";
//        String bucket = "lee64";
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = null;
//        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
//            Auth auth = Auth.create(accessKey, secretKey);
//            String upToken = auth.uploadToken(bucket);
//            try {
//                Response response = uploadManager.put(byteInputStream,key,upToken,null, null);
//                //解析上传成功的结果
//                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
//            } catch (QiniuException ex) {
//                Response r = ex.response;
//                System.err.println(r.toString());
//                try {
//                    System.err.println(r.bodyString());
//                } catch (QiniuException ex2) {
//                    //ignore
//                }
//            }
//        } catch (UnsupportedEncodingException ex) {
//            //ignore
//        }
    }
}
