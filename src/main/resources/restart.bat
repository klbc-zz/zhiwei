@echo off

set JAR_NAME=zhiwei-0.0.1-SNAPSHOT.jar
set JAR_PATH=D:\app\zhiwei\%JAR_NAME%
set LOG_PATH=D:\app\zhiwei\app.log
set PORT=8087

echo Stopping the application...

for /f "tokens=1 delims= " %%i in (
    'jps -l ^| find "%JAR_NAME%"'
) do (
    taskkill /F /PID %%i
)

echo Wait for the process to be released...
timeout /t 3 /nobreak > nul

echo Restarting the application...

start /b javaw -jar D:\app\zhiwei\zhiwei-0.0.1-SNAPSHOT.jar --server.port=8087 >> D:\app\zhiwei\app.log 2>&1

echo The service was successfully restarted
