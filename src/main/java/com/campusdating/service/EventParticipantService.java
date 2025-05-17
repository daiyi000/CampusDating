package com.campusdating.service;

import com.campusdating.dao.EventParticipantDao;
import com.campusdating.model.EventParticipant;
import com.campusdating.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * 活动参与者服务类
 * 提供活动参与者相关的业务逻辑
 */
public class EventParticipantService {
    
    private EventParticipantDao eventParticipantDao;
    
    /**
     * 构造函数
     */
    public EventParticipantService() {
        eventParticipantDao = new EventParticipantDao();
    }
    
    /**
     * 添加活动参与者
     * @param eventParticipant 活动参与者对象
     * @return 成功返回true，失败返回false
     */
    public boolean addEventParticipant(EventParticipant eventParticipant) {
        // 验证活动参与者
        if (eventParticipant == null || eventParticipant.getEventId() <= 0 || 
            eventParticipant.getUserId() <= 0) {
            return false;
        }
        
        // 检查活动参与者是否已存在
        EventParticipant existingParticipant = eventParticipantDao.findByEventIdAndUserId(
                eventParticipant.getEventId(), eventParticipant.getUserId());
        
        if (existingParticipant != null) {
            return false;
        }
        
        // 设置注册时间
        if (eventParticipant.getRegisterTime() == null) {
            eventParticipant.setRegisterTime(new Timestamp(System.currentTimeMillis()));
        }
        
        // 设置默认状态
        if (ValidationUtil.isEmpty(eventParticipant.getStatus())) {
            eventParticipant.setStatus("registered");
        }
        
        // 保存活动参与者
        return eventParticipantDao.save(eventParticipant);
    }
    
    /**
     * 根据ID获取活动参与者
     * @param id 活动参与者ID
     * @return 活动参与者对象，如果不存在返回null
     */
    public EventParticipant getEventParticipantById(int id) {
        return eventParticipantDao.findById(id);
    }
    
    /**
     * 根据活动ID和用户ID获取活动参与者
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 活动参与者对象，如果不存在返回null
     */
    public EventParticipant getEventParticipant(int eventId, int userId) {
        return eventParticipantDao.findByEventIdAndUserId(eventId, userId);
    }
    
    /**
     * 获取活动的所有参与者
     * @param eventId 活动ID
     * @return 活动参与者列表
     */
    public List<EventParticipant> getEventParticipants(int eventId) {
        return eventParticipantDao.findByEventId(eventId);
    }
    
    /**
     * 获取用户参与的所有活动的参与记录
     * @param userId 用户ID
     * @return 活动参与者列表
     */
    public List<EventParticipant> getUserParticipations(int userId) {
        return eventParticipantDao.findByUserId(userId);
    }
    
    /**
     * 更新活动参与者
     * @param eventParticipant 活动参与者对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateEventParticipant(EventParticipant eventParticipant) {
        // 验证活动参与者
        if (eventParticipant == null || eventParticipant.getId() <= 0) {
            return false;
        }
        
        return eventParticipantDao.update(eventParticipant);
    }
    
    /**
     * 删除活动参与者
     * @param id 活动参与者ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteEventParticipant(int id) {
        return eventParticipantDao.delete(id);
    }
    
    /**
     * 取消参与活动
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean cancelParticipation(int eventId, int userId) {
        // 验证参数
        if (eventId <= 0 || userId <= 0) {
            return false;
        }
        
        // 获取活动参与者
        EventParticipant participant = eventParticipantDao.findByEventIdAndUserId(eventId, userId);
        
        // 如果参与者不存在
        if (participant == null) {
            return false;
        }
        
        // 设置状态为取消
        participant.setStatus("cancelled");
        
        // 更新活动参与者
        return eventParticipantDao.update(participant);
    }
    
    /**
     * 标记为已参加
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean markAsAttended(int eventId, int userId) {
        // 验证参数
        if (eventId <= 0 || userId <= 0) {
            return false;
        }
        
        // 获取活动参与者
        EventParticipant participant = eventParticipantDao.findByEventIdAndUserId(eventId, userId);
        
        // 如果参与者不存在
        if (participant == null) {
            return false;
        }
        
        // 设置状态为已参加
        participant.setStatus("attended");
        
        // 更新活动参与者
        return eventParticipantDao.update(participant);
    }
    
    /**
     * 获取活动参与者数量
     * @param eventId 活动ID
     * @return 参与者数量
     */
    public int getParticipantCount(int eventId) {
        // 验证参数
        if (eventId <= 0) {
            return 0;
        }
        
        return eventParticipantDao.countByEventId(eventId);
    }
    
    /**
     * 获取用户参与活动数量
     * @param userId 用户ID
     * @return 参与活动数量
     */
    public int getParticipationCount(int userId) {
        // 验证参数
        if (userId <= 0) {
            return 0;
        }
        
        return eventParticipantDao.countByUserId(userId);
    }
    
    /**
     * 检查用户是否参与了活动
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 如果用户参与了活动返回true
     */
    public boolean isUserParticipated(int eventId, int userId) {
        // 验证参数
        if (eventId <= 0 || userId <= 0) {
            return false;
        }
        
        return eventParticipantDao.findByEventIdAndUserId(eventId, userId) != null;
    }
    
    /**
     * 获取活动的所有参与者用户ID
     * @param eventId 活动ID
     * @return 用户ID列表
     */
    public List<Integer> getParticipantUserIds(int eventId) {
        return eventParticipantDao.findUserIdsByEventId(eventId);
    }
    
    /**
     * 获取用户参与的所有活动ID
     * @param userId 用户ID
     * @return 活动ID列表
     */
    public List<Integer> getParticipatedEventIds(int userId) {
        return eventParticipantDao.findEventIdsByUserId(userId);
    }
}