package com.yeqifu.bus.mapper;

import com.yeqifu.bus.domain.Car;
import com.yeqifu.bus.domain.FlowTable;
import com.yeqifu.bus.vo.CarVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarMapper {

    //单独添加Mapper未知bug  整合到一个

    int insertFlow(FlowTable record);


    List<FlowTable> selecterFlow(FlowTable flowTable);


    int updateHobbyCount(FlowTable record);

    //单独添加未知bug  整合到一个
    int deleteByPrimaryKey(String carnumber);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(String carnumber);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    List<Car> queryAllCar(CarVo carVo);

    List<Car> batchSelect(@Param("ids") List<Integer> ids);

    List<Car> getType();
}