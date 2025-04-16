package com.fantasy.controller;

import com.fantasy.model.Cash;
import com.fantasy.model.Result.PageResult;
import com.fantasy.model.Result.Result;
import com.fantasy.model.dto.CashDTO;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.service.impl.CashService;
import com.fantasy.service.impl.LoginUserService;
import com.fantasy.util.StringUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping({ "/cash" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "审批接口")
@Slf4j
public class CashController {
    @Autowired
    private CashService cashService;
    @Autowired
    private HttpSession session;
    @Autowired
    private LoginUserService loginUserService;

    @PostMapping("/add")
    public Result add(@RequestBody Cash cash, HttpServletRequest request) {
        log.info("add cash{}", cash);
        String token = request.getHeader("Authorization");
        if(!checkLogin(token)){
            return Result.error("请先登录");
        }
        UserDTO user = loginUserService.getLoginUser(token);
        cash.setDbfPath(user.getDbfPath());
        int result = 0;
        try {
            result = cashService.addCash(cash,user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        if (result == 1) {
            return Result.ok(result);
        }
        return Result.error();
    }
    @PostMapping("/update")
    public Result update(@RequestBody Cash cash, HttpServletRequest request) {
        log.info("update cash:{}", cash);
        String token = request.getHeader("Authorization");
        if(!checkLogin(token)){
            return Result.error("请先登录");
        }
        UserDTO user = loginUserService.getLoginUser(token);
        cash.setDbfPath(user.getDbfPath());
        int result = cashService.updateCash(cash);
        if (result == 1) {
            return Result.ok(result);
        }
        return Result.error();
    }
    @GetMapping("/list")
    public PageResult<Cash> list(CashDTO cashDTO, HttpServletRequest request) {
        log.info("list cashDTO:{}", cashDTO);
        String token = request.getHeader("Authorization");
        if(!checkLogin(token)){
            return new PageResult<>();
        }
        try {
            UserDTO user = loginUserService.getLoginUser(token);
            cashDTO.setDbfPath(user.getDbfPath());
            cashDTO.setCname(user.getCname());
            return cashService.list(cashDTO);
        }catch (Exception e){
            log.error("CashController.list e{}",e.getMessage());
            return new PageResult<>();
        }


    }
    // 消息推送
    @GetMapping("/send")
    public Result send(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("send token:{}",token);
        if(!checkLogin(token)){
            return Result.error("请先登录");
        }
        UserDTO user = loginUserService.getLoginUser(token);

        List<Cash> cashList = cashService.send(user);
        return Result.ok(cashList);

    }
    @GetMapping("/get")
    public Result get(@RequestParam("no") String no,HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("get token:{}", token);
        if(!checkLogin(token)){
            return Result.error("请先登录");
        }
        UserDTO user = loginUserService.getLoginUser(token);
        Cash cash = cashService.getByNo(user.getDbfPath(),no);
        return Result.ok(cash);
    }

    private boolean checkLogin(String token){
        UserDTO user = loginUserService.getLoginUser(token);
        if (user == null) {
            return false;
        }

        if (StringUtils.isEmpty(user.getDbfPath()) || StringUtils.isEmpty(user.getPeson())){
           return false;
        }
        return true;
    }
}
