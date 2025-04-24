package com.fantasy.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有 RuntimeException（包括 SQL 异常）
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        if (e.getMessage().contains("sql")) {  // 根据异常信息筛选特定异常
            System.err.println("[致命错误] 检测到SQL异常，触发自动重启: " + e.getMessage());
            triggerRestart();
        }
    }
    @Value("${restart.path}")
    private String RESTART_PATH;

    // 执行重启脚本
    private void triggerRestart() {
        try {
            String scriptPath = RESTART_PATH;  // 替换为你的脚本路径
            log.info("RESTART_PATH:{}", scriptPath);

            Runtime.getRuntime().exec("cmd /c start " + scriptPath);
        } catch (IOException ex) {
            System.err.println("执行重启脚本失败: " + ex.getMessage());
            log.error("执行重启脚本失败: {}",ex.getMessage(), ex);
        }
    }
}