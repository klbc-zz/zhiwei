package com.fantasy.controller;

import com.fantasy.FantasyCommonApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

@RestController
public class RestartController {

//    @PostMapping("/restart")
    public String restart(String bat) {
        try {
            // 执行重启命令
//            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", bat);"java", "-jar", "D:\\project\\My\\Feeding\\target\\Feeding-0.0.1-SNAPSHOT.jar"
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "D:\\project\\My\\Feeding\\target\\Feeding-0.0.1-SNAPSHOT.jar");
            Process process = processBuilder.start();

            // 可选：读取并打印子进程的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
            return "Restart failed!";
        }
        return "Restarting application...";
    }

    @PostMapping("/exit")
    public void exit(){
        // 注意：此方法执行后，当前应用将会终止，新的应用实例将启动
        System.exit(0); // 确保当前JVM退出
    }


    @PostMapping("/restart2")
    public void restart2(String port){
        try {
//            Runtime.getRuntime().exec("cmd /c start D:\\project\\My\\Feeding\\src\\main\\resources\\restart.bat");
//            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "/b", "D:\\project\\My\\Feeding\\src\\main\\resources\\restart.bat");
            ProcessBuilder pb;
            if (port.equals("5005")){
                pb = new ProcessBuilder("cmd", "/c", "start", "/b", "E:\\dev\\restart2.bat");
            }else {
                pb = new ProcessBuilder("cmd", "/c", "start", "/b", "E:\\dev\\restart.bat");
            }
            pb.inheritIO(); // 可选：允许继承输入输出流以便于日志记录
            Process p = pb.start();
            // 注意：这之后应用将终止，无法返回HTTP响应
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}