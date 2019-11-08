package com.itheima.health.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:PageResult
 * @Author：Mr.lee
 * @DATE：2019/09/03
 * @TIME： 19:45
 * @Description: TODO
 */
public class PageResult implements Serializable {

    /**
     * 分页结果的封装对象
     */


    private Long total;//总记录数
    private List rows;//当前页结果

    public PageResult(Long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List getRows() {
        return rows;
    }
    public void setRows(List rows) {
        this.rows = rows;
    }



}
