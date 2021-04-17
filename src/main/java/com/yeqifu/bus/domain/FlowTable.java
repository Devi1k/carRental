package com.yeqifu.bus.domain;

/**
 * 行为记录
 */
public class FlowTable {

    private Integer id;
    private Integer userId;
    private Integer carId;
    private Double sourceCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Double getSourceCount() {
        return sourceCount;
    }

    public void setSourceCount(Double sourceCount) {
        this.sourceCount = sourceCount;
    }
}