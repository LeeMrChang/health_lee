package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * excel 文件导入
     *
     * @param orderSettingList
     */
    @Override
    public void insert(List<OrderSetting> orderSettingList) {
        /**
         * 做添加的操作，
         * 业务：查询预约日期是否相同。如果相同需要修改更新预约人数的数据
         */
        if(orderSettingList!=null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //查询日期是否重合
                long count = orderSettingDao.findOrderDateByNumber(orderSetting.getOrderDate());

                //如果重合
                if(count>0){
                    //做更新预约人数的操作
                    orderSettingDao.editByNumber(orderSetting);
                }

                orderSettingDao.insert(orderSetting);
            }
        }
    }

    /**
     * 根据传过来的年和月份查询预约表的
     * 可预约人数、已预约人数、当月日期
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {

        //1、设置月的头与尾，然后查询
        String beginDate = date+"-1";
        String endDate = date+"-31";

        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(beginDate,endDate);

        //2、现在获取到多个对象的预约数据，创建List<map>容器接收多个数据
        List<Map> maps = new ArrayList<>();

        //将获取到的预约对象遍历
        for (OrderSetting orderSetting : list) {

            //3、创建map容器接收每个对象中的 每月日期，可预约人数、已预约人数
            Map map = new HashMap();

            //日期
            map.put("date",orderSetting.getOrderDate().getDate());
            //可预约人数
            map.put("number",orderSetting.getNumber());
            //已预约人数
            map.put("reservations",orderSetting.getReservations());

            maps.add(map);
        }

        return maps;
    }
}
