package com.campusdating.dao;

import com.campusdating.model.UserInterest;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户兴趣关联数据访问对象类
 * 负责UserInterest对象的CRUD操作
 */
public class UserInterestDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public UserInterestDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存用户兴趣关联到数据库
     * @param userInterest 用户兴趣关联对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(UserInterest userInterest) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO user_interests(user_id, interest_id, interest_level) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userInterest.getUserId());
            ps.setInt(2, userInterest.getInterestId());
            ps.setInt(3, userInterest.getInterestLevel());
            
            flag = ps.executeUpdate() > 0;
            
            // 获取生成的ID
            if (flag) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    userInterest.setId(rs.getInt(1));
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
     * 更新用户兴趣关联信息
     * @param userInterest 用户兴趣关联对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(UserInterest userInterest) {
        boolean flag = false;
        try {
            String sql = "UPDATE user_interests SET user_id=?, interest_id=?, interest_level=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userInterest.getUserId());
            ps.setInt(2, userInterest.getInterestId());
            ps.setInt(3, userInterest.getInterestLevel());
            ps.setInt(4, userInterest.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取用户兴趣关联
     * @param id 用户兴趣关联ID
     * @return 用户兴趣关联对象，如果不存在返回null
     */
    public UserInterest findById(int id) {
        UserInterest userInterest = null;
        try {
            String sql = "SELECT * FROM user_interests WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                userInterest = mapResultSetToUserInterest(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInterest;
    }
    
    /**
     * 获取用户的所有兴趣关联
     * @param userId 用户ID
     * @return 用户兴趣关联列表
     */
    public List<UserInterest> findByUserId(int userId) {
        List<UserInterest> userInterests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM user_interests WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                UserInterest userInterest = mapResultSetToUserInterest(rs);
                userInterests.add(userInterest);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInterests;
    }
    
    /**
     * 获取对指定兴趣感兴趣的所有用户ID
     * @param interestId 兴趣ID
     * @return 用户ID列表
     */
    public List<Integer> findUserIdsByInterestId(int interestId) {
        List<Integer> userIds = new ArrayList<>();
        try {
            String sql = "SELECT user_id FROM user_interests WHERE interest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, interestId);
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
     * 获取用户对指定兴趣的关联
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @return 用户兴趣关联对象，如果不存在返回null
     */
    public UserInterest findByUserIdAndInterestId(int userId, int interestId) {
        UserInterest userInterest = null;
        try {
            String sql = "SELECT * FROM user_interests WHERE user_id=? AND interest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, interestId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                userInterest = mapResultSetToUserInterest(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInterest;
    }
    
    /**
     * 删除用户兴趣关联
     * @param id 用户兴趣关联ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM user_interests WHERE id=?";
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
     * 删除用户的指定兴趣关联
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteByUserIdAndInterestId(int userId, int interestId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM user_interests WHERE user_id=? AND interest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, interestId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 删除用户的所有兴趣关联
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteAllByUserId(int userId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM user_interests WHERE user_id=?";
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
     * 更新用户兴趣等级
     * @param userId 用户ID
     * @param interestId 兴趣ID
     * @param interestLevel 兴趣等级
     * @return 成功返回true，失败返回false
     */
    public boolean updateInterestLevel(int userId, int interestId, int interestLevel) {
        boolean flag = false;
        try {
            String sql = "UPDATE user_interests SET interest_level=? WHERE user_id=? AND interest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, interestLevel);
            ps.setInt(2, userId);
            ps.setInt(3, interestId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 获取具有相同兴趣的用户数量
     * @param userId 用户ID
     * @param otherUserId 另一个用户ID
     * @return 共同兴趣数量
     */
    public int countCommonInterests(int userId, int otherUserId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM user_interests ui1 " +
                        "JOIN user_interests ui2 ON ui1.interest_id = ui2.interest_id " +
                        "WHERE ui1.user_id=? AND ui2.user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, otherUserId);
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
     * 批量保存用户兴趣
     * @param userId 用户ID
     * @param interestIds 兴趣ID列表
     * @return 成功保存的数量
     */
    public int saveBatch(int userId, List<Integer> interestIds) {
        int successCount = 0;
        
        for (Integer interestId : interestIds) {
            // 检查是否已存在
            if (findByUserIdAndInterestId(userId, interestId) != null) {
                continue;
            }
            
            UserInterest userInterest = new UserInterest(userId, interestId, 3); // 默认兴趣等级为3
            if (save(userInterest)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 将ResultSet映射为UserInterest对象
     * @param rs ResultSet
     * @return UserInterest对象
     * @throws SQLException SQLException
     */
    private UserInterest mapResultSetToUserInterest(ResultSet rs) throws SQLException {
        UserInterest userInterest = new UserInterest();
        userInterest.setId(rs.getInt("id"));
        userInterest.setUserId(rs.getInt("user_id"));
        userInterest.setInterestId(rs.getInt("interest_id"));
        userInterest.setInterestLevel(rs.getInt("interest_level"));
        return userInterest;
    }
}