package com.yeqifu.sys.mapper;

import com.yeqifu.sys.domain.Complain;
import com.yeqifu.sys.domain.News;
import com.yeqifu.sys.vo.ComplainVo;

import java.util.List;

public interface ComplainMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Complain record);

    int insertSelective(Complain record);

    Complain selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Complain record);

    int updateByPrimaryKey(Complain record);

    List<Complain> queryAllComplains(Complain news);
}