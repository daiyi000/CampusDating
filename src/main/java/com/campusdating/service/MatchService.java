package com.campusdating.service;

import com.campusdating.dao.MatchDao;
import com.campusdating.dao.UserDao;
import com.campusdating.dao.ProfileDao;
import com.campusdating.model.Match;
import com.campusdating.model.User;
import com.campusdating.model.Profile;
import com.campusdating.model.Interest;
import com.campusdating.util.ValidationUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配服务类
 * 提供匹配相关的业务逻辑
 */
public class MatchService {
    
    private MatchDao matchDao;
    private UserDao userDao;
    private ProfileDao profileDao;
    private InterestService interestService;
    
    /**
     * 构造函数
     */
    public MatchService() {
        matchDao = new MatchDao();
        userDao = new UserDao();
        profileDao = new ProfileDao();
        interestService = new InterestService();
    }
    
    /**
     * 创建匹配
     * @param match 匹配对象
     * @return 成功返回true，失败返回false
     */
    public boolean createMatch(Match match) {
        // 验证匹配
        if (match == null || match.getUserOneId() <= 0 || match.getUserTwoId() <= 0 || 
            match.getUserOneId() == match.getUserTwoId()) {
            return false;
        }
        
        // 检查用户是否存在
        User userOne = userDao.findById(match.getUserOneId());
        User userTwo = userDao.findById(match.getUserTwoId());
        
        if (userOne == null || userTwo == null) {
            return false;
        }
        
        // 检查匹配是否已存在
        Match existingMatch = matchDao.findMatchBetweenUsers(match.getUserOneId(), match.getUserTwoId());
        
        if (existingMatch != null) {
            return false;
        }
        
        // 计算匹配分数
        int matchScore = calculateMatchScore(match.getUserOneId(), match.getUserTwoId());
        match.setMatchScore(matchScore);
        
        // 设置匹配状态为待处理
        if (ValidationUtil.isEmpty(match.getMatchStatus())) {
            match.setMatchStatus("pending");
        }
        
        // 设置创建时间和更新时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        match.setCreateTime(now);
        match.setUpdateTime(now);
        
        // 设置匹配为活动状态
        match.setActive(true);
        
        // 保存匹配
        return matchDao.save(match);
    }
    
    /**
     * 根据ID获取匹配
     * @param id 匹配ID
     * @return 匹配对象，如果不存在返回null
     */
    public Match getMatchById(int id) {
        return matchDao.findById(id);
    }
    
    /**
     * 获取用户的所有匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> getUserMatches(int userId) {
        return matchDao.findMatchesByUserId(userId);
    }
    
    /**
     * 获取两个用户之间的匹配
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 匹配对象，如果不存在返回null
     */
    public Match getMatchBetweenUsers(int userId1, int userId2) {
        return matchDao.findMatchBetweenUsers(userId1, userId2);
    }
    
