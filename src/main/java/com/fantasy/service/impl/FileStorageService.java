package com.fantasy.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String baseUploadDir;

    @Value("${file.allowed-types}")
    private String allowedTypes;

    @Value("${file.access-base-url}")
    private String accessBaseUrl;

    public String storeFile(MultipartFile file,String basePath,String baseIp, String folder) throws IOException {
        // 验证文件类型
        if (!isFileTypeAllowed(file.getContentType())) {
            throw new IllegalArgumentException("不支持的文件类型");
        }

        // 清理文件夹名称，防止路径遍历攻击
        String safeFolder = sanitizeFolderName(folder);

        // 创建完整上传路径(基础路径 + 文件夹)
        Path uploadPath = Paths.get(baseUploadDir, safeFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 保存文件
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        // 返回访问URL (包含文件夹路径)
        return baseIp + accessBaseUrl + safeFolder + "/" + uniqueFileName;
    }

    private boolean isFileTypeAllowed(String contentType) {
        List<String> allowed = Arrays.asList(allowedTypes.split(","));
        return allowed.contains(contentType);
    }

    private String sanitizeFolderName(String folder) {
        // 移除路径中的非法字符，防止目录遍历攻击
        return folder.replaceAll("[^a-zA-Z0-9-_]", "");
    }
}