package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName:CheckGroupController
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:08
 * @Description: TODO
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 检查组分页查询
     */
    @RequestMapping(value = "/getUserName",method =RequestMethod.GET)
    public Result getUserName(){
        try {
            //要认清User引入的包
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return new Result(true,MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }
}
