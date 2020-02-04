package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName:ValidateCodeController
 * @Author：Mr.lee
 * @DATE：2020/02/03
 * @TIME： 15:27
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录校验的接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result check(@RequestBody Map<String, Object> map, HttpServletResponse response) {
        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            //获取redis中的验证码先进行验证码比较
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //验证码比较，如果验证不正确
            if (code == null || !code.equals(validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }else {
                //验证码正确，判断手机号码是否会员，不是的情况自动注册会员
                Member member = memberService.findByTelephone(telephone);
                //不是会员的情况
                if(member==null){
                    member = new Member();
                    member.setPhoneNumber(telephone);
                    member.setRegTime(new Date());
                    //注册新会员
                    memberService.add(member);
                }
            }
            //将登陆的手机号码信息存在cookie中，用于下次可以可用，记住账户信息
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            //设置路径
            cookie.setPath("/");
            //设置cookie存活周期
            cookie.setMaxAge(60*60*24*30);
            //将cookie存发送
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_FAIL);
        }
    }
}