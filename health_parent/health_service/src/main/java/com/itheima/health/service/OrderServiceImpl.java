package com.itheima.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName:CheckGroupServiceImpl
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:09
 * @Description: TODO
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 1：使用预约日期查询“预约设置表”，判断当前预约日期是否存在
     * 如果不存在：返回错误提示信息”当前预约日期不存在“
     * 如果存在
     * 2：判断当前预约日期是否已经预约满
     * 如果已经预约满，此时不能再预约，返回错误信息“提示当前预约人数已满”
     *
     * @param map
     * @return
     */
    @Override
    public Result submit(Map<String, Object> map) throws Exception {
        //1、获取预约日期
        String orderDateString = (String) map.get("orderDate");
        //字符串格式需要转换成日期格式,使用日期工具类
        Date orderDate = DateUtils.parseString2Date(orderDateString);
        //根据这个预约日期查询订单表，判断订单表中这个预约日期是否存在
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(orderDate);
        //如果当前预约日期不存在的情况下
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //获取最大可预约人数
        int reservations = orderSetting.getReservations();
        //获取已经预约人数
        int number = orderSetting.getNumber();
        if (reservations >= number) {
            //判断当前预约日期是否已经预约满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        /**
         * 3：使用手机号查询“会员表”，判断当前手机号是否是会员
         *        如果“是”，判断当前会员，当前时间，套餐id是否已经存在（防止重复预约）
         *           使用当前会员，当前时间，套餐id作为查询条件，查询订单表
         *            如果存在数据：表示”重复预约”
         *
         *        如果“不是”，先新增会员（会员id），
         *
         *        得到会员id，再向订单表中新增数据
         */
        String telephone = (String) map.get("telephone");
        //获取预约对应的套餐id
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
        //根据手机号码查询会员表对象
        Member member = memberDao.findMemberByPhoneNumber(telephone);
        if (member != null) {
            //为会员的情况,需要判断订单中的当前时间、套餐id，不能重复预约
            Order order = new Order(null, member.getId(), orderDate, null, null, setmealId);
            //根据order对象作为条件进行查询
            List<Order> orderList = orderDao.findByOrderListByOrder(order);
            if (orderList != null && orderList.size() > 0) {
                //提示重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //如果不是会员，做新增会员的添加
        if (member == null) {
            //不是会员的情况，添加会员
            member = new Member();
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //如果是会员但是没有重复预约，则得到会员id向订单表中添加订单,
        //所以不管是不是会员都会向订单表添加一条订单的数据信息
        Order order = new Order(member.getId(), orderDate, (String) map.get("orderType"), Order.ORDERSTATUS_NO, setmealId);
        orderDao.add(order);
        //4：表示更新预约设置表，根据当前时间，更新reservations字段（已经预约的人数），值+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.update(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    /**
     * 查询成功预约的体检订单展示信息
     *
     * @param id
     * @return
     */
    @Override
    public Result findById(Integer id) throws Exception {
        Map map = orderDao.findByOrderId(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            //日期转字符串做页面展示
            String date2String = DateUtils.parseDate2String(orderDate);
            map.put("orderDate", date2String);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        }
        return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
    }

    /**
     * 套餐统计
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return orderDao.findSetmealCount();
    }

    /**
     * 运营统计
     *
     * @return Map数据格式：
     * todayNewMember（今日新增会员数） -> number
     * totalMember（总会员数） -> number
     * thisWeekNewMember（本周新增会员数） -> number
     * thisMonthNewMember（本月新增会员数） -> number
     * todayOrderNumber（今日预约数） -> number
     * todayVisitsNumber（今日到诊数） -> number
     * thisWeekOrderNumber（本周预约数） -> number
     * thisWeekVisitsNumber（本周到诊数） -> number
     * thisMonthOrderNumber（本月预约数） -> number
     * thisMonthVisitsNumber（本月到诊数） -> number
     * hotSetmeal（热门套餐（取前4）） -> List<Setmeal>
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        try {
            //当前时间
            Date today = new Date();
            //本周的第一天
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //本周最后一天
            String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //本月的第一天
            String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //本月最后一天
            String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            //当前时间会员数
            Integer todayNewMember = memberDao.findMemberCountByDate(today);
            //总会员数
            Integer totalMember = memberDao.findMemberTotalCount();
            //本周新增会员数
            Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
            //本月新增会员数
            Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

            //今日预约数
            Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
            //今日到诊数
            Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

            //本周预约数 ,需要查询本周的时间段，星期一到星期日之间的预约数
            Map<String, Object> weekMap = new HashMap<String, Object>();
            weekMap.put("begin", thisWeekMonday);
            weekMap.put("end", thisWeekSunday);
            Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(weekMap);
            //本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

            //本月预约数，同样查时间段之间的数据统计
            Map<String, Object> monthMap = new HashMap<String, Object>();
            monthMap.put("begin", firstDay4ThisMonth);
            monthMap.put("end", lastDay4ThisMonth);
            Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);

            //本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

            // 热门套餐（取前4）
            List<Map> hotSetmeal = orderDao.findHotSetmeal();


            Map<String, Object> result = new HashMap<>();
            result.put("reportDate", today);
            result.put("todayNewMember", todayNewMember);
            result.put("totalMember", totalMember);
            result.put("thisWeekNewMember", thisWeekNewMember);
            result.put("thisMonthNewMember", thisMonthNewMember);
            result.put("todayOrderNumber", todayOrderNumber);
            result.put("thisWeekOrderNumber", thisWeekOrderNumber);
            result.put("thisMonthOrderNumber", thisMonthOrderNumber);
            result.put("todayVisitsNumber", todayVisitsNumber);
            result.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
            result.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
            result.put("hotSetmeal",hotSetmeal);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
