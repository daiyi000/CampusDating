package com.campusdating.dao;

import com.campusdating.model.Event;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动数据访问对象类
 * 负责Event对象的CRUD操作
 */
public class EventDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public EventDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存活动到数据库
     * @param event 活动对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Event event) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO events(creator_id, title, description, location, event_date, start_time, end_time, event_type, max_participants, current_participants, image_url, is_public, create_time, is_active, latitude, longitude) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, event.getCreatorId());
            ps.setString(2, event.getTitle());
            ps.setString(3, event.getDescription());
            ps.setString(4, event.getLocation());
            ps.setDate(5, event.getEventDate());
            ps.setTimestamp(6, event.getStartTime());
            ps.setTimestamp(7, event.getEndTime());
            ps.setString(8, event.getEventType());
            ps.setInt(9, event.getMaxParticipants());
            ps.setInt(10, event.getCurrentParticipants());
            ps.setString(11, event.getImageUrl());
            ps.setBoolean(12, event.isPublic());
            ps.setTimestamp(13, event.getCreateTime());
            ps.setBoolean(14, event.isActive());
            ps.setDouble(15, event.getLatitude());
            ps.setDouble(16, event.getLongitude());
            
            flag = ps.executeUpdate() > 0;
            
            // 获取生成的ID
            if (flag) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    event.setId(rs.getInt(1));
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
     * 更新活动信息
     * @param event 活动对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Event event) {
        boolean flag = false;
        try {
            String sql = "UPDATE events SET title=?, description=?, location=?, event_date=?, start_time=?, end_time=?, event_type=?, max_participants=?, current_participants=?, image_url=?, is_public=?, is_active=?, latitude=?, longitude=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getLocation());
            ps.setDate(4, event.getEventDate());
            ps.setTimestamp(5, event.getStartTime());
            ps.setTimestamp(6, event.getEndTime());
            ps.setString(7, event.getEventType());
            ps.setInt(8, event.getMaxParticipants());
            ps.setInt(9, event.getCurrentParticipants());
            ps.setString(10, event.getImageUrl());
            ps.setBoolean(11, event.isPublic());
            ps.setBoolean(12, event.isActive());
            ps.setDouble(13, event.getLatitude());
            ps.setDouble(14, event.getLongitude());
            ps.setInt(15, event.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取活动
     * @param id 活动ID
     * @return 活动对象，如果不存在返回null
     */
    public Event findById(int id) {
        Event event = null;
        try {
            String sql = "SELECT * FROM events WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                event = mapResultSetToEvent(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }
    
    /**
     * 获取所有活动
     * @return 活动列表
     */
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE is_active=true ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 获取指定类型的活动
     * @param eventType 活动类型
     * @return 活动列表
     */
    public List<Event> findByType(String eventType) {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE event_type=? AND is_active=true ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, eventType);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 获取用户创建的活动
     * @param creatorId 创建者ID
     * @return 活动列表
     */
    public List<Event> findByCreator(int creatorId) {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE creator_id=? ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, creatorId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 获取即将到来的活动
     * @return 活动列表
     */
    public List<Event> findUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE event_date >= CURRENT_DATE AND is_active=true ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 获取今天的活动
     * @return 活动列表
     */
    public List<Event> findTodayEvents() {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE event_date = CURRENT_DATE AND is_active=true ORDER BY start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 查找公开活动
     * @return 活动列表
     */
    public List<Event> findPublicEvents() {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE is_public=true AND is_active=true ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 增加活动参与人数
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean incrementParticipantCount(int eventId) {
        boolean flag = false;
        try {
            String sql = "UPDATE events SET current_participants = current_participants + 1 WHERE id=? AND current_participants < max_participants";
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
     * 减少活动参与人数
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean decrementParticipantCount(int eventId) {
        boolean flag = false;
        try {
            String sql = "UPDATE events SET current_participants = current_participants - 1 WHERE id=? AND current_participants > 0";
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
     * 设置活动状态为非活动
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean deactivateEvent(int eventId) {
        boolean flag = false;
        try {
            String sql = "UPDATE events SET is_active=false WHERE id=?";
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
     * 删除活动
     * @param id 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM events WHERE id=?";
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
     * 搜索活动
     * @param keyword 关键词
     * @return 活动列表
     */
    public List<Event> searchEvents(String keyword) {
        List<Event> events = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE (title LIKE ? OR description LIKE ? OR location LIKE ?) AND is_active=true ORDER BY event_date ASC, start_time ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Event event = mapResultSetToEvent(rs);
                events.add(event);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 检查活动是否已满
     * @param eventId 活动ID
     * @return 如果活动已满则返回true
     */
    public boolean isEventFull(int eventId) {
        boolean isFull = false;
        try {
            String sql = "SELECT current_participants, max_participants FROM events WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int current = rs.getInt("current_participants");
                int max = rs.getInt("max_participants");
                isFull = current >= max;
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isFull;
    }
    
    /**
     * 将ResultSet映射为Event对象
     * @param rs ResultSet
     * @return Event对象
     * @throws SQLException SQLException
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setId(rs.getInt("id"));
        event.setCreatorId(rs.getInt("creator_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setLocation(rs.getString("location"));
        event.setEventDate(rs.getDate("event_date"));
        event.setStartTime(rs.getTimestamp("start_time"));
        event.setEndTime(rs.getTimestamp("end_time"));
        event.setEventType(rs.getString("event_type"));
        event.setMaxParticipants(rs.getInt("max_participants"));
        event.setCurrentParticipants(rs.getInt("current_participants"));
        event.setImageUrl(rs.getString("image_url"));
        event.setPublic(rs.getBoolean("is_public"));
        event.setCreateTime(rs.getTimestamp("create_time"));
        event.setActive(rs.getBoolean("is_active"));
        event.setLatitude(rs.getDouble("latitude"));
        event.setLongitude(rs.getDouble("longitude"));
        return event;
    }
}