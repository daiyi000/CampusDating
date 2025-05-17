package com.campusdating.model;

/**
 * 兴趣爱好实体类
 * 用于存储系统中所有可用的兴趣爱好选项
 */
public class Interest {
    private int id;
    private String name;
    private String category; // sports, music, art, etc.
    private String description;
    private String iconUrl;
    
    // 默认构造函数
    public Interest() {
    }
    
    // 完整参数构造函数
    public Interest(int id, String name, String category, String description, String iconUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.iconUrl = iconUrl;
    }
    
    // 基本兴趣构造函数
    public Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Interest interest = (Interest) o;
        
        if (id != 0 && interest.id != 0) {
            return id == interest.id;
        }
        
        // 如果ID为0（未保存到数据库），则比较名称和类别
        if (name != null ? !name.equals(interest.name) : interest.name != null) return false;
        return category != null ? category.equals(interest.category) : interest.category == null;
    }

    @Override
    public int hashCode() {
        if (id != 0) {
            return id;
        }
        
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}