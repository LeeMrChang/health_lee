package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:OrderDao
 * @Author：Mr.lee
 * @DATE：2020/02/03
 * @TIME： 21:25
 * @Description: TODO
 */
@Repository
public interface OrderDao {

    List<Order> findByOrderListByOrder(Order order);

    void add(Order order);

    Map findByOrderId(Integer id);

    List<Map<String, Object>> findSetmealCount();

    Integer findOrderCountByDate(Date today);

    Integer findVisitsCountByDate(Date today);

    Integer findOrderCountBetweenDate(Map<String, Object> weekMap);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();
}
