package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName:CheckItemController
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 14:50
 * @Description: TODO
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //查询所有
    @RequestMapping("/findAll")

    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        if(checkItemList != null && checkItemList.size() > 0){
            Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);

            return result;
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }


    /**@PreAuthorize  添加SpringSecurity的权限校验的注解
     * 分页查询的操作
     */
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = checkItemService.queryPage(
                queryPageBean.getCurrentPage(),  //页码
                queryPageBean.getPageSize(),     //每页显示条数
                queryPageBean.getQueryString()   //分页查询的条件
        );

        return pageResult;
    }

    /**
     * 根据id查询
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){

        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }


    /**
     * 添加
     *   @PreAuthorize  权限校验的注解
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){

        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**
     * 修改
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result update(@RequestBody CheckItem checkItem){

        try {
            checkItemService.updateCheckItemBy(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){

        try {
            checkItemService.deleteCheckItemById(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
