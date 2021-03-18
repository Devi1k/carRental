package com.yeqifu.sys.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yeqifu.sys.domain.Complain;
import com.yeqifu.sys.domain.News;
import com.yeqifu.sys.mapper.ComplainMapper;
import com.yeqifu.sys.service.IComplainService;
import com.yeqifu.sys.utils.DataGridView;
import com.yeqifu.sys.vo.ComplainVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ComplainServiceImpl implements IComplainService {
    @Autowired
    private ComplainMapper complainMapper;

    @Override
    public DataGridView queryAllComplains(ComplainVo complainVo) {
        System.out.println(complainVo.getPage());
        System.out.println(complainVo.getLimit());
        Page<Object> page = PageHelper.startPage(complainVo.getPage(),complainVo.getLimit());
        System.out.println(page);

        List<Complain> data = this.complainMapper.queryAllComplains(complainVo);
        System.out.println("service3");

        return new DataGridView(page.getTotal(),data);
    }

    @Override
    public void addComplains(ComplainVo complainVo) {
        this.complainMapper.insertSelective(complainVo);
    }

    @Override
    public void deleteComplains(Integer complainsid) {
        this.complainMapper.deleteByPrimaryKey(complainsid);
    }

    @Override
    public void deleteBatchComplains(Integer[] ids) {
        for(Integer id:ids){
            this.deleteComplains(id);
        }
    }

    @Override
    public void updateComplains(ComplainVo complainVo) {
        this.complainMapper.updateByPrimaryKeySelective(complainVo);
    }

    @Override
    public Complain queryComplainsById(Integer id) {
        return this.complainMapper.selectByPrimaryKey(id);
    }
}
