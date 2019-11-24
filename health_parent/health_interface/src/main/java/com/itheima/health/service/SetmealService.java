package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * @ClassName:SetmealService
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:47
 * @Description: TODO
 */
public interface SetmealService {
    /**
     * 添加套餐
     * @param setmeal
     */
    void insert(Setmeal setmeal,Integer[] checkGroupIds);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult queryPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根據id查詢套餐
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 查询新增套餐中所对应的检查组id
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 修改
     * @param setmeal
     * @param checkGroupIds
     */
    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    /**
     * 删除
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询套餐列表
     * @return
     */
    List<Setmeal> findAll();
}
