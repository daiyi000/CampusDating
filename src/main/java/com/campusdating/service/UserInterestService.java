package com.campusdating.service;

import com.campusdating.dao.UserInterestDao;
import com.campusdating.model.UserInterest;
import com.campusdating.util.ValidationUtil;

import java.util.List;

/**
 * 用户兴趣服务类
 * 提供用户兴趣关联的业务逻辑
 */
public class UserInterestService {
    
    private UserInterestDao userInterestDao;
    
    /**
     * 构造函数
     */
    public UserInterestService() {
        userInterestDao = new UserInterestDao();
    }
    
    /**
     * 添加用户兴趣
     * @param userInterest 用户兴趣对象
     * @return 成功返回true，失败返回false
     */
    public boolean addUserInterest(UserInterest userInterest) {
        // 验证用户兴趣
        if (userInterest == null || userInterest.getUserId() <= 0 || userInterest.getInterestId() <= 0) {
            return false;
        }
        
        // 检查用户兴趣是否已存在
        UserInterest existingUserInterest = userInterestDao.findByUserIdAndInterestId(
                userInterest.getUserId(), userInterest.getInterestId());
        
        if (existingUserInterest != null) {
            return false;
        }
        
        // 检查兴趣等级是否有效
        if (userInterest.getInterestLevel() < 1 || userInterest.getInterestLevel() > 5) {
            userInterest.setInterestLevel(3); // 默认为中等兴趣
        }
        
        // 保存用户兴趣
        return userInterestDao.save(userInterest);
    }
    
    /**
     * 批量添加用户兴趣
     * @param userId 用户ID
     * @param interestIds 兴趣ID列表
     * @return 成功添加的数量
     */
    public int addUserInterests(int userId, List<Integer> interestIds) {
        // 验证参数
        if (userId <= 0 || interestIds == null || interestIds.isEmpty()) {
            return 0;
        }
        
        return userInterestDao.saveBatch(userId, interestIds);
    }
    
    /**
     * 根据ID获取用户兴趣
     * @param id 用户兴趣ID
     * @return 用户兴趣对象，如果不存在返回null
     */
    public UserInterest getUserInterestById(int id) {
        return userInterestDao.findById(id);
    }
    
    /**
     * 获取用户的所有兴趣关联
     * @param userId 用户ID
     * @return 用户兴趣列表
     */
    public List<UserInterest> getUserInterests(int userId) {
        return userInterestDao.findByUserId(userId);
    }
    
    /**
     * 获取对指定兴趣感兴趣的所有用户ID
     * @param interestId 兴趣ID
     * @return 用户ID列表
     */
    public List<Integer> getUserIdsByInterest(int interestId) {
        return userInterestDao.findUserIdsByInterestId(interestId);
    }
    
    /**
     * 获取用户对指定兴趣的关联
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @return 用户兴趣对象，如果不存在返回null
     */
    public UserInterest getUserInterest(int userId, int interestId) {
        return userInterestDao.findByUserIdAndInterestId(userId, interestId);
    }
    
    /**
     * 更新用户兴趣
     * @param userInterest 用户兴趣对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateUserInterest(UserInterest userInterest) {
        // 验证用户兴趣
        if (userInterest == null || userInterest.getId() <= 0) {
            return false;
        }
        
        return userInterestDao.update(userInterest);
    }
    
    /**
     * 更新用户兴趣等级
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @param interestLevel 兴趣等级
     * @return 成功返回true，失败返回false
     */
    public boolean updateInterestLevel(int userId, int interestId, int interestLevel) {
        // 验证参数
        if (userId <= 0 || interestId <= 0 || interestLevel < 1 || interestLevel > 5) {
            return false;
        }
        
        return userInterestDao.updateInterestLevel(userId, interestId, interestLevel);
    }
    
    /**
     * 移除用户兴趣
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @return 成功返回true，失败返回false
     */
    public boolean removeUserInterest(int userId, int interestId) {
        // 验证参数
        if (userId <= 0 || interestId <= 0) {
            return false;
        }
        
        return userInterestDao.deleteByUserIdAndInterestId(userId, interestId);
    }
    
    /**
     * 移除用户的所有兴趣
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean removeAllUserInterests(int userId) {
        // 验证参数
        if (userId <= 0) {
            return false;
        }
        
        return userInterestDao.deleteAllByUserId(userId);
    }
    
    /**
     * 计算两个用户的共同兴趣数量
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 共同兴趣数量
     */
    public int countCommonInterests(int userId1, int userId2) {
        // 验证参数
        if (userId1 <= 0 || userId2 <= 0) {
            return 0;
        }
        
        return userInterestDao.countCommonInterests(userId1, userId2);
    }
    
    /**
     * 检查用户是否对指定兴趣感兴趣
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @return 如果用户对该兴趣感兴趣返回true
     */
    public boolean isUserInterestedIn(int userId, int interestId) {
        // 验证参数
        if (userId <= 0 || interestId <= 0) {
            return false;
        }
        
        return userInterestDao.findByUserIdAndInterestId(userId, interestId) != null;
    }
}