package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @ClassName:CheckGroupService
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
public interface CheckGroupService {
    /**
     * 添加
     * @param checkGroup
     * @param checkItemIds
     */
    void insert(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 查询检查组所包含的所有检查项的id
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 编辑
     * @param checkGroup
     * @param checkitemIds
     */
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 删除
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询全部
     */
    List<CheckGroup>  findAll();
}
