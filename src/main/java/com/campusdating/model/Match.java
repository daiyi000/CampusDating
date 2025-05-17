package com.campusdating.model;

import java.sql.Timestamp;

/**
 * 匹配实体类
 * 用于存储用户间的匹配关系
 */
public class Match {
    private int id;
    private int userOneId;
    private int userTwoId;
    private int matchScore;
    private String matchStatus; // pending, accepted, rejected
    private Timestamp createTime;
    private Timestamp updateTime;
    private boolean isActive;
    private String matchNotes;
    
    // 默认构造函数
    public Match() {
    }
    
    // 完整参数构造函数
    public Match(int id, int userOneId, int userTwoId, int matchScore, 
                 String matchStatus, Timestamp createTime, Timestamp updateTime, 
                 boolean isActive, String matchNotes) {
        this.id = id;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.matchScore = matchScore;
        this.matchStatus = matchStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isActive = isActive;
        this.matchNotes = matchNotes;
    }
    
    // 创建新匹配构造函数
    public Match(int userOneId, int userTwoId, int matchScore) {
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.matchScore = matchScore;
        this.matchStatus = "pending";
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.updateTime = this.createTime;
        this.isActive = true;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(int userOneId) {
        this.userOneId = userOneId;
    }

    public int getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(int userTwoId) {
        this.userTwoId = userTwoId;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(int matchScore) {
        this.matchScore = matchScore;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }

    public String getMatchNotes() {
        return matchNotes;
    }

    public void setMatchNotes(String matchNotes) {
        this.matchNotes = matchNotes;
    }
    
    /**
     * 检查特定用户是否参与匹配
     * @param userId 要检查的用户ID
     * @return 如果用户是匹配的一方则返回true
     */
    public boolean involvesUser(int userId) {
        return (userOneId == userId || userTwoId == userId);
    }
    
    /**
     * 获取匹配中对方用户的ID
     * @param myUserId 当前用户ID
     * @return 对方用户ID
     */
    public int getOtherUserId(int myUserId) {
        if (myUserId == userOneId) {
            return userTwoId;
        } else if (myUserId == userTwoId) {
            return userOneId;
        }
        return -1; // 如果用户不在此匹配中
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", userOneId=" + userOneId +
                ", userTwoId=" + userTwoId +
                ", matchScore=" + matchScore +
                ", matchStatus='" + matchStatus + '\'' +
                ", createTime=" + createTime +
                ", isActive=" + isActive +
                '}';
    }
}