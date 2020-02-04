package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 1：使用预约日期查询“预约设置表”，判断当前预约日期是否存在
     *        如果不存在：返回错误提示信息”当前预约日期不存在“
     *        如果存在
     * 2：判断当前预约日期是否已经预约满
     *        如果已经预约满，此时不能再预约，返回错误信息“提示当前预约人数已满”
     * @param map
     * @return
     */
    @Override
    public Result submit(Map<String, Object> map) throws Exception {
        //1、获取预约日期
        String orderDateString = (String) map.get("orderDate");
        //字符串格式需要转换成日期格式,使用日期工具类
        Date orderDate = DateUtils.parseString2Date(orderDateString);
        //根据这个预约日期查询订单表，判断订单表中这个预约日期是否存在
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(orderDate);
        //如果当前预约日期不存在的情况下
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //获取最大可预约人数
        int reservations = orderSetting.getReservations();
        //获取已经预约人数
        int number = orderSetting.getNumber();
        if(reservations>=number){
            //判断当前预约日期是否已经预约满
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        /**
         * 3：使用手机号查询“会员表”，判断当前手机号是否是会员
         *        如果“是”，判断当前会员，当前时间，套餐id是否已经存在（防止重复预约）
         *           使用当前会员，当前时间，套餐id作为查询条件，查询订单表
         *            如果存在数据：表示”重复预约”
         *
         *        如果“不是”，先新增会员（会员id），
         *
         *        得到会员id，再向订单表中新增数据
         */
        String telephone = (String) map.get("telephone");
        //获取预约对应的套餐id
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
        //根据手机号码查询会员表对象
        Member member = memberDao.findMemberByPhoneNumber(telephone);
        if(member!=null){
            //为会员的情况,需要判断订单中的当前时间、套餐id，不能重复预约
            Order order = new Order(null,member.getId(),orderDate,null,null,setmealId);
            //根据order对象作为条件进行查询
            List<Order> orderList = orderDao.findByOrderListByOrder(order);
            if(orderList!=null && orderList.size()>0){
                //提示重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        //如果不是会员，做新增会员的添加
        if(member == null){
            //不是会员的情况，添加会员
            member = new Member();
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //如果是会员但是没有重复预约，则得到会员id向订单表中添加订单,
        //所以不管是不是会员都会向订单表添加一条订单的数据信息
        Order order = new Order(member.getId(),orderDate,(String) map.get("orderType"),Order.ORDERSTATUS_NO,setmealId);
        orderDao.add(order);
        //4：表示更新预约设置表，根据当前时间，更新reservations字段（已经预约的人数），值+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.update(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }
}
