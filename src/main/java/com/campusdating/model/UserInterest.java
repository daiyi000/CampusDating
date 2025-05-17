package com.campusdating.model;

/**
 * 用户兴趣关联实体类
 * 用于存储用户与兴趣爱好之间的多对多关系
 */
public class UserInterest {
    private int id;
    private int userId;
    private int interestId;
    private int interestLevel; // 1-5，表示兴趣程度
    
    // 默认构造函数
    public UserInterest() {
    }
    
    // 完整参数构造函数
    public UserInterest(int id, int userId, int interestId, int interestLevel) {
        this.id = id;
        this.userId = userId;
        this.interestId = interestId;
        this.interestLevel = interestLevel;
    }
    
    // 基本构造函数
    public UserInterest(int userId, int interestId) {
        this.userId = userId;
        this.interestId = interestId;
        this.interestLevel = 3; // 默认兴趣级别为中等
    }
    
    // 带兴趣级别的构造函数
    public UserInterest(int userId, int interestId, int interestLevel) {
        this.userId = userId;
        this.interestId = interestId;
        this.interestLevel = interestLevel;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }

    public int getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(int interestLevel) {
        if (interestLevel < 1) {
            this.interestLevel = 1;
        } else if (interestLevel > 5) {
            this.interestLevel = 5;
        } else {
            this.interestLevel = interestLevel;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserInterest that = (UserInterest) o;
        
        if (userId != that.userId) return false;
        return interestId == that.interestId;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + interestId;
        return result;
    }

    @Override
    public String toString() {
        return "UserInterest{" +
                "id=" + id +
                ", userId=" + userId +
                ", interestId=" + interestId +
                ", interestLevel=" + interestLevel +
                '}';
    }
}