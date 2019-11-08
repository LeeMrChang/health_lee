package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @ClassName:CheckItemService
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 14:51
 * @Description: TODO
 */
public interface CheckItemService {

    /**
     * 添加
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult queryPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 刪除
     * @param id
     */
    void deleteCheckItemById(Integer id);


    CheckItem findById(Integer id);

    void updateCheckItemBy(CheckItem checkItem);

    List<CheckItem> findAll();
}
