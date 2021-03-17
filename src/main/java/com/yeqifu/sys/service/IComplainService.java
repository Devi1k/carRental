package com.yeqifu.sys.service;

import com.yeqifu.sys.domain.Complain;
import com.yeqifu.sys.domain.News;
import com.yeqifu.sys.utils.DataGridView;
import com.yeqifu.sys.vo.ComplainVo;
import com.yeqifu.sys.vo.NewsVo;

public interface IComplainService {

    public DataGridView queryAllComplains(ComplainVo complainVo);


    public void addComplains(ComplainVo complainVo);


    public void deleteComplains(Integer newsid);


    public void deleteBatchComplains(Integer[] ids);

    public void updateComplains(ComplainVo complainVo);

    Complain queryComplainsById(Integer id);
}
