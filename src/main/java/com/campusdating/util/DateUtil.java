package com.campusdating.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 用于日期的格式化和转换
 */
public class DateUtil {

    // 日期格式：年-月-日
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // 日期时间格式：年-月-日 时:分:秒
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // 时间格式：时:分:秒
    public static final String TIME_FORMAT = "HH:mm:ss";
    
    /**
     * 格式化日期为字符串
     * @param date 日期
     * @param pattern 格式模式
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 格式化日期为默认日期格式字符串
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }
    
    /**
     * 格式化日期为默认日期时间格式字符串
     * @param date 日期
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DATETIME_FORMAT);
    }
    
    /**
     * 格式化日期为默认时间格式字符串
     * @param date 日期
     * @return 格式化后的时间字符串
     */
    public static String formatTime(Date date) {
        return formatDate(date, TIME_FORMAT);
    }
    
    /**
     * 解析字符串为日期
     * @param dateString 日期字符串
     * @param pattern 格式模式
     * @return 解析后的日期，如果解析失败返回null
     */
    public static Date parseDate(String dateString, String pattern) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 解析字符串为默认日期格式的日期
     * @param dateString 日期字符串
     * @return 解析后的日期，如果解析失败返回null
     */
    public static Date parseDate(String dateString) {
        return parseDate(dateString, DATE_FORMAT);
    }
    
    /**
     * 解析字符串为默认日期时间格式的日期
     * @param dateTimeString 日期时间字符串
     * @return 解析后的日期，如果解析失败返回null
     */
    public static Date parseDateTime(String dateTimeString) {
        return parseDate(dateTimeString, DATETIME_FORMAT);
    }
    
    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static Date getCurrentDate() {
        return new Date();
    }
    
    /**
     * 获取当前日期的字符串表示
     * @return 当前日期的字符串表示
     */
    public static String getCurrentDateString() {
        return formatDate(getCurrentDate());
    }
    
    /**
     * 获取当前日期时间的字符串表示
     * @return 当前日期时间的字符串表示
     */
    public static String getCurrentDateTimeString() {
        return formatDateTime(getCurrentDate());
    }
    
    /**
     * 获取当前时间的字符串表示
     * @return 当前时间的字符串表示
     */
    public static String getCurrentTimeString() {
        return formatTime(getCurrentDate());
    }
    
    /**
     * 计算两个日期之间的天数差
     * @param date1 日期1
     * @param date2 日期2
     * @return 天数差
     */
    public static int daysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        // 将日期转换为毫秒数
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        // 计算天数差
        long days = Math.abs(time1 - time2) / (1000 * 60 * 60 * 24);
        return (int) days;
    }
    
    /**
     * 计算年龄
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        // 检查是否已过生日
        if (birth.get(Calendar.MONTH) > now.get(Calendar.MONTH) || 
            (birth.get(Calendar.MONTH) == now.get(Calendar.MONTH) && 
             birth.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }
    
    /**
     * 获取指定日期的年份
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    
    /**
     * 获取指定日期的月份
     * @param date 日期
     * @return 月份（1-12）
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1; // 月份从0开始
    }
    
    /**
     * 获取指定日期的日
     * @param date 日期
     * @return 日
     */
    public static int getDay(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * 获取指定天数前的日期
     * @param date 基准日期
     * @param days 天数
     * @return 指定天数前的日期
     */
    public static Date getDateBefore(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }
    
    /**
     * 获取指定天数后的日期
     * @param date 基准日期
     * @param days 天数
     * @return 指定天数后的日期
     */
    public static Date getDateAfter(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
    
    /**
     * 将Java.util.Date转换为java.sql.Date
     * @param date Java.util.Date日期
     * @return java.sql.Date日期
     */
    public static java.sql.Date toSqlDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }
    
    /**
     * 将Java.util.Date转换为java.sql.Timestamp
     * @param date Java.util.Date日期
     * @return java.sql.Timestamp时间戳
     */
    public static java.sql.Timestamp toSqlTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Timestamp(date.getTime());
    }
}