package com.itheima.health.dao;

import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @ClassName:UserDao
 * @Author：Mr.lee
 * @DATE：2020/02/06
 * @TIME： 15:05
 * @Description: TODO
 */
@Repository
public interface RoleDao {

    Set<Role> findRolesByUserId(Integer userId);
}
