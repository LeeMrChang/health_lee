package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName:CheckItemServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 14:52
 * @Description: TODO
 */
@Transactional
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.insert(checkItem);
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

        /**
         * 设置查询条件
         */
        Page<CheckItem> page = checkItemDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 刪除
     *
     * @param id
     */
    @Override
    public void deleteCheckItemById(Integer id) {

        //TODO 需要判断检查项的id是否被检查组引用
        //查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被检查组引用，不能删除");
        }
        checkItemDao.deleteCheckItemById(id);
    }


    @Override
    public CheckItem findById(Integer checkItemId) {
        return checkItemDao.findCheckItemById(checkItemId);
    }

    /**
     * 修改
     * @param checkItem
     */
    @Override
    public void updateCheckItemBy(CheckItem checkItem) {
        checkItemDao.updateCheckItemBy(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}

