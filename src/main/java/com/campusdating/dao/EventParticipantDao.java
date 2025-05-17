package com.campusdating.dao;

import com.campusdating.model.EventParticipant;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动参与者数据访问对象类
 * 负责EventParticipant对象的CRUD操作
 */
public class EventParticipantDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public EventParticipantDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存活动参与者到数据库
     * @param eventParticipant 活动参与者对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(EventParticipant eventParticipant) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO event_participants(event_id, user_id, status, register_time, notes) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, eventParticipant.getEventId());
            ps.setInt(2, eventParticipant.getUserId());
            ps.setString(3, eventParticipant.getStatus());
            ps.setTimestamp(4, eventParticipant.getRegisterTime());
            ps.setString(5, eventParticipant.getNotes());
            
            flag = ps.executeUpdate() > 0;
            
            // 获取生成的ID
            if (flag) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    eventParticipant.setId(rs.getInt(1));
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
     * 更新活动参与者信息
     * @param eventParticipant 活动参与者对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(EventParticipant eventParticipant) {
        boolean flag = false;
        try {
            String sql = "UPDATE event_participants SET event_id=?, user_id=?, status=?, register_time=?, notes=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventParticipant.getEventId());
            ps.setInt(2, eventParticipant.getUserId());
            ps.setString(3, eventParticipant.getStatus());
            ps.setTimestamp(4, eventParticipant.getRegisterTime());
            ps.setString(5, eventParticipant.getNotes());
            ps.setInt(6, eventParticipant.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取活动参与者
     * @param id 活动参与者ID
     * @return 活动参与者对象，如果不存在返回null
     */
    public EventParticipant findById(int id) {
        EventParticipant eventParticipant = null;
        try {
            String sql = "SELECT * FROM event_participants WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                eventParticipant = mapResultSetToEventParticipant(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventParticipant;
    }
    
    /**
     * 获取活动的所有参与者
     * @param eventId 活动ID
     * @return 活动参与者列表
     */
    public List<EventParticipant> findByEventId(int eventId) {
        List<EventParticipant> eventParticipants = new ArrayList<>();
        try {
            String sql = "SELECT * FROM event_participants WHERE event_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EventParticipant eventParticipant = mapResultSetToEventParticipant(rs);
                eventParticipants.add(eventParticipant);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventParticipants;
    }
    
    /**
     * 获取用户参与的所有活动的参与记录
     * @param userId 用户ID
     * @return 活动参与者列表
     */
    public List<EventParticipant> findByUserId(int userId) {
        List<EventParticipant> eventParticipants = new ArrayList<>();
        try {
            String sql = "SELECT * FROM event_participants WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EventParticipant eventParticipant = mapResultSetToEventParticipant(rs);
                eventParticipants.add(eventParticipant);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventParticipants;
    }
    
    /**
     * 根据活动ID和用户ID获取活动参与者
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 活动参与者对象，如果不存在返回null
     */
    public EventParticipant findByEventIdAndUserId(int eventId, int userId) {
        EventParticipant eventParticipant = null;
        try {
            String sql = "SELECT * FROM event_participants WHERE event_id=? AND user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                eventParticipant = mapResultSetToEventParticipant(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventParticipant;
    }
    
    /**
     * 获取活动的所有参与者用户ID
     * @param eventId 活动ID
     * @return 用户ID列表
     */
    public List<Integer> findUserIdsByEventId(int eventId) {
        List<Integer> userIds = new ArrayList<>();
        try {
            String sql = "SELECT user_id FROM event_participants WHERE event_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }
    
    /**
     * 获取用户参与的所有活动ID
     * @param userId 用户ID
     * @return 活动ID列表
     */
    public List<Integer> findEventIdsByUserId(int userId) {
        List<Integer> eventIds = new ArrayList<>();
        try {
            String sql = "SELECT event_id FROM event_participants WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                eventIds.add(rs.getInt("event_id"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventIds;
    }
    
    /**
     * 删除活动参与者
     * @param id 活动参与者ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM event_participants WHERE id=?";
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
     * 删除活动的所有参与者
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteByEventId(int eventId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM event_participants WHERE event_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 删除用户的所有活动参与记录
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteByUserId(int userId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM event_participants WHERE user_id=?";
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
     * 获取活动参与者数量
     * @param eventId 活动ID
     * @return 参与者数量
     */
    public int countByEventId(int eventId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM event_participants WHERE event_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
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
     * 获取用户参与活动数量
     * @param userId 用户ID
     * @return 参与活动数量
     */
    public int countByUserId(int userId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM event_participants WHERE user_id=?";
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
     * 将ResultSet映射为EventParticipant对象
     * @param rs ResultSet
     * @return EventParticipant对象
     * @throws SQLException SQLException
     */
    private EventParticipant mapResultSetToEventParticipant(ResultSet rs) throws SQLException {
        EventParticipant eventParticipant = new EventParticipant();
        eventParticipant.setId(rs.getInt("id"));
        eventParticipant.setEventId(rs.getInt("event_id"));
        eventParticipant.setUserId(rs.getInt("user_id"));
        eventParticipant.setStatus(rs.getString("status"));
        eventParticipant.setRegisterTime(rs.getTimestamp("register_time"));
        eventParticipant.setNotes(rs.getString("notes"));
        return eventParticipant;
    }
}