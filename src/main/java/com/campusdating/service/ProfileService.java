package com.campusdating.service;

import com.campusdating.dao.ProfileDao;
import com.campusdating.model.Profile;
import com.campusdating.util.ValidationUtil;

import java.util.List;

/**
 * 个人资料服务类
 * 提供个人资料相关的业务逻辑
 */
public class ProfileService {
    
    private ProfileDao profileDao;
    
    /**
     * 构造函数
     */
    public ProfileService() {
        profileDao = new ProfileDao();
    }
    
    /**
     * 根据ID获取个人资料
     * @param id 个人资料ID
     * @return 个人资料对象，如果不存在返回null
     */
    public Profile getProfileById(int id) {
        return profileDao.findById(id);
    }
    
    /**
     * 根据用户ID获取个人资料
     * @param userId 用户ID
     * @return 个人资料对象，如果不存在返回null
     */
    public Profile getProfileByUserId(int userId) {
        return profileDao.findByUserId(userId);
    }
    
    /**
     * 获取所有个人资料
     * @return 个人资料列表
     */
    public List<Profile> getAllProfiles() {
        return profileDao.findAll();
    }
    
    /**
     * 保存或更新个人资料
     * @param profile 个人资料对象
     * @return 成功返回true，失败返回false
     */
    public boolean saveOrUpdateProfile(Profile profile) {
        // 验证个人资料
        if (profile == null || profile.getUserId() <= 0) {
            return false;
        }
        
        // 检查个人资料是否已存在
        Profile existingProfile = profileDao.findByUserId(profile.getUserId());
        
        // 如果个人资料已存在，更新
        if (existingProfile != null) {
            // 设置ID
            profile.setId(existingProfile.getId());
            return profileDao.update(profile);
        } 
        // 否则，创建新的个人资料
        else {
            return profileDao.save(profile);
        }
    }
    
    /**
     * 更新个人资料
     * @param profile 个人资料对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateProfile(Profile profile) {
        // 验证个人资料
        if (profile == null || profile.getId() <= 0) {
            return false;
        }
        
        return profileDao.update(profile);
    }
    
    /**
     * 删除个人资料
     * @param id 个人资料ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteProfile(int id) {
        return profileDao.delete(id);
    }
    
    /**
     * 根据用户ID删除个人资料
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteProfileByUserId(int userId) {
        return profileDao.deleteByUserId(userId);
    }
    
    /**
     * 更新头像URL
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 成功返回true，失败返回false
     */
    public boolean updateAvatarUrl(int userId, String avatarUrl) {
        // 验证参数
        if (userId <= 0 || ValidationUtil.isEmpty(avatarUrl)) {
            return false;
        }
        
        return profileDao.updateAvatarUrl(userId, avatarUrl);
    }
    
    /**
     * 根据条件搜索个人资料
     * @param gender 性别
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @param school 学校
     * @param department 院系
     * @return 符合条件的个人资料列表
     */
    public List<Profile> searchProfiles(String gender, int minAge, int maxAge, String school, String department) {
        return profileDao.searchProfiles(gender, minAge, maxAge, school, department);
    }
    
    /**
     * 检查个人资料是否完整
     * @param userId 用户ID
     * @return 如果个人资料完整返回true，否则返回false
     */
    public boolean isProfileComplete(int userId) {
        Profile profile = profileDao.findByUserId(userId);
        
        // 如果个人资料不存在
        if (profile == null) {
            return false;
        }
        
        // 检查必填字段
        return !ValidationUtil.isEmpty(profile.getFullName()) && 
               !ValidationUtil.isEmpty(profile.getGender()) && 
               profile.getBirthday() != null && 
               !ValidationUtil.isEmpty(profile.getSchool()) && 
               !ValidationUtil.isEmpty(profile.getDepartment());
    }
    
    /**
     * 获取个人资料完整度百分比
     * @param userId 用户ID
     * @return 完整度百分比（0-100）
     */
    public int getProfileCompletionPercentage(int userId) {
        Profile profile = profileDao.findByUserId(userId);
        
        // 如果个人资料不存在
        if (profile == null) {
            return 0;
        }
        
        int totalFields = 10; // 总字段数
        int completedFields = 0;
        
        // 检查各个字段
        if (!ValidationUtil.isEmpty(profile.getFullName())) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getGender())) completedFields++;
        if (profile.getBirthday() != null) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getSchool())) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getDepartment())) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getMajor())) completedFields++;
        if (profile.getGrade() > 0) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getHometown())) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getBio())) completedFields++;
        if (!ValidationUtil.isEmpty(profile.getAvatarUrl())) completedFields++;
        
        // 计算百分比
        return (completedFields * 100) / totalFields;
    }
}