package com.campusdating.service;

import com.campusdating.dao.EventDao;
import com.campusdating.dao.EventParticipantDao;
import com.campusdating.dao.UserDao;
import com.campusdating.model.Event;
import com.campusdating.model.EventParticipant;
import com.campusdating.model.User;
import com.campusdating.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动服务类
 * 提供活动相关的业务逻辑
 */
public class EventService {
    
    private EventDao eventDao;
    private EventParticipantDao eventParticipantDao;
    private UserDao userDao;
    
    /**
     * 构造函数
     */
    public EventService() {
        eventDao = new EventDao();
        eventParticipantDao = new EventParticipantDao();
        userDao = new UserDao();
    }
    
    /**
     * 创建活动
     * @param event 活动对象
     * @return 成功返回true，失败返回false
     */
    public boolean createEvent(Event event) {
        // 验证活动
        if (event == null || event.getCreatorId() <= 0 || ValidationUtil.isEmpty(event.getTitle()) || 
            ValidationUtil.isEmpty(event.getLocation()) || event.getEventDate() == null || 
            event.getStartTime() == null || event.getEndTime() == null || 
            ValidationUtil.isEmpty(event.getEventType()) || event.getMaxParticipants() <= 0) {
            return false;
        }
        
        // 检查创建者是否存在
        User creator = userDao.findById(event.getCreatorId());
        if (creator == null) {
            return false;
        }
        
        // 设置创建时间
        if (event.getCreateTime() == null) {
            event.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        
        // 设置活动状态为活动
        event.setActive(true);
        
        // 保存活动
        boolean success = eventDao.save(event);
        
        // 如果保存成功且活动ID大于0，自动将创建者添加为参与者
        if (success && event.getId() > 0) {
            joinEvent(event.getId(), event.getCreatorId());
        }
        
        return success;
    }
    
    /**
     * 根据ID获取活动
     * @param id 活动ID
     * @return 活动对象，如果不存在返回null
     */
    public Event getEventById(int id) {
        return eventDao.findById(id);
    }
    
    /**
     * 获取所有活动
     * @return 活动列表
     */
    public List<Event> getAllEvents() {
        return eventDao.findAll();
    }
    
    /**
     * 获取指定类型的活动
     * @param eventType 活动类型
     * @return 活动列表
     */
    public List<Event> getEventsByType(String eventType) {
        return eventDao.findByType(eventType);
    }
    
    /**
     * 获取用户创建的活动
     * @param creatorId 创建者ID
     * @return 活动列表
     */
    public List<Event> getEventsByCreator(int creatorId) {
        return eventDao.findByCreator(creatorId);
    }
    
    /**
     * 获取即将到来的活动
     * @return 活动列表
     */
    public List<Event> getUpcomingEvents() {
        return eventDao.findUpcomingEvents();
    }
    
    /**
     * 获取今天的活动
     * @return 活动列表
     */
    public List<Event> getTodayEvents() {
        return eventDao.findTodayEvents();
    }
    
    /**
     * 获取公开活动
     * @return 活动列表
     */
    public List<Event> getPublicEvents() {
        return eventDao.findPublicEvents();
    }
    
    /**
     * 更新活动
     * @param event 活动对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateEvent(Event event) {
        // 验证活动
        if (event == null || event.getId() <= 0) {
            return false;
        }
        
        // 获取现有活动
        Event existingEvent = eventDao.findById(event.getId());
        
        // 如果活动不存在
        if (existingEvent == null) {
            return false;
        }
        
        // 更新活动
        return eventDao.update(event);
    }
    
    /**
     * 删除活动
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteEvent(int eventId) {
        // 获取活动
        Event event = eventDao.findById(eventId);
        
        // 如果活动不存在
        if (event == null) {
            return false;
        }
        
        // 先删除活动参与者关系
        eventParticipantDao.deleteByEventId(eventId);
        
        // 再删除活动
        return eventDao.delete(eventId);
    }
    
    /**
     * 取消活动
     * @param eventId 活动ID
     * @return 成功返回true，失败返回false
     */
    public boolean cancelEvent(int eventId) {
        // 将活动状态设为非活动
        return eventDao.deactivateEvent(eventId);
    }
    
    /**
     * 参与活动
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean joinEvent(int eventId, int userId) {
        // 验证参数
        if (eventId <= 0 || userId <= 0) {
            return false;
        }
        
        // 获取活动
        Event event = eventDao.findById(eventId);
        
        // 如果活动不存在或者已满
        if (event == null || event.isFull() || !event.isActive()) {
            return false;
        }
        
        // 检查用户是否已参与
        if (isUserParticipated(eventId, userId)) {
            return false;
        }
        
        // 创建活动参与者对象
        EventParticipant participant = new EventParticipant(eventId, userId);
        
        // 保存活动参与者
        boolean success = eventParticipantDao.save(participant);
        
        // 如果保存成功，增加活动参与人数
        if (success) {
            eventDao.incrementParticipantCount(eventId);
        }
        
        return success;
    }
    
    /**
     * 取消参与活动
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean leaveEvent(int eventId, int userId) {
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
        
        // 删除活动参与者
        boolean success = eventParticipantDao.delete(participant.getId());
        
        // 如果删除成功，减少活动参与人数
        if (success) {
            eventDao.decrementParticipantCount(eventId);
        }
        
        return success;
    }
    
    /**
     * 检查用户是否已参与活动
     * @param eventId 活动ID
     * @param userId 用户ID
     * @return 如果用户已参与活动返回true
     */
    public boolean isUserParticipated(int eventId, int userId) {
        // 验证参数
        if (eventId <= 0 || userId <= 0) {
            return false;
        }
        
        return eventParticipantDao.findByEventIdAndUserId(eventId, userId) != null;
    }
    
    /**
     * 获取活动参与者
     * @param eventId 活动ID
     * @return 用户列表
     */
    public List<User> getEventParticipants(int eventId) {
        // 验证参数
        if (eventId <= 0) {
            return new ArrayList<>();
        }
        
        // 获取参与者ID列表
        List<Integer> participantIds = eventParticipantDao.findUserIdsByEventId(eventId);
        
        // 获取参与者用户对象列表
        List<User> participants = new ArrayList<>();
        for (Integer userId : participantIds) {
            User user = userDao.findById(userId);
            if (user != null) {
                participants.add(user);
            }
        }
        
        return participants;
    }
    
    /**
     * 获取用户参与的活动
     * @param userId 用户ID
     * @return 活动列表
     */
    public List<Event> getEventsByParticipant(int userId) {
        // 验证参数
        if (userId <= 0) {
            return new ArrayList<>();
        }
        
        // 获取用户参与的活动ID列表
        List<Integer> eventIds = eventParticipantDao.findEventIdsByUserId(userId);
        
        // 获取活动对象列表
        List<Event> events = new ArrayList<>();
        for (Integer eventId : eventIds) {
            Event event = eventDao.findById(eventId);
            if (event != null && event.isActive()) {
                events.add(event);
            }
        }
        
        return events;
    }
    
    /**
     * 获取用户参与的活动ID列表
     * @param userId 用户ID
     * @return 活动ID列表
     */
    public List<Integer> getEventIdsByParticipant(int userId) {
        // 验证参数
        if (userId <= 0) {
            return new ArrayList<>();
        }
        
        return eventParticipantDao.findEventIdsByUserId(userId);
    }
    
    /**
     * 搜索活动
     * @param keyword 关键词
     * @return 活动列表
     */
    public List<Event> searchEvents(String keyword) {
        // 验证参数
        if (ValidationUtil.isEmpty(keyword)) {
            return new ArrayList<>();
        }
        
        return eventDao.searchEvents(keyword);
    }
    
    /**
     * 获取活动是否已满
     * @param eventId 活动ID
     * @return 如果活动已满返回true
     */
    public boolean isEventFull(int eventId) {
        // 验证参数
        if (eventId <= 0) {
            return false;
        }
        
        return eventDao.isEventFull(eventId);
    }
    
    /**
     * 获取活动余额（剩余参与名额）
     * @param eventId 活动ID
     * @return 剩余参与名额
     */
    public int getEventRemaining(int eventId) {
        // 获取活动
        Event event = eventDao.findById(eventId);
        
        // 如果活动不存在
        if (event == null) {
            return 0;
        }
        
        return event.getMaxParticipants() - event.getCurrentParticipants();
    }
    
    /**
     * 获取所有活动类型
     * @return 活动类型列表
     */
    public List<String> getAllEventTypes() {
        List<String> eventTypes = new ArrayList<>();
        eventTypes.add("社交");
        eventTypes.add("学习");
        eventTypes.add("运动");
        eventTypes.add("文化");
        eventTypes.add("娱乐");
        eventTypes.add("旅游");
        eventTypes.add("其他");
        return eventTypes;
    }
    
    /**
     * 获取所有用户ID
     * @return 用户ID列表
     */
    public List<Integer> getAllUserIds() {
        List<User> users = userDao.findAll();
        List<Integer> userIds = new ArrayList<>();
        
        for (User user : users) {
            userIds.add(user.getId());
        }
        
        return userIds;
    }
}