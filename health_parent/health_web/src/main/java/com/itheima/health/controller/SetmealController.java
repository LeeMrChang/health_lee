package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;


import java.util.List;
import java.util.UUID;

/**
 * @ClassName:SetmealController
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:47
 * @Description: TODO
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 分页查询
     */
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

       PageResult pageResult = setmealService.queryPage(
               queryPageBean.getCurrentPage(),
               queryPageBean.getPageSize(),
               queryPageBean.getQueryString()
       );

       return pageResult;
    }

    /**
     * 根据新增套餐id查询对应新增套餐的信息
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){

        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 查询所有新增套餐id所对应的检查组id
     */
    @RequestMapping(value = "/findCheckGroupIdsBySetmealId",method = RequestMethod.GET)
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id){

        //查询新增套餐中所对应的检查组id
        List<Integer> list = setmealService.findCheckGroupIdsBySetmealId(id);

        return list;
    }

    /**
     * 文件上传的接口
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            //使用七牛云工具类
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //图片上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result save(@RequestBody Setmeal setmeal,Integer[] checkGroupIds){

        try {
            setmealService.insert(setmeal,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }

        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 编辑修改
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkGroupIds){

        try {
            setmealService.edit(setmeal,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑失败");
        }

        return new Result(true,"编辑成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(Integer id){

        try {
            setmealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败！");
        }

        return new Result(true,"删除成功！");
    }
}
