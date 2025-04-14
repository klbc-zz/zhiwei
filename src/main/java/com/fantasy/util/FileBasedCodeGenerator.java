package com.fantasy.util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class FileBasedCodeGenerator {
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private static final ReentrantLock lock = new ReentrantLock();
    
    // 流水号文件存储路径
    private static final String SEQ_FILE_PATH = "serial_number.txt";
    
    /**
     * 生成编码
     * @param company 帐套号(2位)
     * @return 生成的编码
     */
    public static String generateCode(String company) throws IOException {
        // 验证帐套号
        if (company == null || company.length() != 2) {
            throw new IllegalArgumentException("帐套号必须为2位字符");
        }
        
        lock.lock();  // 加锁保证线程安全
        try {
            // 1. 获取当前年月
            String yearMonth = LocalDate.now().format(MONTH_FORMATTER);
            yearMonth = yearMonth.substring(2, yearMonth.length());
            String compositeKey = company + yearMonth;
            
            // 2. 读取或初始化流水号
            int sequence = readAndUpdateSequence(compositeKey);
            
            // 3. 生成编码
            return compositeKey + String.format("%04d", sequence);
        } finally {
            lock.unlock();  // 释放锁
        }
    }
    
    /**
     * 读取并更新流水号
     */
    private static int readAndUpdateSequence(String compositeKey) throws IOException {
        // 如果文件不存在则创建
        if (!Files.exists(Paths.get(SEQ_FILE_PATH))) {
            Files.createFile(Paths.get(SEQ_FILE_PATH));
        }
        
        // 读取文件内容
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(SEQ_FILE_PATH))) {
            content = reader.readLine();
        }
        
        int sequence = 1;
        boolean keyExists = false;
        
        // 解析文件内容，格式为: key=value
        if (content != null && !content.isEmpty()) {
            String[] parts = content.split("=");
            if (parts.length == 2 && parts[0].equals(compositeKey)) {
                sequence = Integer.parseInt(parts[1]) + 1;
                keyExists = true;
            }
        }
        
        // 更新文件内容
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SEQ_FILE_PATH))) {
            if (keyExists) {
                writer.write(compositeKey + "=" + sequence);
            } else {
                writer.write(compositeKey + "=" + sequence);
            }
        }
        
        return sequence;
    }
    
    /**
     * 重置流水号(测试用)
     */
    public static void resetSequenceFile() throws IOException {
        lock.lock();
        try {
            Files.deleteIfExists(Paths.get(SEQ_FILE_PATH));
        } finally {
            lock.unlock();
        }
    }
}