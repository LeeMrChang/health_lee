package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName:CheckGroupController
 * @Author：Mr.lee
 * @DATE：2019/11/05
 * @TIME： 20:08
 * @Description: TODO
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 检查组分页查询
     */
    @RequestMapping(value = "/findPage",method =RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        //设置分页的页码、每页显示条数、与查询条件

        PageResult pageResult = checkGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );

        return pageResult;
    }


    /**
     * 查询所有，在新增套餐作数据回显
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public Result findAll(){

        List<CheckGroup> groupList = checkGroupService.findAll();

        if(groupList!=null && groupList.size()>0){
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,groupList);
        }

        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 根据id查询
     */
    @RequestMapping(value = "/findById",method = RequestMethod.GET)
    public Result findById(Integer id){

            CheckGroup checkGroup = checkGroupService.findById(id);
            if(checkGroup!=null){
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
            }

            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);

    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){

        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**
     * 查询检查组所包含的所有检查项的id
     */
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId",method = RequestMethod.GET)
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){

       List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);

       return list;
    }


    /**
     * 檢查組添加
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){

        try {
            checkGroupService.insert(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete",method = RequestMethod.GET)
    public Result delete(Integer id){

        try {
            checkGroupService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
