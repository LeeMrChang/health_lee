package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public void add(Member member) {
        //对会员密码进行md5加密
        if(member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findMemberByPhoneNumber(telephone);
    }

    /**
     *  根据日期时间格式  regTime 去查询每月新增会员的统计  （查询截止到月底）
     *   本来根据 2019-12 yue   但是12月有30天或者31天
     * @param list  封装12个月的时间格式
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> dateList = new ArrayList<>();

        for (String s : list) {
            //格式：2019-04-31,每个月的月底可能不同，有的月底是31天
            s = s + "-31";
            Integer count = memberDao.findMemberCountBeforeDate(s);
            //将每个月的会员统计返回
            dateList.add(count);
        }
        return dateList;
    }
}
