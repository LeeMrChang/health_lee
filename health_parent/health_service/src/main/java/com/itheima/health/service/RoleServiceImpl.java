package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

}
