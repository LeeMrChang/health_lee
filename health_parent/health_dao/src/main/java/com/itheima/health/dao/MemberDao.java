package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @ClassName:SetmealDao
 * @Author：Mr.lee
 * @DATE：2019/11/07
 * @TIME： 16:02
 * @Description: TODO
 */
@Repository
public interface MemberDao {

    Member findMemberByPhoneNumber(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberCountByDate(Date date);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
