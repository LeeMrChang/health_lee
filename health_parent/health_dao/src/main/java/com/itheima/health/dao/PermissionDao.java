package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
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
public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
