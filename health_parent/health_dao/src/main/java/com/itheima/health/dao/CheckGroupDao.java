package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:CheckGroupDao
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:15
 * @Description: TODO
 */
@Repository
public interface CheckGroupDao {

    void insert(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(@Param(value = "checkGroupId") Integer checkGroupId,
                                   @Param(value = "checkitemId")Integer checkitemId);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    void delete(Integer id);

    Long findCheGroupById(Integer id);

    List<CheckGroup> findAll();

}
