package com.fantasy.controller;

import com.fantasy.entity.LoginUser;
import com.fantasy.model.Result.Result;
import com.fantasy.model.vo.MpordeVO;
import com.fantasy.model.vo.MporintpVO;
import com.fantasy.service.impl.MpordeInfoService;
import com.fantasy.util.StringUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping({ "/common/mporde" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "用户接口")
public class MpordeInfoController {

    @Autowired
    private MpordeInfoService mpordeInfoService;

    @Autowired
    private HttpSession session;

    @PostMapping("/getUnPayPurchaseOrders")
    @ApiOperation(value = "获取未交采购单",notes = "1")
    public Result getUnPayPurchaseOrders(@RequestBody MpordeVO mpordeVO){
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            mpordeVO.setDbfPath(user.getDbfPath());
            //用户名
            if (StringUtils.isEmpty(mpordeVO.getJmkcuna())) {
                mpordeVO.setJmkcuna(user.getCname());
            }
            if (StringUtils.isEmpty(mpordeVO.getMkcuno())) {
                mpordeVO.setMkcuno(user.getPeson());
            }
        }
        if (StringUtils.isEmpty(mpordeVO.getDbfPath()) || StringUtils.isEmpty(mpordeVO.getMkcuno())){
            return Result.error("请先登录");
        }
        return mpordeInfoService.getUnPayPurchaseOrders(mpordeVO);
    }

    @PostMapping("/getSubmitMporintps")
    @ApiOperation(value = "获取已交货资料",notes = "2")
    public Result getSubmitMporintps(@RequestBody MporintpVO mporintpVO){
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            mporintpVO.setDbfPath(user.getDbfPath());
            //用户名
            if (StringUtils.isEmpty(mporintpVO.getMkcuno())) {
                mporintpVO.setMkcuno(user.getPeson());
            }
        }
        if (StringUtils.isEmpty(mporintpVO.getDbfPath()) || StringUtils.isEmpty(mporintpVO.getMkcuno())){
            return Result.error("请先登录");
        }
        return mpordeInfoService.getSubmitMporintps(mporintpVO);
    }

    @PostMapping("/deleteMporintp")
    @ApiOperation(value = "删除已交货资料",notes = "3")
    public Result deleteMporintp(@RequestBody MporintpVO mporintpVO){
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            mporintpVO.setDbfPath(user.getDbfPath());
            //用户名
            if (StringUtils.isEmpty(mporintpVO.getMkcuno())) {
                mporintpVO.setMkcuno(user.getPeson());
            }
        }
        if (StringUtils.isEmpty(mporintpVO.getDbfPath()) || StringUtils.isEmpty(mporintpVO.getMkcuno())){
            return Result.error("请先登录");
        }
        return mpordeInfoService.deleteMporintp(mporintpVO);
    }

}
