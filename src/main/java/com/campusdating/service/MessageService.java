package com.campusdating.service;

import com.campusdating.dao.MessageDao;
import com.campusdating.model.Message;
import com.campusdating.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * 消息服务类
 * 提供消息相关的业务逻辑
 */
public class MessageService {
    
    private MessageDao messageDao;
    
    /**
     * 构造函数
     */
    public MessageService() {
        messageDao = new MessageDao();
    }
    
    /**
     * 发送消息
     * @param message 消息对象
     * @return 成功返回true，失败返回false
     */
    public boolean sendMessage(Message message) {
        // 验证消息
        if (message == null || message.getSenderId() <= 0 || message.getReceiverId() <= 0 || 
            ValidationUtil.isEmpty(message.getContent())) {
            return false;
        }
        
        // 设置发送时间
        if (message.getSendTime() == null) {
            message.setSendTime(new Timestamp(System.currentTimeMillis()));
        }
        
        // 设置消息状态
        message.setRead(false);
        message.setDeleted(false);
        
        // 设置默认消息类型
        if (ValidationUtil.isEmpty(message.getMessageType())) {
            message.setMessageType("text");
        }
        
        // 保存消息
        return messageDao.save(message);
    }
    
    /**
     * 根据ID获取消息
     * @param id 消息ID
     * @return 消息对象，如果不存在返回null
     */
    public Message getMessageById(int id) {
        return messageDao.findById(id);
    }
    
    /**
     * 获取两个用户之间的消息
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 消息列表
     */
    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        return messageDao.findMessagesBetweenUsers(userId1, userId2);
    }
    
    /**
     * 获取新消息（在指定消息ID之后的消息）
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @param lastMessageId 最后一条消息ID
     * @return 新消息列表
     */
    public List<Message> getNewMessagesBetweenUsers(int userId1, int userId2, int lastMessageId) {
        // 获取所有消息
        List<Message> allMessages = messageDao.findMessagesBetweenUsers(userId1, userId2);
        
        // 过滤出新消息
        allMessages.removeIf(message -> message.getId() <= lastMessageId);
        
        return allMessages;
    }
    
    /**
     * 获取用户的接收消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getReceivedMessages(int userId) {
        return messageDao.findReceivedMessages(userId);
    }
    
    /**
     * 获取用户的发送消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getSentMessages(int userId) {
        return messageDao.findSentMessages(userId);
    }
    
    /**
     * 获取用户的未读消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getUnreadMessages(int userId) {
        return messageDao.findUnreadMessages(userId);
    }
    
    /**
     * 获取未读消息数量
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @return 未读消息数量
     */
    public int getUnreadMessageCount(int senderId, int receiverId) {
        // 获取所有消息
        List<Message> messages = messageDao.findMessagesBetweenUsers(senderId, receiverId);
        
        // 统计未读消息数量
        int count = 0;
        for (Message message : messages) {
            if (message.getSenderId() == senderId && message.getReceiverId() == receiverId && !message.isRead()) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 将消息标记为已读
     * @param messageId 消息ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAsRead(int messageId) {
        return messageDao.markAsRead(messageId);
    }
    
    /**
     * 将用户所有未读消息标记为已读
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAllAsRead(int userId) {
        return messageDao.markAllAsRead(userId);
    }
    
    /**
     * 将指定发送者发送给接收者的所有消息标记为已读
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @return 成功返回true，失败返回false
     */
    public boolean markMessagesAsRead(int senderId, int receiverId) {
        // 获取所有消息
        List<Message> messages = messageDao.findMessagesBetweenUsers(senderId, receiverId);
        
        boolean allSuccess = true;
        for (Message message : messages) {
            if (message.getSenderId() == senderId && message.getReceiverId() == receiverId && !message.isRead()) {
                boolean success = messageDao.markAsRead(message.getId());
                if (!success) {
                    allSuccess = false;
                }
            }
        }
        
        return allSuccess;
    }
    
    /**
     * 删除消息
     * @param messageId 消息ID
     * @param userId 用户ID（验证用户是消息的发送者或接收者）
     * @return 成功返回true，失败返回false
     */
    public boolean deleteMessage(int messageId, int userId) {
        // 获取消息
        Message message = messageDao.findById(messageId);
        
        // 验证消息存在且用户是消息的发送者或接收者
        if (message == null || (message.getSenderId() != userId && message.getReceiverId() != userId)) {
            return false;
        }
        
        return messageDao.softDelete(messageId);
    }
    
    /**
     * 删除用户的所有消息
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteAllUserMessages(int userId) {
        // 获取用户的所有接收消息
        List<Message> receivedMessages = messageDao.findReceivedMessages(userId);
        
        // 获取用户的所有发送消息
        List<Message> sentMessages = messageDao.findSentMessages(userId);
        
        boolean allSuccess = true;
        
        // 删除接收消息
        for (Message message : receivedMessages) {
            boolean success = messageDao.softDelete(message.getId());
            if (!success) {
                allSuccess = false;
            }
        }
        
        // 删除发送消息
        for (Message message : sentMessages) {
            boolean success = messageDao.softDelete(message.getId());
            if (!success) {
                allSuccess = false;
            }
        }
        
        return allSuccess;
    }
    
    /**
     * 获取消息伙伴（与用户有消息往来的用户）
     * @param userId 用户ID
     * @return 用户ID列表
     */
    public List<Integer> getMessagePartners(int userId) {
        return messageDao.findMessagePartners(userId);
    }
    
    /**
     * 获取最后一条消息
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 最后一条消息，如果不存在返回null
     */
    public Message getLastMessageBetweenUsers(int userId1, int userId2) {
        return messageDao.findLastMessageBetweenUsers(userId1, userId2);
    }
}