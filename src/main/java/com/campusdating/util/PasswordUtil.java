package com.campusdating.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * 密码工具类
 * 处理密码的加密和验证
 */
public class PasswordUtil {

    // 盐的长度
    private static final int SALT_LENGTH = 16;
    
    // 随机数生成器
    private static final Random RANDOM = new SecureRandom();
    
    /**
     * 生成随机盐
     * @return 随机盐字符串
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * 使用MD5算法对密码进行加密
     * @param password 原始密码
     * @param salt 盐
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // 如果MD5算法不可用，则返回原始密码（实际项目中不应该这样做）
            return password;
        }
    }
    
    /**
     * 验证密码是否匹配
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的加密密码
     * @param salt 盐
     * @return 如果匹配返回true
     */
    public static boolean verifyPassword(String inputPassword, String storedPassword, String salt) {
        String encryptedInput = encryptPassword(inputPassword, salt);
        return encryptedInput.equals(storedPassword);
    }
    
    /**
     * 生成随机验证码
     * @param length 验证码长度
     * @return 随机验证码
     */
    public static String generateVerificationCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            verificationCode.append(chars.charAt(index));
        }
        return verificationCode.toString();
    }
    
    /**
     * 生成6位数字验证码
     * @return 6位数字验证码
     */
    public static String generateNumericVerificationCode() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
    
    /**
     * 判断密码强度
     * @param password 密码
     * @return 密码强度（1-5，5为最强）
     */
    public static int checkPasswordStrength(String password) {
        int score = 0;
        
        // 基础长度检查
        if (password.length() >= 8) {
            score += 1;
        }
        if (password.length() >= 12) {
            score += 1;
        }
        
        // 检查是否包含数字
        if (password.matches(".*\\d.*")) {
            score += 1;
        }
        
        // 检查是否包含小写字母
        if (password.matches(".*[a-z].*")) {
            score += 1;
        }
        
        // 检查是否包含大写字母
        if (password.matches(".*[A-Z].*")) {
            score += 1;
        }
        
        // 检查是否包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            score += 1;
        }
        
        // 最高分为5分
        return Math.min(5, score);
    }
    
    /**
     * 生成随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}