package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @ClassName:SetmealDao
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 16:02
 * @Description: TODO
 */
@Repository
public interface OrderSettingDao {

    /**
     * 导入添加数据成功
     * @param orderSetting
     */
    void insert(OrderSetting orderSetting);

    /**
     * 查询日期
     * @param orderDate
     * @return
     */
    long findOrderDateByNumber(Date orderDate);

    /**
     * 更新修改预约人数
     * @param orderSetting
     */
    void editByNumber(OrderSetting orderSetting);

    /**
     * 根据传过来的年和月份查询预约表的
     * 可预约人数、已预约人数、当月日期
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(@Param("beginDate") String beginDate,
                                              @Param("endDate") String endDate);
}
