package com.felix.unbiz.json.rpc.service.bo;

import java.util.List;

/**
 * ClassName: QueryCriteria <br>
 * Function: 查询条件
 *
 * @author wangxujin
 */
public class QueryCriteria {

    /**
     * 名称
     */
    private List<String> nameList;

    private int pageNo;

    private int pageSize;

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
