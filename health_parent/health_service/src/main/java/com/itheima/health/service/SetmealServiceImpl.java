package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @ClassName:SetmealServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:48
 * @Description: TODO
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * redis
     */
    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加套餐
     *
     * @param setmeal
     */
    @Override
    public void insert(Setmeal setmeal,Integer[] checkGroupIds) {
        //添加新增套餐的操作
        setmealDao.insert(setmeal);
        //设置新增套餐id与检查组id的关联关系
        if(checkGroupIds!=null && checkGroupIds.length>0){
            for (Integer checkGroupId : checkGroupIds) {
                setmealDao.setSetmealAndCheckGroup(setmeal.getId(),checkGroupId);
            }
        }

        //将图片文件名称设置进redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES
                ,setmeal.getImg());
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
    public PageResult queryPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);

        Page<Setmeal> page = setmealDao.queryString(queryString);

        /**
         * 设置查询出来的总记录数与查询结果
         */
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根據id查詢套餐
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 查询新增套餐中所对应的检查组id
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改
     *
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {

        //修改之前还需清除两张表之间的联系
        setmealDao.deleteSetmealAndCheckGroup(setmeal.getId());


        setmealDao.edit(setmeal);

        if(checkGroupIds!=null && checkGroupIds.length>0){
            for (Integer checkGroupId : checkGroupIds) {
               setmealDao.setSetmealAndCheckGroup(setmeal.getId(),checkGroupId);
            }
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {

        //删除之前判断该表是否被引用
        Long count = setmealDao.findSetmealIdBy(id) ;

        if(count>0){
            throw new RuntimeException("该id被引用，无法删除");
        }

        setmealDao.delete(id);
    }

    /**
     * 查询套餐列表
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }
}
