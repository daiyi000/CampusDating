package com.campusdating.util;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 用于数据验证
 */
public class ValidationUtil {

    // 邮箱正则表达式
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    // 手机号正则表达式（中国手机号）
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    
    // 用户名正则表达式（字母开头，允许字母、数字和下划线，长度4-16）
    private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";
    
    // 密码正则表达式（至少包含一个字母和一个数字，长度6-20）
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,20}$";
    
    // 编译正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    
    /**
     * 验证邮箱格式
     * @param email 邮箱
     * @return 如果格式正确返回true
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证手机号格式
     * @param phone 手机号
     * @return 如果格式正确返回true
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 验证用户名格式
     * @param username 用户名
     * @return 如果格式正确返回true
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * 验证密码格式
     * @param password 密码
     * @return 如果格式正确返回true
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 验证两个密码是否相同
     * @param password 密码
     * @param confirmPassword 确认密码
     * @return 如果相同返回true
     */
    public static boolean isPasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    
    /**
     * 验证字符串是否为空或null
     * @param str 字符串
     * @return 如果为空或null返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 验证字符串是否不为空且不为null
     * @param str 字符串
     * @return 如果不为空且不为null返回true
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * 验证字符串长度是否在指定范围内
     * @param str 字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 如果在范围内返回true
     */
    public static boolean isLengthValid(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * 验证数字是否在指定范围内
     * @param value 数字
     * @param min 最小值
     * @param max 最大值
     * @return 如果在范围内返回true
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * 验证是否为整数
     * @param str 字符串
     * @return 如果是整数返回true
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 验证是否为双精度浮点数
     * @param str 字符串
     * @return 如果是双精度浮点数返回true
     */
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 验证是否为URL
     * @param url URL字符串
     * @return 如果是URL返回true
     */
    public static boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 验证是否为日期格式（yyyy-MM-dd）
     * @param date 日期字符串
     * @return 如果是日期格式返回true
     */
    public static boolean isValidDate(String date) {
        if (date == null) {
            return false;
        }
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}