package com.yeqifu.sys.controller;


import com.yeqifu.sys.domain.Complain;
import com.yeqifu.sys.domain.User;
import com.yeqifu.sys.service.IComplainService;
import com.yeqifu.sys.utils.DataGridView;
import com.yeqifu.sys.utils.ResultObj;
import com.yeqifu.sys.utils.WebUtils;
import com.yeqifu.sys.vo.ComplainVo;
import com.yeqifu.sys.vo.NewsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("complain")
public class ComplainController {

    @Autowired
    private IComplainService complainService;

    @RequestMapping("loadAllComplains")
    public DataGridView loadAllComplains(ComplainVo complainVo){
        System.out.println("controller");
        return this.complainService.queryAllComplains(complainVo);
    }


    @RequestMapping("addComplains")
    public ResultObj addComplains(ComplainVo complainVo){
        try {
            complainVo.setCreatetime(new Date());
            User user = (User) WebUtils.getHttpSession().getAttribute("user");
            System.out.println(complainVo.getId()+complainVo.getTitle()+complainVo.getContent()+user);
            /*if(user != null) {
            	complainVo.setReplyer(user.getRealname());
            }*/
            this.complainService.addComplains(complainVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    @RequestMapping("deleteComplains")
    public ResultObj deleteComplain(ComplainVo complainVo){
        try {
            this.complainService.deleteComplains(complainVo.getId());
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    @RequestMapping("deleteBatchComplains")
    public ResultObj deleteBatchComplains(ComplainVo complainVo) {
        try {
            this.complainService.deleteBatchComplains(complainVo.getIds());
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    @RequestMapping("updateComplains")
    public ResultObj updateComplains(ComplainVo complainVo){
        try {
        	User user = (User) WebUtils.getHttpSession().getAttribute("user");
        	if(user != null) {
            	complainVo.setReplyer(user.getRealname());
            }
            this.complainService.updateComplains(complainVo);
            return ResultObj.UPDATE_SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    @RequestMapping("loadComplainsById")
    public Complain loadComplainsById(Integer id){
        return this.complainService.queryComplainsById(id);
    }

}
