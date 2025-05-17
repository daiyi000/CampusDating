package com.campusdating.dao;

import com.campusdating.model.Interest;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 兴趣爱好数据访问对象类
 * 负责Interest对象的CRUD操作
 */
public class InterestDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public InterestDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存兴趣爱好到数据库
     * @param interest 兴趣爱好对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Interest interest) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO interests(name, category, description, icon_url) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, interest.getName());
            ps.setString(2, interest.getCategory());
            ps.setString(3, interest.getDescription());
            ps.setString(4, interest.getIconUrl());
            
            flag = ps.executeUpdate() > 0;
            
            // 获取生成的ID
            if (flag) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    interest.setId(rs.getInt(1));
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
     * 更新兴趣爱好信息
     * @param interest 兴趣爱好对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Interest interest) {
        boolean flag = false;
        try {
            String sql = "UPDATE interests SET name=?, category=?, description=?, icon_url=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, interest.getName());
            ps.setString(2, interest.getCategory());
            ps.setString(3, interest.getDescription());
            ps.setString(4, interest.getIconUrl());
            ps.setInt(5, interest.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取兴趣爱好
     * @param id 兴趣爱好ID
     * @return 兴趣爱好对象，如果不存在返回null
     */
    public Interest findById(int id) {
        Interest interest = null;
        try {
            String sql = "SELECT * FROM interests WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                interest = mapResultSetToInterest(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interest;
    }
    
    /**
     * 根据名称获取兴趣爱好
     * @param name 兴趣爱好名称
     * @return 兴趣爱好对象，如果不存在返回null
     */
    public Interest findByName(String name) {
        Interest interest = null;
        try {
            String sql = "SELECT * FROM interests WHERE name=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                interest = mapResultSetToInterest(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interest;
    }
    
    /**
     * 获取所有兴趣爱好
     * @return 兴趣爱好列表
     */
    public List<Interest> findAll() {
        List<Interest> interests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM interests ORDER BY name ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Interest interest = mapResultSetToInterest(rs);
                interests.add(interest);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }
    
    /**
     * 获取指定类别的兴趣爱好
     * @param category 类别
     * @return 兴趣爱好列表
     */
    public List<Interest> findByCategory(String category) {
        List<Interest> interests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM interests WHERE category=? ORDER BY name ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Interest interest = mapResultSetToInterest(rs);
                interests.add(interest);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }
    
    /**
     * 获取所有可用的兴趣爱好类别
     * @return 类别列表
     */
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT category FROM interests ORDER BY category ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * 删除兴趣爱好
     * @param id 兴趣爱好ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM interests WHERE id=?";
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
     * 获取用户的兴趣爱好
     * @param userId 用户ID
     * @return 兴趣爱好列表
     */
    public List<Interest> findUserInterests(int userId) {
        List<Interest> interests = new ArrayList<>();
        try {
            String sql = "SELECT i.* FROM interests i JOIN user_interests ui ON i.id = ui.interest_id WHERE ui.user_id=? ORDER BY i.name ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Interest interest = mapResultSetToInterest(rs);
                interests.add(interest);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }
    
    /**
     * 搜索兴趣爱好
     * @param keyword 关键词
     * @return 兴趣爱好列表
     */
    public List<Interest> searchInterests(String keyword) {
        List<Interest> interests = new ArrayList<>();
        try {
            String sql = "SELECT * FROM interests WHERE name LIKE ? OR description LIKE ? ORDER BY name ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Interest interest = mapResultSetToInterest(rs);
                interests.add(interest);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interests;
    }
    
    /**
     * 获取指定兴趣爱好的用户数量
     * @param interestId 兴趣爱好ID
     * @return 用户数量
     */
    public int getUserCountByInterest(int interestId) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM user_interests WHERE interest_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, interestId);
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
     * 将ResultSet映射为Interest对象
     * @param rs ResultSet
     * @return Interest对象
     * @throws SQLException SQLException
     */
    private Interest mapResultSetToInterest(ResultSet rs) throws SQLException {
        Interest interest = new Interest();
        interest.setId(rs.getInt("id"));
        interest.setName(rs.getString("name"));
        interest.setCategory(rs.getString("category"));
        interest.setDescription(rs.getString("description"));
        interest.setIconUrl(rs.getString("icon_url"));
        return interest;
    }
}