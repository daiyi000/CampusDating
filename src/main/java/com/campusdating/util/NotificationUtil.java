package com.campusdating.util;

import com.campusdating.dao.NotificationDao;
import com.campusdating.model.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知工具类
 * 用于处理系统通知
 */
public class NotificationUtil {

    private static NotificationDao notificationDao = new NotificationDao();
    
    /**
     * 发送匹配通知
     * @param userId 接收通知的用户ID
     * @param matcherName 匹配者名称
     * @param matchId 匹配ID
     * @param matcherUserId 匹配者用户ID
     * @return 成功返回true，失败返回false
     */
    public static boolean sendMatchNotification(int userId, String matcherName, int matchId, int matcherUserId) {
        return notificationDao.createMatchNotification(userId, matcherName, matchId, matcherUserId);
    }
    
    /**
     * 发送消息通知
     * @param userId 接收通知的用户ID
     * @param senderName 发送者名称
     * @param messageId 消息ID
     * @param senderUserId 发送者用户ID
     * @return 成功返回true，失败返回false
     */
    public static boolean sendMessageNotification(int userId, String senderName, int messageId, int senderUserId) {
        return notificationDao.createMessageNotification(userId, senderName, messageId, senderUserId);
    }
    
    /**
     * 发送活动通知
     * @param userId 接收通知的用户ID
     * @param eventTitle 活动标题
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public static boolean sendEventNotification(int userId, String eventTitle, int eventId) {
        return notificationDao.createEventNotification(userId, eventTitle, eventId);
    }
    
    /**
     * 发送系统通知
     * @param userId 接收通知的用户ID
     * @param content 通知内容
     * @return 成功返回true，失败返回false
     */
    public static boolean sendSystemNotification(int userId, String content) {
        return notificationDao.createSystemNotification(userId, content);
    }
    
    /**
     * 批量发送系统通知
     * @param userIds 接收通知的用户ID列表
     * @param content 通知内容
     * @return 成功发送的通知数量
     */
    public static int sendBatchSystemNotifications(List<Integer> userIds, String content) {
        int successCount = 0;
        
        for (Integer userId : userIds) {
            if (sendSystemNotification(userId, content)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 批量发送活动通知
     * @param userIds 接收通知的用户ID列表
     * @param eventTitle 活动标题
     * @param eventId 活动ID
     * @return 成功发送的通知数量
     */
    public static int sendBatchEventNotifications(List<Integer> userIds, String eventTitle, int eventId) {
        int successCount = 0;
        
        for (Integer userId : userIds) {
            if (sendEventNotification(userId, eventTitle, eventId)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 将通知标记为已读
     * @param notificationId 通知ID
     * @return 成功返回true，失败返回false
     */
    public static boolean markAsRead(int notificationId) {
        return notificationDao.markAsRead(notificationId);
    }
    
    /**
     * 将用户所有通知标记为已读
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public static boolean markAllAsRead(int userId) {
        return notificationDao.markAllAsRead(userId);
    }
    
    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读通知数量
     */
    public static int getUnreadCount(int userId) {
        return notificationDao.countUnreadNotifications(userId);
    }
    
    /**
     * 获取用户的所有通知
     * @param userId 用户ID
     * @return 通知列表
     */
    public static List<Notification> getUserNotifications(int userId) {
        return notificationDao.findByUserId(userId);
    }
    
    /**
     * 获取用户的所有未读通知
     * @param userId 用户ID
     * @return 未读通知列表
     */
    public static List<Notification> getUnreadNotifications(int userId) {
        return notificationDao.findUnreadByUserId(userId);
    }
    
    /**
     * 删除通知
     * @param notificationId 通知ID
     * @return 成功返回true，失败返回false
     */
    public static boolean deleteNotification(int notificationId) {
        return notificationDao.delete(notificationId);
    }
    
    /**
     * 删除用户的所有通知
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public static boolean deleteAllUserNotifications(int userId) {
        return notificationDao.deleteAllByUserId(userId);
    }
    
    /**
     * 创建匹配成功通知
     * @param user1Id 用户1ID
     * @param user2Id 用户2ID
     * @param user1Name 用户1名称
     * @param user2Name 用户2名称
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public static boolean createMatchSuccessNotifications(int user1Id, int user2Id, String user1Name, String user2Name, int matchId) {
        boolean success1 = sendMatchNotification(user1Id, user2Name, matchId, user2Id);
        boolean success2 = sendMatchNotification(user2Id, user1Name, matchId, user1Id);
        return success1 && success2;
    }
    
    /**
     * 判断通知是否已读
     * @param notification 通知对象
     * @return 如果已读返回true
     */
    public static boolean isRead(Notification notification) {
        return notification != null && notification.isRead();
    }
    
    /**
     * 获取通知类型的显示名称
     * @param notificationType 通知类型
     * @return 显示名称
     */
    public static String getNotificationTypeDisplayName(String notificationType) {
        if (notificationType == null) {
            return "未知";
        }
        
        switch (notificationType) {
            case "match":
                return "匹配";
            case "message":
                return "消息";
            case "event":
                return "活动";
            case "system":
                return "系统";
            default:
                return notificationType;
        }
    }
    
    /**
     * 获取通知图标CSS类
     * @param notificationType 通知类型
     * @return 图标CSS类
     */
    public static String getNotificationIconClass(String notificationType) {
        if (notificationType == null) {
            return "fa-bell";
        }
        
        switch (notificationType) {
            case "match":
                return "fa-heart";
            case "message":
                return "fa-envelope";
            case "event":
                return "fa-calendar";
            case "system":
                return "fa-info-circle";
            default:
                return "fa-bell";
        }
    }
}