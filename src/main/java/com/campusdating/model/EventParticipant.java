package com.campusdating.model;

import java.sql.Timestamp;

/**
 * 活动参与者实体类
 * 用于存储活动与参与者之间的多对多关系
 */
public class EventParticipant {
    private int id;
    private int eventId;
    private int userId;
    private String status; // registered, attended, cancelled
    private Timestamp registerTime;
    private String notes;
    
    // 默认构造函数
    public EventParticipant() {
    }
    
    // 完整参数构造函数
    public EventParticipant(int id, int eventId, int userId, String status, 
                           Timestamp registerTime, String notes) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.status = status;
        this.registerTime = registerTime;
        this.notes = notes;
    }
    
    // 基本构造函数
    public EventParticipant(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
        this.status = "registered";
        this.registerTime = new Timestamp(System.currentTimeMillis());
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * 取消参与
     */
    public void cancel() {
        this.status = "cancelled";
    }
    
    /**
     * 标记为已参加
     */
    public void markAttended() {
        this.status = "attended";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        EventParticipant that = (EventParticipant) o;
        
        if (eventId != that.eventId) return false;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        int result = eventId;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "EventParticipant{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", registerTime=" + registerTime +
                '}';
    }
}