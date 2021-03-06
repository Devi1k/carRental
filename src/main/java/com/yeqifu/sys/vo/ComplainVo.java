package com.yeqifu.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeqifu.sys.domain.Complain;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ComplainVo extends Complain {
    private Integer page;
    private Integer limit;

    /**
     * 将前台页面的时间转换到后端
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;


    /**
     * 接受多个id
     */
    private Integer [] ids;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }



    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }
}
