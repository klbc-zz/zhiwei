package com.fantasy.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/check" })
@ApiSupport(author = "fantasy0521")
@Api(tags = "服务检查接口")
public class CheckController {
    @GetMapping("")
    public String check(){
        return "ok";
    }
    @GetMapping("/test")
    public String test() {
        simulateSqlError();
        return "This line won't be reached";
    }
    public void simulateSqlError() {
        throw new RuntimeException("sql执行出现异常：Connection refused");
    }
}
