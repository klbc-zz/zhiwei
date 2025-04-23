@echo off
REM 重启脚本路径: C:\your-app\restart.bat

REM 1. 杀死当前进程
for /f "tokens=1,2 delims= " %%A in ('jps -l ^| find "zhiwei-0.0.1-SNAPSHOT.jar"') do (
    echo Killing PID %%A...
    taskkill /F /PID %%A
)

REM 2. 等待3秒确保进程终止
timeout /t 3 /nobreak >nul

REM 3. 重新启动应用
echo Restarting application...

start "" javaw -jar D:\app\zhiwei\zhiwei-0.0.1-SNAPSHOT.jar --server.port=8087 >> D:\app\zhiwei\app.log 2>&1

echo Application restarted.
exit