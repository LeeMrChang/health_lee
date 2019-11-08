package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 做检查组的添加。需求。设置检查组与检查项的关联关系
     * @param checkGroup
     * @param checkItemIds
     */

    @Override
    public void insert(CheckGroup checkGroup, Integer[] checkItemIds) {

        //检查组添加操作
        checkGroupDao.insert(checkGroup);

        //设置检查组与检查项的关联关系
       setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);

    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);

        /**
         * 设置查询条件
         */
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 查询检查组所包含的所有检查项的id
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {

        //1.编辑之前先清空

        //根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {

        //TODO 查询并判断检查组id是否被引用
        Long number =  checkGroupDao.findCheGroupById(id);

        if(number>0){
            throw  new RuntimeException("This id is referenced !!!");
        }

        checkGroupDao.delete(id);
    }

    /**
     * 查询全部
     */
    @Override
    public List<CheckGroup> findAll() {
       return checkGroupDao.findAll();
    }


    //设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.setCheckGroupAndCheckItem(checkGroupId,checkitemId);
            }
        }
    }

}
