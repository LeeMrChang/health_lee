package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * @ClassName:SetmealService
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 15:47
 * @Description: TODO
 */
public interface MemberService {


    void add(Member member);

    Member findByTelephone(String telephone);

    List<Integer> findMemberCountByMonth(List<String> list);
}
