package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @ClassName:OrderController
 * @Author：Mr.lee
 * @DATE：2020/02/03
 * @TIME： 21:28
 * @Description: TODO
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public Result submit(@RequestBody Map<String,Object> map){
        //在加入订单之前必须先判断验证码是否正确或者不为null
        //获取手机号码
        String telephone = (String) map.get("telephone");
        //获取验证码
        String validateCode = (String) map.get("validateCode");
        //获取redis中的验证码，与页面输入的验证码相对比
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //redis中的code不为null 或者 两个验证码不相同
        if(code==null || !code.equals(validateCode)){
            //就是预约不成功
            return  new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //然后使用手机预约
        map.put("orderType", Order.ORDERTYPE_WEIXIN);

//        Result result = s.submit(map);
//        if(result.isFlag()){
//
//        }
        return null;
    }
}
