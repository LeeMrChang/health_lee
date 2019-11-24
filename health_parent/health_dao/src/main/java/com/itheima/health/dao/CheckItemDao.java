package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName:CheckItemDao
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 15:10
 * @Description: TODO
 */
@Repository
public interface CheckItemDao {
    /**
     * 添加
     * @param checkItem
     */
    void insert(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findPage(String queryString);

    /**
     * 刪除
     * @param id
     */
    void deleteCheckItemById(Integer id);

    /**
     * 查询检查项的id是否被引用
     * @param id
     * @return
     */
    long findCountByCheckItemId(Integer id);


    CheckItem findCheckItemById(Integer checkItemId);

    /**
     * 修改
     * @param checkItem
     */

    void updateCheckItemBy(CheckItem checkItem);

    List<CheckItem> findAll();

    /**
     * 多对多查询
     * @param id
     * @return
     */
    List<CheckItem> findCheckItemListById(Integer id);
}
