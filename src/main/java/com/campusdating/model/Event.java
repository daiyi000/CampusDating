package com.campusdating.model;

import java.sql.Timestamp;
import java.sql.Date;

/**
 * 活动实体类
 * 用于存储校园约会活动信息
 */
public class Event {
    private int id;
    private int creatorId;
    private String title;
    private String description;
    private String location;
    private Date eventDate;
    private Timestamp startTime;
    private Timestamp endTime;
    private String eventType; // social, academic, sports, etc.
    private int maxParticipants;
    private int currentParticipants;
    private String imageUrl;
    private boolean isPublic;
    private Timestamp createTime;
    private boolean isActive;
    private double latitude;
    private double longitude;
    
    // 默认构造函数
    public Event() {
    }
    
    // 完整参数构造函数
    public Event(int id, int creatorId, String title, String description, 
                 String location, Date eventDate, Timestamp startTime, Timestamp endTime, 
                 String eventType, int maxParticipants, int currentParticipants, 
                 String imageUrl, boolean isPublic, Timestamp createTime, 
                 boolean isActive, double latitude, double longitude) {
        this.id = id;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.imageUrl = imageUrl;
        this.isPublic = isPublic;
        this.createTime = createTime;
        this.isActive = isActive;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // 创建新活动构造函数
    public Event(int creatorId, String title, String description, String location, 
                 Date eventDate, Timestamp startTime, Timestamp endTime, 
                 String eventType, int maxParticipants) {
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 1; // 创建者自动参加
        this.isPublic = true;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.isActive = true;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    /**
     * 检查活动是否已满
     * @return 如果活动已达到最大参与人数则返回true
     */
    public boolean isFull() {
        return currentParticipants >= maxParticipants;
    }
    
    /**
     * 添加参与者
     * @return 如果成功添加参与者则返回true，如果已满则返回false
     */
    public boolean addParticipant() {
        if (currentParticipants < maxParticipants) {
            currentParticipants++;
            return true;
        }
        return false;
    }
    
    /**
     * 移除参与者
     * @return 如果成功移除参与者则返回true
     */
    public boolean removeParticipant() {
        if (currentParticipants > 0) {
            currentParticipants--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", eventDate=" + eventDate +
                ", eventType='" + eventType + '\'' +
                ", currentParticipants=" + currentParticipants + "/" + maxParticipants +
                ", isActive=" + isActive +
                '}';
    }
}