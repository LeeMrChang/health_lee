package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.OrderService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:ReportController
 * @Author：Mr.lee
 * @DATE：2020/02/07
 * @TIME： 20:56
 * @Description: TODO
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private OrderService orderService;

    /**
     * 统计会员每月增长的报表
     */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {
        try {
            Calendar calendar = Calendar.getInstance();
            //获得当前日期之前12个月的日期
            calendar.add(Calendar.MONTH, -12);

            List<String> list = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                //获取每个日期每个月逐渐过来的月  月加1
                calendar.add(Calendar.MONTH, 1);
                //获取到12个月每个月的数据，需要转换成时间格式，年-月的时间格式
                //list.add(DateUtils.parseDate2String(calendar.getTime())); 使用时间工具类的方式
                list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }
            Map<String, Object> map = new HashMap<>();
            //封装过去一年12个月的数据时间格式
            map.put("months", list);

            //根据每个月的时间格式 2019-12  去查询每个月的会员新增统计
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐统计的饼状图统计
     */
    @RequestMapping(value = "/getSetmealReport", method = RequestMethod.GET)
    public Result getSetmealReport() {

        try {
            //查询套餐统计的数据
            List<Map<String, Object>> countMap = orderService.findSetmealCount();
            //封装套餐名称的容器
            List<String> list = new ArrayList<>();
            for (Map<String, Object> map : countMap) {
                //获取套餐名称
                String name = (String) map.get("name");
                list.add(name);
            }
            //添加套餐名称与套餐统计得到的count数据到map容器中
            Map<String, Object> map = new HashMap<>();
            map.put("setmealNames", list);
            map.put("setmealCount", countMap);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 运营统计的接口
     */
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData() {

        try {
            Map<String, Object> map = orderService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 使用poi做报表导出功能
     *
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try{
            //远程调用报表服务获取报表数据
            Map<String, Object> result = orderService.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获得Excel模板文件绝对路径
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            //日期
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            //新增会员数（本日）
            row.getCell(5).setCellValue(todayNewMember);
            //总会员数
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            //本周新增会员数
            row.getCell(5).setCellValue(thisWeekNewMember);
            //本月新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);

            row = sheet.getRow(7);
            //今日预约数
            row.getCell(5).setCellValue(todayOrderNumber);
            //今日到诊数
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            //本周预约数
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            //本周到诊数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            //本月预约数
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            //本月到诊数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            int rowNum = 12;
            for(Map map : hotSetmeal){
                //热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                //套餐名称
                row.getCell(4).setCellValue(name);
                //预约数量
                row.getCell(5).setCellValue(setmeal_count);
                //占比
                row.getCell(6).setCellValue(proportion.doubleValue());
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }
}
