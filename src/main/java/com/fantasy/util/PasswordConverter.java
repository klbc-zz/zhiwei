package com.fantasy.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PasswordConverter {

    // 加密函数（原函数）
    public static String passToChr(String tcpasschr) {
        String lcthischr = tcpasschr.trim();
        StringBuilder lcoldchr = new StringBuilder();
        int lnlenthischr = lcthischr.length();

        for (int ln = 0; ln < lnlenthischr; ln++) {
            char lctmpchr = lcthischr.charAt(ln);
            lctmpchr = (char)((int)lctmpchr - 93);
            lcoldchr.append(lctmpchr);
        }

        return lcoldchr.toString();
    }

    // 解密函数（新增）
    public static String chrToPass(String encryptedStr) {
        StringBuilder originalStr = new StringBuilder();
        int length = encryptedStr.length();

        for (int i = 0; i < length; i++) {
            char tmpChar = encryptedStr.charAt(i);
            tmpChar = (char)((int)tmpChar + 93); // 加密是减93，解密就加93
            originalStr.append(tmpChar);
        }

        return originalStr.toString();
    }
    public static String safePassToChr(String input) {
        StringBuilder result = new StringBuilder();
        for(char c : input.toCharArray()) {
            int encrypted = (c - 93) % 65536; // 确保在char范围内
            if(encrypted < 0) encrypted += 65536; // 处理负数
            result.append((char)encrypted);
        }
        return result.toString();
    }
    public static void printCharCodes(String s, String description) {
        System.out.println(description + " '" + s + "' 的编码值：");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            System.out.printf("字符%d: %c → Unicode: U+%04X, 十进制: %d%n",
                    i, c, (int)c, (int)c);
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
    public static String encryptDBValue(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            sb.append((char) (c + 93));
        }
        return sb.toString();
    }
    public static String encryptDBValue2(String input) {
        try{
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                sb.append((char) (c + 93));
            }
            StringBuilder sb2 = new StringBuilder();
            for (char c : sb.toString().toCharArray()) {
                sb2.append(String.format("%02X ", (int) c));
            }
            return sb2.toString();
        }catch (Exception e){
            log.error("PasswordConverter.encryptDBValue2{}",e.getMessage());
            return "";
        }

    }

    public static String decryptDBValue(String encrypted) {
        StringBuilder sb = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            sb.append((char) (c - 93)); // 加密是+93，解密就-93
        }
        return sb.toString();
    }
    public static void main(String[] args) throws UnsupportedEncodingException {

        String original = "123456";

        String encrypted2 = encryptDBValue2(original); // 加密得到 ￔￔￓ￙ￕￛ


        String encrypted = encryptDBValue(original);
        String decrypted = decryptDBValue(encrypted);

        System.out.println("原始值: " + original);
        System.out.println("加密后: " + encrypted);
        System.out.println("解密后: " + decrypted);

        // 打印十六进制
        System.out.print("加密十六进制: ");
        for (char c : encrypted.toCharArray()) {
            System.out.printf("%02X ", (int) c);
        }
        String dbValue = "帋崜彆"; // 数据库显示的值

        byte[] encryptedBytes = encrypted2.getBytes("UTF-8"); // 或者 GBK
        System.out.println("加密后的十六进制: " + bytesToHex(encryptedBytes));
//        printCharCodes(encrypted, "加密结果");
//        printCharCodes(dbValue, "数据库值");
        String dbfPath = "D:\\work\\parttime\\zhiwei-master\\src\\main\\resources\\dbf\\new2";
        // 测试示例
//        String original = "110628";
//        System.out.println("原始字符串: " + original);
//
//        // 加密
//        String encrypted = passToChr(original);
//        String encrypted2 = safePassToChr(original);
//        System.out.println("加密后: " + encrypted);
//        System.out.println("加密后2: " + encrypted2);

        List<Object> params = new ArrayList<Object>();
        List<Map<String, Object>> maps = DBFSqlUtils.executeQuerySqlListResult2(dbfPath, "select  PASSWORD from Newview",params);
        if (maps.isEmpty()) {
            throw new RuntimeException("找不到该账号");
        }
        for(Map<String, Object> map : maps) {
            String CNAME = (String) map.get("PASSWORD");
            String PASSWORD1 = (String) map.get("PASSWORD");
            System.out.println(CNAME + ":" + PASSWORD1);
        }
    }
}