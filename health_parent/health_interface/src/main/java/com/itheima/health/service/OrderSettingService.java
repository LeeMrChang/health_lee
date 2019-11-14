package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:SetmealService
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:47
 * @Description: TODO
 */
public interface OrderSettingService {

    /**
     * excel 文件导入
     * @param orderSettingList
     */
    void insert(List<OrderSetting> orderSettingList);

    /**
     * 根据传过来的年和月份查询预约表的
     * 可预约人数、已预约人数、当月日期
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);
}
