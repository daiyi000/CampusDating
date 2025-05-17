package com.campusdating.dao;

import com.campusdating.model.Message;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息数据访问对象类
 * 负责Message对象的CRUD操作
 */
public class MessageDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public MessageDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存消息到数据库
     * @param message 消息对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Message message) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO messages(sender_id, receiver_id, content, send_time, is_read, is_deleted, message_type) VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, message.getSendTime());
            ps.setBoolean(5, message.isRead());
            ps.setBoolean(6, message.isDeleted());
            ps.setString(7, message.getMessageType());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 更新消息信息
     * @param message 消息对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Message message) {
        boolean flag = false;
        try {
            String sql = "UPDATE messages SET sender_id=?, receiver_id=?, content=?, send_time=?, is_read=?, is_deleted=?, message_type=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, message.getSendTime());
            ps.setBoolean(5, message.isRead());
            ps.setBoolean(6, message.isDeleted());
            ps.setString(7, message.getMessageType());
            ps.setInt(8, message.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取消息
     * @param id 消息ID
     * @return 消息对象，如果不存在返回null
     */
    public Message findById(int id) {
        Message message = null;
        try {
            String sql = "SELECT * FROM messages WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                message = mapResultSetToMessage(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
    
    /**
     * 获取两个用户之间的所有消息
     * @param userId1 用户1的ID
     * @param userId2 用户2的ID
     * @return 消息列表
     */
    public List<Message> findMessagesBetweenUsers(int userId1, int userId2) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM messages WHERE (sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?) ORDER BY send_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = mapResultSetToMessage(rs);
                messages.add(message);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    /**
     * 获取用户的所有收到的消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> findReceivedMessages(int userId) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM messages WHERE receiver_id=? AND is_deleted=false ORDER BY send_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = mapResultSetToMessage(rs);
                messages.add(message);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    /**
     * 获取用户的所有发送的消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> findSentMessages(int userId) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM messages WHERE sender_id=? AND is_deleted=false ORDER BY send_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = mapResultSetToMessage(rs);
                messages.add(message);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    /**
     * 获取用户的所有未读消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> findUnreadMessages(int userId) {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM messages WHERE receiver_id=? AND is_read=false AND is_deleted=false ORDER BY send_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Message message = mapResultSetToMessage(rs);
                messages.add(message);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    /**
     * 获取用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public int countUnreadMessages(int userId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id=? AND is_read=false AND is_deleted=false";
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
     * 将消息标记为已读
     * @param messageId 消息ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAsRead(int messageId) {
        boolean flag = false;
        try {
            String sql = "UPDATE messages SET is_read=true WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 将用户所有未读消息标记为已读
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAllAsRead(int userId) {
        boolean flag = false;
        try {
            String sql = "UPDATE messages SET is_read=true WHERE receiver_id=? AND is_read=false";
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
     * 软删除消息（将is_deleted设为true）
     * @param messageId 消息ID
     * @return 成功返回true，失败返回false
     */
    public boolean softDelete(int messageId) {
        boolean flag = false;
        try {
            String sql = "UPDATE messages SET is_deleted=true WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 硬删除消息（从数据库中删除）
     * @param messageId 消息ID
     * @return 成功返回true，失败返回false
     */
    public boolean hardDelete(int messageId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM messages WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 获取与用户有消息往来的用户列表
     * @param userId 用户ID
     * @return 用户ID列表
     */
    public List<Integer> findMessagePartners(int userId) {
        List<Integer> partnerIds = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT sender_id FROM messages WHERE receiver_id=? AND is_deleted=false " +
                        "UNION " +
                        "SELECT DISTINCT receiver_id FROM messages WHERE sender_id=? AND is_deleted=false";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                partnerIds.add(rs.getInt(1));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partnerIds;
    }
    
    /**
     * 获取用户最后一条消息
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 最后一条消息
     */
    public Message findLastMessageBetweenUsers(int userId1, int userId2) {
        Message message = null;
        try {
            String sql = "SELECT * FROM messages WHERE ((sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?)) AND is_deleted=false ORDER BY send_time DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                message = mapResultSetToMessage(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }
    
    /**
     * 将ResultSet映射为Message对象
     * @param rs ResultSet
     * @return Message对象
     * @throws SQLException SQLException
     */
    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setSenderId(rs.getInt("sender_id"));
        message.setReceiverId(rs.getInt("receiver_id"));
        message.setContent(rs.getString("content"));
        message.setSendTime(rs.getTimestamp("send_time"));
        message.setRead(rs.getBoolean("is_read"));
        message.setDeleted(rs.getBoolean("is_deleted"));
        message.setMessageType(rs.getString("message_type"));
        return message;
    }
}