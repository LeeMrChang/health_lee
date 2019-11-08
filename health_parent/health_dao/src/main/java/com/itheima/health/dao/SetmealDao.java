package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName:SetmealDao
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 16:02
 * @Description: TODO
 */
@Repository
public interface SetmealDao {
    /**
     * 添加
     * @param setmeal
     */
    void insert(Setmeal setmeal);

    /**
     * 设置id关联
     * @param setmealId
     * @param checkGroupId
     */
    void setSetmealAndCheckGroup(@Param("setmealId") Integer setmealId,
                                 @Param("checkGroupId") Integer checkGroupId);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> queryString(String queryString);

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
     * 删除表与表之间的联系
     * @param id
     */
    void deleteSetmealAndCheckGroup(Integer id);

    /**
     * 重新编辑
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 查询该id是否被引用
     * @param id
     * @return
     */
    Long findSetmealIdBy(Integer id);

    /**
     * 删除
     * @param id
     */
    void delete(Integer id);

}
