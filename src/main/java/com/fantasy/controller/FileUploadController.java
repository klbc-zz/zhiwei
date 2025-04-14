package com.fantasy.controller;

import com.fantasy.entity.LoginUser;
import com.fantasy.model.Result.Result;
import com.fantasy.model.dto.UserDTO;
import com.fantasy.service.impl.FileStorageService;
import com.fantasy.service.impl.LoginUserService;
import com.fantasy.util.StringUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/files")
@ApiSupport(author = "fantasy0521")
@Api(tags = "文件上传接口")
public class FileUploadController {
    
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private LoginUserService loginUserService;
    @Autowired
    private HttpSession session;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        try {
            UserDTO user = loginUserService.getLoginUser(token);
            String folder = "";
            if (user != null) {
                if (StringUtils.isEmpty(user.getDbfPath()) || StringUtils.isEmpty(user.getPeson())){
                    return ResponseEntity.badRequest().body("请先登录");
                }
                folder = user.getPeson();
            }else {
                return ResponseEntity.badRequest().body("请先登录");
            }

            String fileUrl = fileStorageService.storeFile(file,folder);
            return ResponseEntity.ok().body(new UploadResponse(true, "文件上传成功", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UploadResponse(false, "文件上传失败: " + e.getMessage(), null));
        }
    }
    
    // 响应对象
    private static class UploadResponse {
        private boolean success;
        private String message;
        private String fileUrl;
        
        public UploadResponse(boolean success, String message, String fileUrl) {
            this.success = success;
            this.message = message;
            this.fileUrl = fileUrl;
        }
        
        // getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getFileUrl() { return fileUrl; }
    }
}