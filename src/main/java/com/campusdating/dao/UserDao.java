package com.campusdating.dao;

import com.campusdating.model.User;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象类
 * 负责User对象的CRUD操作
 */
public class UserDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public UserDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存用户到数据库
     * @param user 用户对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(User user) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO users(username, password, email, phone, active, role, register_time, verification_code, verified) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setBoolean(5, user.isActive());
            ps.setString(6, user.getRole());
            ps.setTimestamp(7, user.getRegisterTime());
            ps.setString(8, user.getVerificationCode());
            ps.setBoolean(9, user.isVerified());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(User user) {
        boolean flag = false;
        try {
            String sql = "UPDATE users SET username=?, password=?, email=?, phone=?, active=?, role=?, last_login_time=?, verification_code=?, verified=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setBoolean(5, user.isActive());
            ps.setString(6, user.getRole());
            ps.setTimestamp(7, user.getLastLoginTime());
            ps.setString(8, user.getVerificationCode());
            ps.setBoolean(9, user.isVerified());
            ps.setInt(10, user.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public User findById(int id) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户对象，如果不存在返回null
     */
    public User findByUsername(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * 根据邮箱获取用户
     * @param email 邮箱
     * @return 用户对象，如果不存在返回null
     */
    public User findByEmail(String email) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE email=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 验证用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，如果验证失败返回null
     */
    public User validateLogin(String username, String password) {
        User user = null;
        try {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
                // 更新最后登录时间
                user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
                update(user);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * 更新最后登录时间
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean updateLastLoginTime(int userId) {
        boolean flag = false;
        try {
            String sql = "UPDATE users SET last_login_time=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, userId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据验证码验证用户
     * @param verificationCode 验证码
     * @return 成功返回true，失败返回false
     */
    public boolean verifyUser(String verificationCode) {
        boolean flag = false;
        try {
            String sql = "UPDATE users SET verified=true WHERE verification_code=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, verificationCode);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    public boolean isUsernameExists(String username) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    public boolean isEmailExists(String email) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE email=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    /**
     * 将ResultSet映射为User对象
     * @param rs ResultSet
     * @return User对象
     * @throws SQLException SQLException
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setActive(rs.getBoolean("active"));
        user.setRole(rs.getString("role"));
        user.setRegisterTime(rs.getTimestamp("register_time"));
        user.setLastLoginTime(rs.getTimestamp("last_login_time"));
        user.setVerificationCode(rs.getString("verification_code"));
        user.setVerified(rs.getBoolean("verified"));
        return user;
    }
}