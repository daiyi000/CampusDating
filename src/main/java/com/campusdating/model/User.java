package com.campusdating.model;

import java.sql.Timestamp;

/**
 * 用户实体类
 * 用于存储用户基本信息
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean active;
    private String role;
    private Timestamp registerTime;
    private Timestamp lastLoginTime;
    private String verificationCode;
    private boolean verified;
    
    // 默认构造函数
    public User() {
    }
    
    // 完整参数构造函数
    public User(int id, String username, String password, String email, String phone, 
                boolean active, String role, Timestamp registerTime, Timestamp lastLoginTime,
                String verificationCode, boolean verified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.active = active;
        this.role = role;
        this.registerTime = registerTime;
        this.lastLoginTime = lastLoginTime;
        this.verificationCode = verificationCode;
        this.verified = verified;
    }
    
    // 注册用户构造函数
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = true;
        this.role = "user";
        this.registerTime = new Timestamp(System.currentTimeMillis());
        this.verified = false;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", role='" + role + '\'' +
                ", registerTime=" + registerTime +
                '}';
    }
}