package com.campusdating.dao;

import com.campusdating.model.Match;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配数据访问对象类
 * 负责Match对象的CRUD操作
 */
public class MatchDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public MatchDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存匹配到数据库
     * @param match 匹配对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Match match) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO matches(user_one_id, user_two_id, match_score, match_status, create_time, update_time, is_active, match_notes) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, match.getUserOneId());
            ps.setInt(2, match.getUserTwoId());
            ps.setInt(3, match.getMatchScore());
            ps.setString(4, match.getMatchStatus());
            ps.setTimestamp(5, match.getCreateTime());
            ps.setTimestamp(6, match.getUpdateTime());
            ps.setBoolean(7, match.isActive());
            ps.setString(8, match.getMatchNotes());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 更新匹配信息
     * @param match 匹配对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Match match) {
        boolean flag = false;
        try {
            String sql = "UPDATE matches SET user_one_id=?, user_two_id=?, match_score=?, match_status=?, update_time=?, is_active=?, match_notes=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, match.getUserOneId());
            ps.setInt(2, match.getUserTwoId());
            ps.setInt(3, match.getMatchScore());
            ps.setString(4, match.getMatchStatus());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setBoolean(6, match.isActive());
            ps.setString(7, match.getMatchNotes());
            ps.setInt(8, match.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取匹配
     * @param id 匹配ID
     * @return 匹配对象，如果不存在返回null
     */
    public Match findById(int id) {
        Match match = null;
        try {
            String sql = "SELECT * FROM matches WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                match = mapResultSetToMatch(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }
    
    /**
     * 获取用户的所有匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> findMatchesByUserId(int userId) {
        List<Match> matches = new ArrayList<>();
        try {
            String sql = "SELECT * FROM matches WHERE (user_one_id=? OR user_two_id=?) AND is_active=true ORDER BY update_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Match match = mapResultSetToMatch(rs);
                matches.add(match);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
    
    /**
     * 获取两个用户之间的匹配
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 匹配对象，如果不存在返回null
     */
    public Match findMatchBetweenUsers(int userId1, int userId2) {
        Match match = null;
        try {
            String sql = "SELECT * FROM matches WHERE (user_one_id=? AND user_two_id=?) OR (user_one_id=? AND user_two_id=?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                match = mapResultSetToMatch(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return match;
    }
    
    /**
     * 获取用户的所有已接受匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> findAcceptedMatchesByUserId(int userId) {
        List<Match> matches = new ArrayList<>();
        try {
            String sql = "SELECT * FROM matches WHERE (user_one_id=? OR user_two_id=?) AND match_status='accepted' AND is_active=true ORDER BY update_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Match match = mapResultSetToMatch(rs);
                matches.add(match);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
    
    /**
     * 获取用户的所有待处理匹配
     * @param userId 用户ID
     * @return 匹配列表
     */
    public List<Match> findPendingMatchesByUserId(int userId) {
        List<Match> matches = new ArrayList<>();
        try {
            String sql = "SELECT * FROM matches WHERE (user_one_id=? OR user_two_id=?) AND match_status='pending' AND is_active=true ORDER BY update_time DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Match match = mapResultSetToMatch(rs);
                matches.add(match);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
    
    /**
     * 更新匹配状态
     * @param matchId 匹配ID
     * @param status 状态
     * @return 成功返回true，失败返回false
     */
    public boolean updateMatchStatus(int matchId, String status) {
        boolean flag = false;
        try {
            String sql = "UPDATE matches SET match_status=?, update_time=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, matchId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 将匹配设为非活动状态
     * @param matchId 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean deactivateMatch(int matchId) {
        boolean flag = false;
        try {
            String sql = "UPDATE matches SET is_active=false, update_time=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, matchId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 删除匹配
     * @param id 匹配ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM matches WHERE id=?";
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
     * 获取所有匹配分数高于指定值的匹配
     * @param minScore 最低匹配分数
     * @return 匹配列表
     */
    public List<Match> findMatchesAboveScore(int minScore) {
        List<Match> matches = new ArrayList<>();
        try {
            String sql = "SELECT * FROM matches WHERE match_score >= ? AND is_active=true ORDER BY match_score DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, minScore);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Match match = mapResultSetToMatch(rs);
                matches.add(match);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
    
    /**
     * 获取用户的匹配统计
     * @param userId 用户ID
     * @return 包含总匹配数、已接受匹配数和待处理匹配数的数组
     */
    public int[] getMatchStatistics(int userId) {
        int[] stats = new int[3]; // [总匹配数, 已接受匹配数, 待处理匹配数]
        try {
            String sql = "SELECT COUNT(*) as total, " +
                        "SUM(CASE WHEN match_status='accepted' THEN 1 ELSE 0 END) as accepted, " +
                        "SUM(CASE WHEN match_status='pending' THEN 1 ELSE 0 END) as pending " +
                        "FROM matches WHERE (user_one_id=? OR user_two_id=?) AND is_active=true";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                stats[0] = rs.getInt("total");
                stats[1] = rs.getInt("accepted");
                stats[2] = rs.getInt("pending");
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
    
    /**
     * 检查用户是否有足够的匹配数量
     * @param userId 用户ID
     * @param minCount 最低匹配数量
     * @return 如果匹配数量大于等于最低匹配数量则返回true
     */
    public boolean hasEnoughMatches(int userId, int minCount) {
        boolean result = false;
        try {
            String sql = "SELECT COUNT(*) FROM matches WHERE (user_one_id=? OR user_two_id=?) AND is_active=true";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                result = rs.getInt(1) >= minCount;
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 将ResultSet映射为Match对象
     * @param rs ResultSet
     * @return Match对象
     * @throws SQLException SQLException
     */
    private Match mapResultSetToMatch(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setId(rs.getInt("id"));
        match.setUserOneId(rs.getInt("user_one_id"));
        match.setUserTwoId(rs.getInt("user_two_id"));
        match.setMatchScore(rs.getInt("match_score"));
        match.setMatchStatus(rs.getString("match_status"));
        match.setCreateTime(rs.getTimestamp("create_time"));
        match.setUpdateTime(rs.getTimestamp("update_time"));
        match.setActive(rs.getBoolean("is_active"));
        match.setMatchNotes(rs.getString("match_notes"));
        return match;
    }
}