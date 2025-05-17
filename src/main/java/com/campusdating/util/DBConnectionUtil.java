package com.campusdating.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 提供获取数据库连接的方法
 */
public class DBConnectionUtil {

    // 数据库连接URL
    private static final String URL = "jdbc:mysql://localhost:3306/campus_dating?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    
    // 数据库用户名
    private static final String USER = "root";
    
    // 数据库密码
    private static final String PASSWORD = "123456";
    
    // JDBC驱动名
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // 静态代码块，加载驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("加载MySQL驱动失败！");
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws SQLException 如果获取连接失败
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("获取数据库连接失败：" + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 关闭数据库连接
     * @param connection 数据库连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("关闭数据库连接失败！");
            }
        }
    }
    
    /**
     * 测试数据库连接
     * @return 如果连接成功返回true
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null;
        } catch (SQLException e) {
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}