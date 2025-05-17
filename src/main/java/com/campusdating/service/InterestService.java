package com.campusdating.service;

import com.campusdating.dao.InterestDao;
import com.campusdating.model.Interest;
import com.campusdating.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 兴趣服务类
 * 提供兴趣相关的业务逻辑
 */
public class InterestService {
    
    private InterestDao interestDao;
    
    /**
     * 构造函数
     */
    public InterestService() {
        interestDao = new InterestDao();
    }
    
    /**
     * 创建兴趣
     * @param interest 兴趣对象
     * @return 成功返回true，失败返回false
     */
    public boolean createInterest(Interest interest) {
        // 验证兴趣
        if (interest == null || ValidationUtil.isEmpty(interest.getName()) || 
            ValidationUtil.isEmpty(interest.getCategory())) {
            return false;
        }
        
        // 检查兴趣是否已存在
        Interest existingInterest = interestDao.findByName(interest.getName());
        if (existingInterest != null) {
            return false;
        }
        
        // 保存兴趣
        return interestDao.save(interest);
    }
    
    /**
     * 根据ID获取兴趣
     * @param id 兴趣ID
     * @return 兴趣对象，如果不存在返回null
     */
    public Interest getInterestById(int id) {
        return interestDao.findById(id);
    }
    
    /**
     * 根据名称获取兴趣
     * @param name 兴趣名称
     * @return 兴趣对象，如果不存在返回null
     */
    public Interest getInterestByName(String name) {
        return interestDao.findByName(name);
    }
    
    /**
     * 获取所有兴趣
     * @return 兴趣列表
     */
    public List<Interest> getAllInterests() {
        return interestDao.findAll();
    }
    
    /**
     * 根据类别获取兴趣
     * @param category 类别
     * @return 兴趣列表
     */
    public List<Interest> getInterestsByCategory(String category) {
        return interestDao.findByCategory(category);
    }
    
    /**
     * 获取所有兴趣类别
     * @return 类别列表
     */
    public List<String> getAllCategories() {
        return interestDao.findAllCategories();
    }
    
    /**
     * 更新兴趣
     * @param interest 兴趣对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateInterest(Interest interest) {
        // 验证兴趣
        if (interest == null || interest.getId() <= 0 || ValidationUtil.isEmpty(interest.getName()) || 
            ValidationUtil.isEmpty(interest.getCategory())) {
            return false;
        }
        
        return interestDao.update(interest);
    }
    
    /**
     * 删除兴趣
     * @param id 兴趣ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteInterest(int id) {
        return interestDao.delete(id);
    }
    
    /**
     * 获取用户的兴趣
     * @param userId 用户ID
     * @return 兴趣列表
     */
    public List<Interest> getUserInterests(int userId) {
        return interestDao.findUserInterests(userId);
    }
    
    /**
     * 搜索兴趣
     * @param keyword 关键词
     * @return 兴趣列表
     */
    public List<Interest> searchInterests(String keyword) {
        return interestDao.searchInterests(keyword);
    }
    
    /**
     * 获取指定兴趣的用户数量
     * @param interestId 兴趣ID
     * @return 用户数量
     */
    public int getUserCountByInterest(int interestId) {
        return interestDao.getUserCountByInterest(interestId);
    }
    
    /**
     * 获取热门兴趣
     * @param limit 热门兴趣数量
     * @return 热门兴趣列表
     */
    public List<Interest> getPopularInterests(int limit) {
        // 获取所有兴趣
        List<Interest> allInterests = interestDao.findAll();
        
        // 计算每个兴趣的用户数量
        List<InterestPopularity> popularities = new ArrayList<>();
        
        for (Interest interest : allInterests) {
            int userCount = interestDao.getUserCountByInterest(interest.getId());
            popularities.add(new InterestPopularity(interest, userCount));
        }
        
        // 按用户数量降序排序
        popularities.sort((ip1, ip2) -> ip2.getUserCount() - ip1.getUserCount());
        
        // 获取前limit个兴趣
        List<Interest> popularInterests = new ArrayList<>();
        int count = 0;
        
        for (InterestPopularity popularity : popularities) {
            if (count >= limit) {
                break;
            }
            
            popularInterests.add(popularity.getInterest());
            count++;
        }
        
        return popularInterests;
    }
    
    /**
     * 辅助类：用于存储兴趣和用户数量
     */
    private static class InterestPopularity {
        private Interest interest;
        private int userCount;
        
        public InterestPopularity(Interest interest, int userCount) {
            this.interest = interest;
            this.userCount = userCount;
        }
        
        public Interest getInterest() {
            return interest;
        }
        
        public int getUserCount() {
            return userCount;
        }
    }
}