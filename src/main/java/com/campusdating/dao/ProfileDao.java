package com.campusdating.dao;

import com.campusdating.model.Profile;
import com.campusdating.util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户个人资料数据访问对象类
 * 负责Profile对象的CRUD操作
 */
public class ProfileDao {
    
    private Connection connection;
    
    /**
     * 构造函数，初始化数据库连接
     */
    public ProfileDao() {
        try {
            connection = DBConnectionUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 保存个人资料到数据库
     * @param profile 个人资料对象
     * @return 成功返回true，失败返回false
     */
    public boolean save(Profile profile) {
        boolean flag = false;
        try {
            String sql = "INSERT INTO profiles(user_id, full_name, gender, birthday, age, school, " +
                    "department, major, grade, hometown, height, weight, avatar_url, bio, " +
                    "zodiac_sign, looking_for, relationship_status, personality_type) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFullName());
            ps.setString(3, profile.getGender());
            ps.setDate(4, profile.getBirthday());
            ps.setInt(5, profile.getAge());
            ps.setString(6, profile.getSchool());
            ps.setString(7, profile.getDepartment());
            ps.setString(8, profile.getMajor());
            ps.setInt(9, profile.getGrade());
            ps.setString(10, profile.getHometown());
            ps.setDouble(11, profile.getHeight());
            ps.setDouble(12, profile.getWeight());
            ps.setString(13, profile.getAvatarUrl());
            ps.setString(14, profile.getBio());
            ps.setString(15, profile.getZodiacSign());
            ps.setString(16, profile.getLookingFor());
            ps.setString(17, profile.getRelationshipStatus());
            ps.setString(18, profile.getPersonalityType());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 更新个人资料
     * @param profile 个人资料对象
     * @return 成功返回true，失败返回false
     */
    public boolean update(Profile profile) {
        boolean flag = false;
        try {
            String sql = "UPDATE profiles SET full_name=?, gender=?, birthday=?, age=?, school=?, " +
                    "department=?, major=?, grade=?, hometown=?, height=?, weight=?, avatar_url=?, " +
                    "bio=?, zodiac_sign=?, looking_for=?, relationship_status=?, personality_type=? " +
                    "WHERE id=?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, profile.getFullName());
            ps.setString(2, profile.getGender());
            ps.setDate(3, profile.getBirthday());
            ps.setInt(4, profile.getAge());
            ps.setString(5, profile.getSchool());
            ps.setString(6, profile.getDepartment());
            ps.setString(7, profile.getMajor());
            ps.setInt(8, profile.getGrade());
            ps.setString(9, profile.getHometown());
            ps.setDouble(10, profile.getHeight());
            ps.setDouble(11, profile.getWeight());
            ps.setString(12, profile.getAvatarUrl());
            ps.setString(13, profile.getBio());
            ps.setString(14, profile.getZodiacSign());
            ps.setString(15, profile.getLookingFor());
            ps.setString(16, profile.getRelationshipStatus());
            ps.setString(17, profile.getPersonalityType());
            ps.setInt(18, profile.getId());
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 根据ID获取个人资料
     * @param id 个人资料ID
     * @return 个人资料对象，如果不存在返回null
     */
    public Profile findById(int id) {
        Profile profile = null;
        try {
            String sql = "SELECT * FROM profiles WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                profile = mapResultSetToProfile(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }
    
    /**
     * 根据用户ID获取个人资料
     * @param userId 用户ID
     * @return 个人资料对象，如果不存在返回null
     */
    public Profile findByUserId(int userId) {
        Profile profile = null;
        try {
            String sql = "SELECT * FROM profiles WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                profile = mapResultSetToProfile(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }
    
    /**
     * 获取所有个人资料
     * @return 个人资料列表
     */
    public List<Profile> findAll() {
        List<Profile> profiles = new ArrayList<>();
        try {
            String sql = "SELECT * FROM profiles";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Profile profile = mapResultSetToProfile(rs);
                profiles.add(profile);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }
    
    /**
     * 删除个人资料
     * @param id 个人资料ID
     * @return 成功返回true，失败返回false
     */
    public boolean delete(int id) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM profiles WHERE id=?";
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
     * 根据用户ID删除个人资料
     * @param userId 用户ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteByUserId(int userId) {
        boolean flag = false;
        try {
            String sql = "DELETE FROM profiles WHERE user_id=?";
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
     * 根据条件搜索个人资料
     * @param gender 性别
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @param school 学校
     * @param department 院系
     * @return 符合条件的个人资料列表
     */
    public List<Profile> searchProfiles(String gender, int minAge, int maxAge, String school, String department) {
        List<Profile> profiles = new ArrayList<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM profiles WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            if (gender != null && !gender.isEmpty()) {
                sqlBuilder.append(" AND gender=?");
                params.add(gender);
            }
            
            if (minAge > 0) {
                sqlBuilder.append(" AND age>=?");
                params.add(minAge);
            }
            
            if (maxAge > 0) {
                sqlBuilder.append(" AND age<=?");
                params.add(maxAge);
            }
            
            if (school != null && !school.isEmpty()) {
                sqlBuilder.append(" AND school LIKE ?");
                params.add("%" + school + "%");
            }
            
            if (department != null && !department.isEmpty()) {
                sqlBuilder.append(" AND department LIKE ?");
                params.add("%" + department + "%");
            }
            
            PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString());
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Profile profile = mapResultSetToProfile(rs);
                profiles.add(profile);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }
    
    /**
     * 更新头像URL
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 成功返回true，失败返回false
     */
    public boolean updateAvatarUrl(int userId, String avatarUrl) {
        boolean flag = false;
        try {
            String sql = "UPDATE profiles SET avatar_url=? WHERE user_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, avatarUrl);
            ps.setInt(2, userId);
            
            flag = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    /**
     * 将ResultSet映射为Profile对象
     * @param rs ResultSet
     * @return Profile对象
     * @throws SQLException SQLException
     */
    private Profile mapResultSetToProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getInt("id"));
        profile.setUserId(rs.getInt("user_id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setGender(rs.getString("gender"));
        profile.setBirthday(rs.getDate("birthday"));
        profile.setAge(rs.getInt("age"));
        profile.setSchool(rs.getString("school"));
        profile.setDepartment(rs.getString("department"));
        profile.setMajor(rs.getString("major"));
        profile.setGrade(rs.getInt("grade"));
        profile.setHometown(rs.getString("hometown"));
        profile.setHeight(rs.getDouble("height"));
        profile.setWeight(rs.getDouble("weight"));
        profile.setAvatarUrl(rs.getString("avatar_url"));
        profile.setBio(rs.getString("bio"));
        profile.setZodiacSign(rs.getString("zodiac_sign"));
        profile.setLookingFor(rs.getString("looking_for"));
        profile.setRelationshipStatus(rs.getString("relationship_status"));
        profile.setPersonalityType(rs.getString("personality_type"));
        return profile;
    }
}