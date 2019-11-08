package com.itheima.health.entity;

import java.io.Serializable;

/**
 * @ClassName:QueryPageBean
 * @Author：Mr.lee
 * @DATE：2019/09/03
 * @TIME： 19:46
 * @Description: TODO
 */
public class QueryPageBean implements Serializable {

    /**
     * 封装分页查询的条件
     */

    private Integer currentPage;//页码
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

}
