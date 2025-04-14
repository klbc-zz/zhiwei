@echo off
set JAR_PATH=E:\dev\Feeding-0.0.1-SNAPSHOT.jar

echo Stopping the application...
tasklist /FI "IMAGENAME eq java.exe" | find /I "Feeding-0.0.1-SNAPSHOT.jar" > nul
if %ERRORLEVEL% equ 0 (
    for /F "tokens=2 delims=," %%P in ('tasklist /NH /FI "IMAGENAME eq java.exe" ^| find /I "Feeding-0.0.1-SNAPSHOT.jar"') do taskkill /F /PID %%P
    timeout /T 5 /NOBREAK > nul
)

echo Restarting the application...
start /b java -jar "%JAR_PATH%" --server.port=5005