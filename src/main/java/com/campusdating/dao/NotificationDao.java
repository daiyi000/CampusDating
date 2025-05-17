package com.campusdating.dao;

import com.campusdating.model.Notification;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知数据访问对象类
 * 负责Notification对象的CRUD操作
 */
public class NotificationDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public NotificationDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存通知到数据库
     * @param notification 通知对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Notification notification) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO notifications(user_id, notification_type, content, link, is_read, create_time, related_id, sender_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getNotificationType());
            ps.setString(3, notification.getContent());
            ps.setString(4, notification.getLink());
            ps.setBoolean(5, notification.isRead());
            ps.setTimestamp(6, notification.getCreateTime());
            ps.setInt(7, notification.getRelatedId());
            ps.setInt(8, notification.getSenderId());
            
            flag = ps.executeUpdate() > 0;
            
            // 获取生成的ID
            if (flag) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    notification.setId(rs.getInt(1));
                }
                rs.close();
            }
            
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 更新通知信息
     * @param notification 通知对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Notification notification) {
        boolean flag = false;
        try {
            String sql = "UPDATE notifications SET user_id=?, notification_type=?, content=?, link=?, is_read=?, create_time=?, related_id=?, sender_id=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getNotificationType());
            ps.setString(3, notification.getContent());
            ps.setString(4, notification.getLink());
            ps.setBoolean(5, notification.isRead());
            ps.setTimestamp(6, notification.getCreateTime());
            ps.setInt(7, notification.getRelatedId());
            ps.setInt(8, notification.getSenderId());
            ps.setInt(9, notification.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取通知
     * @param id 通知ID
     * @return 通知对象，如果不存在返回null
     */
    public Notification findById(int id) {
        Notification notification = null;
        try {
            String sql = "SELECT * FROM notifications WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                notification = mapResultSetToNotification(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notification;
    }
    
    /**
     * 获取用户的所有通知
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> findByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql = "SELECT * FROM notifications WHERE user_id=? ORDER BY create_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    /**
     * 获取用户的所有未读通知
     * @param userId 用户ID
     * @return 通知列表
     */
    public List<Notification> findUnreadByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql = "SELECT * FROM notifications WHERE user_id=? AND is_read=false ORDER BY create_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    /**
     * 获取用户指定类型的通知
     * @param userId 用户ID
     * @param type 通知类型
     * @return 通知列表
     */
    public List<Notification> findByType(int userId, String type) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql = "SELECT * FROM notifications WHERE user_id=? AND notification_type=? ORDER BY create_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    /**
     * 获取用户的未读通知数量
     * @param userId 用户ID
     * @return 未读通知数量
     */
    public int countUnreadNotifications(int userId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM notifications WHERE user_id=? AND is_read=false";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    /**
     * 将通知标记为已读
     * @param notificationId 通知ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAsRead(int notificationId) {
        boolean flag = false;
        try {
            String sql = "UPDATE notifications SET is_read=true WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, notificationId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 将用户所有通知标记为已读
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAllAsRead(int userId) {
        boolean flag = false;
        try {
            String sql = "UPDATE notifications SET is_read=true WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 删除通知
     * @param id 通知ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM notifications WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 删除用户的所有通知
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteAllByUserId(int userId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM notifications WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 创建匹配通知
     * @param userId 接收通知的用户ID
     * @param matcherName 匹配者名称
     * @param matchId 匹配ID
     * @param matcherUserId 匹配者用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean createMatchNotification(int userId, String matcherName, int matchId, int matcherUserId) {
        Notification notification = Notification.buildMatchNotification(userId, matcherName, matchId, matcherUserId);
        return save(notification);
    }
    
    /**
     * 创建消息通知
     * @param userId 接收通知的用户ID
     * @param senderName 发送者名称
     * @param messageId 消息ID
     * @param senderUserId 发送者用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean createMessageNotification(int userId, String senderName, int messageId, int senderUserId) {
        Notification notification = Notification.buildMessageNotification(userId, senderName, messageId, senderUserId);
        return save(notification);
    }
    
    /**
     * 创建活动通知
     * @param userId 接收通知的用户ID
     * @param eventTitle 活动标题
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean createEventNotification(int userId, String eventTitle, int eventId) {
        Notification notification = Notification.buildEventNotification(userId, eventTitle, eventId);
        return save(notification);
    }
    
    /**
     * 创建系统通知
     * @param userId 接收通知的用户ID
     * @param content 通知内容
     * @return 成功返回true，失败返回false
     */
    public boolean createSystemNotification(int userId, String content) {
        Notification notification = new Notification(userId, content);
        return save(notification);
    }
    
    /**
     * 批量创建通知
     * @param userIds 接收通知的用户ID列表
     * @param notificationType 通知类型
     * @param content 通知内容
     * @param link 通知链接
     * @param relatedId 关联ID
     * @param senderId 发送者ID
     * @return 成功创建的通知数量
     */
    public int createBatchNotifications(List<Integer> userIds, String notificationType, String content, String link, int relatedId, int senderId) {
        int successCount = 0;
        
        for (Integer userId : userIds) {
            Notification notification = new Notification(userId, notificationType, content, link, relatedId, senderId);
            if (save(notification)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 将ResultSet映射为Notification对象
     * @param rs ResultSet
     * @return Notification对象
     * @throws SQLException SQLException
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setContent(rs.getString("content"));
        notification.setLink(rs.getString("link"));
        notification.setRead(rs.getBoolean("is_read"));
        notification.setCreateTime(rs.getTimestamp("create_time"));
        notification.setRelatedId(rs.getInt("related_id"));
        notification.setSenderId(rs.getInt("sender_id"));
        return notification;
    }
}