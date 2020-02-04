package com.itheima.health.service;

import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:SetmealService
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:47
 * @Description: TODO
 */
public interface OrderService {


    Result submit(Map<String, Object> map) throws Exception;

    Result findById(Integer id) throws Exception;
}
