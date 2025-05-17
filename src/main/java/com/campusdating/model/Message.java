package com.campusdating.model;

import java.sql.Timestamp;

/**
 * 消息实体类
 * 用于存储用户之间的消息记录
 */
public class Message {
    private int id;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp sendTime;
    private boolean isRead;
    private boolean isDeleted;
    private String messageType; // text, image, etc.
    
    // 默认构造函数
    public Message() {
    }
    
    // 完整参数构造函数
    public Message(int id, int senderId, int receiverId, String content, 
                   Timestamp sendTime, boolean isRead, boolean isDeleted, String messageType) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sendTime = sendTime;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
        this.messageType = messageType;
    }
    
    // 发送新消息构造函数
    public Message(int senderId, int receiverId, String content, String messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sendTime = new Timestamp(System.currentTimeMillis());
        this.isRead = false;
        this.isDeleted = false;
        this.messageType = messageType;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", isRead=" + isRead +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}