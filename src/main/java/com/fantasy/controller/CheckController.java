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
}
