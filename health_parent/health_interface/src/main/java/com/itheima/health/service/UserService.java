package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.User;

import java.util.List;

/**
 * @ClassName:CheckGroupService
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
public interface UserService {

    User findUserByUsername(String username);
}
