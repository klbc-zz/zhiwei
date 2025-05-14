package com.fantasy.controller;

import com.fantasy.entity.Connect;
import com.fantasy.entity.LoginUser;
import com.fantasy.model.Result.Result;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.service.impl.LoginUserService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping({ "/common/user" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private HttpSession session;

    @PostMapping("/testConnect")
    @ApiOperation(value = "测试连接",notes = "1")
    public Result testConnect(@RequestBody Connect connect){
        loginUserService.testConnect(connect);
        session.setAttribute("connect",connect.getDbfPath());
        return Result.ok("连接成功");
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "2")
    public Result login(@RequestBody LoginUser loginUser){
        log.info("login user:{}",loginUser);
        Object connect = session.getAttribute("connect");
        if (connect != null) {
            loginUser.setDbfPath((String) connect);
        }
        if(loginUser.getIp()==null||loginUser.getIp().equals("")){
            return Result.error("登录失败，ip为空");
        }
        UserDTO login = loginUserService.login(loginUser);
        if (login.isLogin()) {
            session.setAttribute("loginUser",login);
            return Result.ok("登录成功",login);
        }else {
            return Result.error("登录失败");
        }
    }

}
