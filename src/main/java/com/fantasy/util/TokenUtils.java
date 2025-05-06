package com.fantasy.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenUtils {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "ThisIsASecretKey"; // 16, 24 or 32 bytes
    private static final String INIT_VECTOR = "RandomInitVector"; // 16 bytes

    /**
     * 加密用户名和路径生成token
     * @param username 用户名
     * @param dbfPath 路径
     * @return 加密后的token字符串
     */
    public static String encrypt(String username, String dbfPath,String ip) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            // 将用户名和路径用特殊字符连接起来
            String originalText = username + "|||" + dbfPath+"|||"+ip;
            byte[] encrypted = cipher.doFinal(originalText.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 解密token获取用户名和路径
     * @param token 加密的token字符串
     * @return 包含用户名和路径的字符串数组，[0]为用户名，[1]为路径
     */
    public static String[] decrypt(String token) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(token));
            String originalText = new String(original);

            // 分割用户名和路径
            return originalText.split("\\|\\|\\|", 3);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // 测试示例
        String username = "OR";
        String dbfPath = "D:/work/parttime/zhiwei-master/src/main/resources/dbf/new2";
        String ip = "http://127.0.0.1";

        // 加密
        String token = encrypt(username, dbfPath,ip);
        System.out.println("加密后的Token: " + token);
        
        // 解密
        String[] result = decrypt(token);
        if (result != null && result.length == 3) {
            System.out.println("解密后的用户名: " + result[0]);
            System.out.println("解密后的路径: " + result[1]);
            System.out.println("解密后的路径: " + result[2]);
        }
    }
}