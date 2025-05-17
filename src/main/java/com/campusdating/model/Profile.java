package com.campusdating.model;

import java.sql.Date;

/**
 * 用户个人资料实体类
 * 存储用户的详细个人信息
 */
public class Profile {
    private int id;
    private int userId;
    private String fullName;
    private String gender;
    private Date birthday;
    private int age;
    private String school;
    private String department;
    private String major;
    private int grade;
    private String hometown;
    private double height;
    private double weight;
    private String avatarUrl;
    private String bio;
    private String zodiacSign;
    private String lookingFor;
    private String relationshipStatus;
    private String personalityType;
    
    // 默认构造函数
    public Profile() {
    }
    
    // 完整参数构造函数
    public Profile(int id, int userId, String fullName, String gender, Date birthday, 
                   int age, String school, String department, String major, int grade, 
                   String hometown, double height, double weight, String avatarUrl, 
                   String bio, String zodiacSign, String lookingFor, 
                   String relationshipStatus, String personalityType) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.age = age;
        this.school = school;
        this.department = department;
        this.major = major;
        this.grade = grade;
        this.hometown = hometown;
        this.height = height;
        this.weight = weight;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.zodiacSign = zodiacSign;
        this.lookingFor = lookingFor;
        this.relationshipStatus = relationshipStatus;
        this.personalityType = personalityType;
    }
    
    // 基本资料构造函数
    public Profile(int userId, String fullName, String gender, Date birthday, 
                  String school, String department) {
        this.userId = userId;
        this.fullName = fullName;
        this.gender = gender;
        this.birthday = birthday;
        this.school = school;
        this.department = department;
        
        // 根据生日计算年龄
        if (birthday != null) {
            long milliseconds = System.currentTimeMillis() - birthday.getTime();
            this.age = (int) (milliseconds / (1000L * 60 * 60 * 24 * 365));
        }
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        // 更新年龄
        if (birthday != null) {
            long milliseconds = System.currentTimeMillis() - birthday.getTime();
            this.age = (int) (milliseconds / (1000L * 60 * 60 * 24 * 365));
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getZodiacSign() {
        return zodiacSign;
    }

    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getPersonalityType() {
        return personalityType;
    }

    public void setPersonalityType(String personalityType) {
        this.personalityType = personalityType;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", school='" + school + '\'' +
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                '}';
    }
}