    /**
     * 获取用户的已接受匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> getAcceptedMatches(int userId) {
        return matchDao.findAcceptedMatchesByUserId(userId);
    }
    
    /**
     * 获取用户的待处理匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> getPendingMatches(int userId) {
        return matchDao.findPendingMatchesByUserId(userId);
    }
    
    /**
     * 接受匹配
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean acceptMatch(int matchId) {
        return matchDao.updateMatchStatus(matchId, "accepted");
    }
    
    /**
     * 拒绝匹配
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean rejectMatch(int matchId) {
        return matchDao.updateMatchStatus(matchId, "rejected");
    }
    
    /**
     * 取消匹配
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean cancelMatch(int matchId) {
        return matchDao.deactivateMatch(matchId);
    }
    
    /**
     * 删除匹配
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteMatch(int matchId) {
        return matchDao.delete(matchId);
    }
    
    /**
     * 计算两个用户之间的匹配分数
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 匹配分数（0-100）
     */
    public int calculateMatchScore(int userId1, int userId2) {
        // 基础分数
        int score = 0;
        
        try {
            // 获取用户个人资料
            Profile profile1 = profileDao.findByUserId(userId1);
            Profile profile2 = profileDao.findByUserId(userId2);
            
            // 如果个人资料不存在，返回基础分数
            if (profile1 == null || profile2 == null) {
                return score;
            }
            
            // 学校匹配（最高30分）
            if (profile1.getSchool() != null && profile2.getSchool() != null && 
                profile1.getSchool().equals(profile2.getSchool())) {
                score += 30;
            }
            
            // 院系匹配（最高20分）
            if (profile1.getDepartment() != null && profile2.getDepartment() != null && 
                profile1.getDepartment().equals(profile2.getDepartment())) {
                score += 20;
            }
            
            // 专业匹配（最高10分）
            if (profile1.getMajor() != null && profile2.getMajor() != null && 
                profile1.getMajor().equals(profile2.getMajor())) {
                score += 10;
            }
            
            // 年级匹配（最高5分）
            if (profile1.getGrade() > 0 && profile2.getGrade() > 0) {
                int gradeDiff = Math.abs(profile1.getGrade() - profile2.getGrade());
                if (gradeDiff == 0) {
                    score += 5;
                } else if (gradeDiff == 1) {
                    score += 3;
                } else if (gradeDiff == 2) {
                    score += 1;
                }
            }
            
            // 兴趣匹配（最高35分）
            List<Interest> interests1 = interestService.getUserInterests(userId1);
            List<Interest> interests2 = interestService.getUserInterests(userId2);
            
            if (!interests1.isEmpty() && !interests2.isEmpty()) {
                // 计算共同兴趣数量
                int commonInterests = 0;
                for (Interest interest1 : interests1) {
                    for (Interest interest2 : interests2) {
                        if (interest1.getId() == interest2.getId()) {
                            commonInterests++;
                            break;
                        }
                    }
                }
                
                // 兴趣匹配分数
                int maxCommonInterests = Math.min(interests1.size(), interests2.size());
                if (maxCommonInterests > 0) {
                    score += (commonInterests * 35) / maxCommonInterests;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return Math.min(score, 100);
    }
    
    /**
     * 推荐用户匹配
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐的用户ID列表
     */
    public List<Integer> getRecommendedMatches(int userId, int limit) {
        List<Integer> recommendedUserIds = new ArrayList<>();
        
        try {
            // 获取用户个人资料
            Profile userProfile = profileDao.findByUserId(userId);
            
            // 如果个人资料不存在，返回空列表
            if (userProfile == null) {
                return recommendedUserIds;
            }
            
            // 获取所有用户个人资料
            List<Profile> allProfiles = profileDao.findAll();
            
            // 计算所有用户的匹配分数
            List<MatchScore> matchScores = new ArrayList<>();
            
            for (Profile profile : allProfiles) {
                // 排除自己
                if (profile.getUserId() == userId) {
                    continue;
                }
                
                // 检查是否已经匹配
                Match existingMatch = matchDao.findMatchBetweenUsers(userId, profile.getUserId());
                if (existingMatch != null) {
                    continue;
                }
                
                // 计算匹配分数
                int score = calculateMatchScore(userId, profile.getUserId());
                
                matchScores.add(new MatchScore(profile.getUserId(), score));
            }
            
            // 按分数降序排序
            matchScores.sort((ms1, ms2) -> ms2.getScore() - ms1.getScore());
            
            // 获取前limit个用户
            int count = 0;
            for (MatchScore matchScore : matchScores) {
                if (count >= limit) {
                    break;
                }
                
                recommendedUserIds.add(matchScore.getUserId());
                count++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return recommendedUserIds;
    }
    
    /**
     * 获取用户的匹配统计信息
     * @param userId 用户ID
     * @return 包含总匹配数、已接受匹配数和待处理匹配数的数组
     */
    public int[] getMatchStatistics(int userId) {
        return matchDao.getMatchStatistics(userId);
    }
    
    /**
     * 检查用户是否有足够的匹配数量
     * @param userId 用户ID
     * @param minCount 最低匹配数量
     * @return 如果匹配数量大于等于最低匹配数量则返回true
     */
    public boolean hasEnoughMatches(int userId, int minCount) {
        return matchDao.hasEnoughMatches(userId, minCount);
    }
    
    /**
     * 辅助类：用于存储用户ID和匹配分数
     */
    private static class MatchScore {
        private int userId;
        private int score;
        
        public MatchScore(int userId, int score) {
            this.userId = userId;
            this.score = score;
        }
        
        public int getUserId() {
            return userId;
        }
        
        public int getScore() {
            return score;
        }
    }
}