package com.fantasy.controller;

import com.fantasy.entity.LoginUser;
import com.fantasy.entity.Mater;
import com.fantasy.entity.OrderBK;
import com.fantasy.model.Result.Result;
import com.fantasy.model.vo.MpordeVO;
import com.fantasy.model.vo.MporintpVO;
import com.fantasy.service.impl.ScanService;
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
@RequestMapping({ "/common/scan" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "扫码接口")
public class ScanController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ScanService service;

    /**
     * 成品入库扫码
     * @param orderBK
     * @return
     * @throws Exception
     */
    @PostMapping("/scanOrder")
    @ApiOperation(value = "",notes = "1")
    public Result scanOrder(@RequestBody OrderBK orderBK) throws Exception {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            orderBK.setPdusername(user.getCname());
            orderBK.setDbfPath(user.getDbfPath());
        }
        if (orderBK.getDbfPath().isEmpty() || orderBK.getPdusername().isEmpty()){
            return Result.error("请先登录");
        }
        return service.scanOrder(orderBK);
    }

    /**
     * 采购入库扫码
     * @param mpordeVO
     * @return
     * @throws Exception
     */
    @PostMapping("/scanPurchase")
    @ApiOperation(value = "",notes = "2")
    public Result scanPurchase(@RequestBody MpordeVO mpordeVO) throws Exception {
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
        return service.scanPurchase(mpordeVO);
    }

    /**
     * 采购入库
     * @param purchase
     * @return
     * @throws Exception
     */
    @PostMapping("/savePurchase")
    @ApiOperation(value = "",notes = "2")
    public Result savePurchase(@RequestBody MporintpVO purchase) throws Exception {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            purchase.setDbfPath(user.getDbfPath());
            if (StringUtils.isEmpty(purchase.getMkcuno())) {
                purchase.setMkoutno(user.getPeson());
            }
        }
        if (StringUtils.isEmpty(purchase.getDbfPath()) || StringUtils.isEmpty(purchase.getMkcuno())){
            return Result.error("请先登录");
        }
        return service.savePurchase(purchase);
    }



    /**
     * 加工入库扫码
     * @param mater
     * @return
     * @throws Exception
     */
    @PostMapping("/scanMater")
    @ApiOperation(value = "",notes = "3")
    public Result scanMater(@RequestBody Mater mater) throws Exception {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            mater.setDbfPath(user.getDbfPath());
        }else if (mater.getDbfPath().isEmpty()){
            return Result.error("请先登录");
        }
        return service.scanMater(mater);
    }

    /**
     * 加工入库
     * @param mater
     * @return
     * @throws Exception
     */
    @PostMapping("/saveMater")
    @ApiOperation(value = "",notes = "3")
    public Result saveMater(@RequestBody Mater mater) throws Exception {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            LoginUser user = (LoginUser) loginUser;
            mater.setDbfPath(user.getDbfPath());
        }else if (mater.getDbfPath().isEmpty()){
            return Result.error("请先登录");
        }
        return service.saveMater(mater);
    }


}
