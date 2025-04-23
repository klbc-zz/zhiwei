@echo off
REM 修复版重启脚本（兼容Java调用）

setlocal enabledelayedexpansion  <!-- 启用延迟变量扩展 -->

set "APP_NAME=zhiwei-0.0.1-SNAPSHOT.jar"
set "APP_PATH=D:\app\zhiwei\%APP_NAME%"
set "LOG_DIR=D:\app\zhiwei"
set "LOG_FILE=%LOG_DIR%\restart.log"

REM 1. 创建日志目录（确保路径规范）
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

REM 2. 记录时间戳（避免空格问题）
echo [%date% %time%] 开始重启 >> "%LOG_FILE%"

REM 3. 终止进程（兼容jps不存在的情况）
where jps >nul 2>&1
if !errorlevel! equ 0 (
    for /f "tokens=1 delims= " %%i in ('jps -l ^| find "%APP_NAME%"') do (
        echo [%date% %time%] 终止进程 PID=%%i >> "%LOG_FILE%"
        taskkill /F /PID %%i >> "%LOG_FILE%" 2>&1
    )
) else (
    echo [%date% %time%] 使用tasklist替代jps >> "%LOG_FILE%"
    for /f "tokens=2 delims=," %%i in ('tasklist /nh /fi "imagename eq java.exe" /fo csv ^| find "%APP_NAME%"') do (
        set pid=%%~i
        echo [%date% %time%] 终止进程 PID=!pid! >> "%LOG_FILE%"
        taskkill /F /PID !pid! >> "%LOG_FILE%" 2>&1
    )
)

REM 4. 重启应用（显式指定工作目录）
echo [%date% %time%] 启动应用... >> "%LOG_FILE%"
cd /d "D:\app\zhiwei"
start "" /B javaw -jar "%APP_PATH%" --server.port=8087 >> "%LOG_DIR%\app.log" 2>&1

echo [%date% %time%] 重启完成 >> "%LOG_FILE%"
endlocal
exit /b 0