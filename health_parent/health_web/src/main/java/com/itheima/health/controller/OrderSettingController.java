package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.CheckGroupService;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:CheckGroupController
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:08
 * @Description: TODO
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 根据传过来的年和月份查询预约表的
     * 可预约人数、已预约人数、当月日期
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.POST)
    public Result getOrderSettingByMonth(String date){

        try {
            List<Map> mapList = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * excel 文件上传
     * 并解析文件内容保存到数据库
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile) {

        try {
            //1.先根据poi工具类获取要上传文件的数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            //2.创建orderSetting集合对象接收多个数据
            List<OrderSetting> orderSettings = new ArrayList<>();

            if(list!=null && list.size()>0){
                //如果此文件有数据，遍历
                for (String[] strings : list) {
                    //创建orderSetting对象
                    OrderSetting orderSetting = new OrderSetting();
                    //设置预约日期
                    orderSetting.setOrderDate(new Date(strings[0]));
                    //设置可预约人数
                    orderSetting.setNumber(Integer.parseInt(strings[1]));
                    //添加到集合中
                    orderSettings.add(orderSetting);
                }
            }
            orderSettingService.insert(orderSettings);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }
}
