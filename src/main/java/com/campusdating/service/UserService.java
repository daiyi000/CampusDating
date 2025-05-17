package com.campusdating.service;

import com.campusdating.dao.UserDao;
import com.campusdating.model.User;
import com.campusdating.util.PasswordUtil;
import com.campusdating.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户服务类
 * 提供用户相关的业务逻辑
 */
public class UserService {
    
    private UserDao userDao;
    
    /**
     * 构造函数
     */
    public UserService() {
        userDao = new UserDao();
    }
    
    /**
     * 用户注册
     * @param user 用户对象
     * @return 成功返回true，失败返回false
     */
    public boolean register(User user) {
        // 验证用户信息
        if (user == null || ValidationUtil.isEmpty(user.getUsername()) || 
            ValidationUtil.isEmpty(user.getPassword()) || ValidationUtil.isEmpty(user.getEmail())) {
            return false;
        }
        
        // 检查用户名和邮箱是否已存在
        if (isUsernameExists(user.getUsername()) || isEmailExists(user.getEmail())) {
            return false;
        }
        
        // 生成盐值
        String salt = PasswordUtil.generateSalt();
        
        // 加密密码
        String encryptedPassword = PasswordUtil.encryptPassword(user.getPassword(), salt);
        
        // 设置加密后的密码
        user.setPassword(encryptedPassword + ":" + salt);
        
        // 设置注册时间
        if (user.getRegisterTime() == null) {
            user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
        }
        
        // 设置默认角色
        if (ValidationUtil.isEmpty(user.getRole())) {
            user.setRole("user");
        }
        
        // 设置账户状态为活动
        user.setActive(true);
        
        // 生成验证码
        String verificationCode = PasswordUtil.generateVerificationCode(8);
        user.setVerificationCode(verificationCode);
        
        // 默认未验证
        user.setVerified(false);
        
        // 保存用户
        return userDao.save(user);
    }
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    public User login(String username, String password) {
        // 验证参数
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            return null;
        }
        
        // 获取用户
        User user = userDao.findByUsername(username);
        
        // 如果用户不存在
        if (user == null) {
            return null;
        }
        
        // 检查用户状态
        if (!user.isActive()) {
            return null;
        }
        
        // 验证密码
        String storedPassword = user.getPassword();
        String[] parts = storedPassword.split(":");
        
        if (parts.length != 2) {
            return null;
        }
        
        String hashedPassword = parts[0];
        String salt = parts[1];
        
        // 验证密码是否匹配
        if (PasswordUtil.verifyPassword(password, hashedPassword, salt)) {
            // 更新最后登录时间
            user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            userDao.update(user);
            return user;
        }
        
        return null;
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    public User getUserById(int id) {
        return userDao.findById(id);
    }
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户对象，如果不存在返回null
     */
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
    
    /**
     * 根据邮箱获取用户
     * @param email 邮箱
     * @return 用户对象，如果不存在返回null
     */
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateUser(User user) {
        // 验证用户
        if (user == null || user.getId() <= 0) {
            return false;
        }
        
        // 获取现有用户
        User existingUser = userDao.findById(user.getId());
        
        // 如果用户不存在
        if (existingUser == null) {
            return false;
        }
        
        // 更新用户信息
        return userDao.update(user);
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteUser(int id) {
        return userDao.delete(id);
    }
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 成功返回true，失败返回false
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // 验证参数
        if (userId <= 0 || ValidationUtil.isEmpty(oldPassword) || ValidationUtil.isEmpty(newPassword)) {
            return false;
        }
        
        // 获取用户
        User user = userDao.findById(userId);
        
        // 如果用户不存在
        if (user == null) {
            return false;
        }
        
        // 验证旧密码
        String storedPassword = user.getPassword();
        String[] parts = storedPassword.split(":");
        
        if (parts.length != 2) {
            return false;
        }
        
        String hashedPassword = parts[0];
        String salt = parts[1];
        
        // 验证旧密码是否匹配
        if (!PasswordUtil.verifyPassword(oldPassword, hashedPassword, salt)) {
            return false;
        }
        
        // 加密新密码
        String newEncryptedPassword = PasswordUtil.encryptPassword(newPassword, salt);
        
        // 设置新密码
        user.setPassword(newEncryptedPassword + ":" + salt);
        
        // 更新用户
        return userDao.update(user);
    }
    
    /**
     * 重置密码
     * @param email 邮箱
     * @return 成功返回新密码，失败返回null
     */
    public String resetPassword(String email) {
        // 验证参数
        if (ValidationUtil.isEmpty(email)) {
            return null;
        }
        
        // 获取用户
        User user = userDao.findByEmail(email);
        
        // 如果用户不存在
        if (user == null) {
            return null;
        }
        
        // 生成新密码
        String newPassword = PasswordUtil.generateRandomPassword(8);
        
        // 生成新盐
        String salt = PasswordUtil.generateSalt();
        
        // 加密新密码
        String encryptedPassword = PasswordUtil.encryptPassword(newPassword, salt);
        
        // 设置新密码
        user.setPassword(encryptedPassword + ":" + salt);
        
        // 更新用户
        boolean success = userDao.update(user);
        
        if (success) {
            return newPassword;
        } else {
            return null;
        }
    }
    
    /**
     * 验证用户
     * @param verificationCode 验证码
     * @return 成功返回true，失败返回false
     */
    public boolean verifyUser(String verificationCode) {
        // 验证参数
        if (ValidationUtil.isEmpty(verificationCode)) {
            return false;
        }
        
        return userDao.verifyUser(verificationCode);
    }
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    public boolean isUsernameExists(String username) {
        return userDao.isUsernameExists(username);
    }
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    public boolean isEmailExists(String email) {
        return userDao.isEmailExists(email);
    }
    
    /**
     * 检查用户是否活动
     * @param userId 用户ID
     * @return 活动返回true，非活动返回false
     */
    public boolean isUserActive(int userId) {
        User user = userDao.findById(userId);
        return user != null && user.isActive();
    }
    
    /**
     * 检查用户是否已验证
     * @param userId 用户ID
     * @return 已验证返回true，未验证返回false
     */
    public boolean isUserVerified(int userId) {
        User user = userDao.findById(userId);
        return user != null && user.isVerified();
    }
    
    /**
     * 激活用户
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean activateUser(int userId) {
        User user = userDao.findById(userId);
        
        if (user == null) {
            return false;
        }
        
        user.setActive(true);
        return userDao.update(user);
    }
    
    /**
     * 停用用户
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deactivateUser(int userId) {
        User user = userDao.findById(userId);
        
        if (user == null) {
            return false;
        }
        
        user.setActive(false);
        return userDao.update(user);
    }
}