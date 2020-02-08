package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:ReportController
 * @Author：Mr.lee
 * @DATE：2020/02/07
 * @TIME： 20:56
 * @Description: TODO
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private OrderService orderService;

    /**
     * 统计会员每月增长的报表
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getMemberReport(){
        try {
            Calendar calendar = Calendar.getInstance();
            //获得当前日期之前12个月的日期
            calendar.add(Calendar.MONTH,-12);

            List<String> list = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                //获取每个日期每个月逐渐过来的月  ；月加1
                calendar.add(Calendar.MONTH,1);
                //获取到12个月每个月的数据，需要转换成时间格式，年-月的时间格式
                //list.add(DateUtils.parseDate2String(calendar.getTime())); 使用时间工具类的方式
                list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }
            Map<String,Object> map = new HashMap<>();
            //封装过去一年12个月的数据时间格式
            map.put("months",list);

            //根据每个月的时间格式 2019-12  去查询每个月的会员新增统计
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐统计的饼状图统计
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){

        try {
            //查询套餐统计的数据
            List<Map<String,Object>> countMap = orderService.findSetmealCount();
            //封装套餐名称的容器
            List<String> list = new ArrayList<>();
            for (Map<String, Object> map : countMap) {
                //获取套餐名称
                String name = (String) map.get("name");
                list.add(name);
            }
            //添加套餐名称与套餐统计得到的count数据到map容器中
            Map<String,Object> map = new HashMap<>();
            map.put("setmealNames",list);
            map.put("setmealCount",countMap);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}
