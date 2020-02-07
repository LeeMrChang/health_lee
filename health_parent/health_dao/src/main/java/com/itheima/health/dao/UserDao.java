package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @ClassName:UserDao
 * @Author：Mr.lee
 * @DATE：2020/02/06
 * @TIME： 15:05
 * @Description: TODO
 */
@Repository
public interface UserDao {
    User findUserByUsername(String username);
}
