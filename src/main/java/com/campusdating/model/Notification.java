package com.campusdating.model;

import java.sql.Timestamp;

/**
 * 通知实体类
 * 用于存储系统发送给用户的各类通知
 */
public class Notification {
    private int id;
    private int userId;
    private String notificationType; // match, message, event, system, etc.
    private String content;
    private String link;
    private boolean isRead;
    private Timestamp createTime;
    private int relatedId; // 关联ID (比如匹配ID, 消息ID等)
    private int senderId; // 发送者ID (如果适用)
    
    // 默认构造函数
    public Notification() {
    }
    
    // 完整参数构造函数
    public Notification(int id, int userId, String notificationType, String content, 
                        String link, boolean isRead, Timestamp createTime, 
                        int relatedId, int senderId) {
        this.id = id;
        this.userId = userId;
        this.notificationType = notificationType;
        this.content = content;
        this.link = link;
        this.isRead = isRead;
        this.createTime = createTime;
        this.relatedId = relatedId;
        this.senderId = senderId;
    }
    
    // 创建新通知构造函数
    public Notification(int userId, String notificationType, String content, 
                        String link, int relatedId, int senderId) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.content = content;
        this.link = link;
        this.isRead = false;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.relatedId = relatedId;
        this.senderId = senderId;
    }
    
    // 系统通知构造函数
    public Notification(int userId, String content) {
        this.userId = userId;
        this.notificationType = "system";
        this.content = content;
        this.isRead = false;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.relatedId = 0;
        this.senderId = 0;
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

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    /**
     * 构建匹配通知内容
     * @param matcherName 匹配者名称
     * @return 匹配通知
     */
    public static Notification buildMatchNotification(int userId, String matcherName, int matchId, int matcherUserId) {
        String content = "你与 " + matcherName + " 匹配成功！";
        String link = "match?id=" + matchId;
        return new Notification(userId, "match", content, link, matchId, matcherUserId);
    }
    
    /**
     * 构建消息通知内容
     * @param senderName 发送者名称
     * @return 消息通知
     */
    public static Notification buildMessageNotification(int userId, String senderName, int messageId, int senderUserId) {
        String content = "你收到来自 " + senderName + " 的新消息";
        String link = "messages?with=" + senderUserId;
        return new Notification(userId, "message", content, link, messageId, senderUserId);
    }
    
    /**
     * 构建活动通知内容
     * @param eventTitle 活动标题
     * @return 活动通知
     */
    public static Notification buildEventNotification(int userId, String eventTitle, int eventId) {
        String content = "有一个新活动：" + eventTitle;
        String link = "eventDetails?id=" + eventId;
        return new Notification(userId, "event", content, link, eventId, 0);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", notificationType='" + notificationType + '\'' +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                ", createTime=" + createTime +
                '}';
    }
}