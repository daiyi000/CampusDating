package com.campusdating.servlet.user;

import com.campusdating.model.Profile;
import com.campusdating.model.User;
import com.campusdating.service.ProfileService;
import com.campusdating.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 个人资料Servlet
 * 处理用户个人资料相关请求
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private ProfileService profileService;
    private UserService userService;
    
    @Override
    public void init() {
        profileService = new ProfileService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示个人资料
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取当前会话
        HttpSession session = request.getSession(false);
        
        // 检查用户是否已登录
        if (session == null || session.getAttribute("user") == null) {
            // 保存请求的URL
            session = request.getSession(true);
            session.setAttribute("redirectURL", "profile");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取用户个人资料
        Profile profile = profileService.getProfileByUserId(user.getId());
        
        // 检查是否指定查看其他用户的个人资料
        String userIdParam = request.getParameter("id");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdParam);
                // 如果不是当前用户，获取指定用户的个人资料
                if (userId != user.getId()) {
                    User viewUser = userService.getUserById(userId);
                    if (viewUser != null) {
                        profile = profileService.getProfileByUserId(userId);
                        request.setAttribute("viewUser", viewUser);
                    }
                }
            } catch (NumberFormatException e) {
                // 忽略无效的用户ID
            }
        }
        
        // 将个人资料放入请求属性
        request.setAttribute("profile", profile);
        
        // 转发到个人资料页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理编辑个人资料请求
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取当前会话
        HttpSession session = request.getSession(false);
        
        // 检查用户是否已登录
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取用户个人资料
        Profile profile = profileService.getProfileByUserId(user.getId());
        
        // 如果个人资料不存在，创建新的个人资料
        if (profile == null) {
            profile = new Profile();
            profile.setUserId(user.getId());
        }
        
        // 获取请求参数并设置个人资料属性
        String action = request.getParameter("action");
        
        // 编辑个人资料操作
        if ("edit".equals(action)) {
            // 转发到编辑个人资料页面
            request.setAttribute("profile", profile);
            RequestDispatcher dispatcher = request.getRequestDispatcher("editProfile.jsp");
            dispatcher.forward(request, response);
        }
        // 保存个人资料操作
        else if ("save".equals(action)) {
            // 获取表单数据
            String fullName = request.getParameter("fullName");
            String gender = request.getParameter("gender");
            String birthdayStr = request.getParameter("birthday");
            String school = request.getParameter("school");
            String department = request.getParameter("department");
            String major = request.getParameter("major");
            String gradeStr = request.getParameter("grade");
            String hometown = request.getParameter("hometown");
            String heightStr = request.getParameter("height");
            String weightStr = request.getParameter("weight");
            String bio = request.getParameter("bio");
            String zodiacSign = request.getParameter("zodiacSign");
            String lookingFor = request.getParameter("lookingFor");
            String relationshipStatus = request.getParameter("relationshipStatus");
            String personalityType = request.getParameter("personalityType");
            
            // 设置个人资料属性
            profile.setFullName(fullName);
            profile.setGender(gender);
            profile.setSchool(school);
            profile.setDepartment(department);
            profile.setMajor(major);
            profile.setHometown(hometown);
            profile.setBio(bio);
            profile.setZodiacSign(zodiacSign);
            profile.setLookingFor(lookingFor);
            profile.setRelationshipStatus(relationshipStatus);
            profile.setPersonalityType(personalityType);
            
            // 转换并设置生日
            try {
                if (birthdayStr != null && !birthdayStr.isEmpty()) {
                    java.sql.Date birthday = java.sql.Date.valueOf(birthdayStr);
                    profile.setBirthday(birthday);
                }
            } catch (IllegalArgumentException e) {
                // 忽略无效的生日格式
            }
            
            // 转换并设置年级
            try {
                if (gradeStr != null && !gradeStr.isEmpty()) {
                    int grade = Integer.parseInt(gradeStr);
                    profile.setGrade(grade);
                }
            } catch (NumberFormatException e) {
                // 忽略无效的年级格式
            }
            
            // 转换并设置身高
            try {
                if (heightStr != null && !heightStr.isEmpty()) {
                    double height = Double.parseDouble(heightStr);
                    profile.setHeight(height);
                }
            } catch (NumberFormatException e) {
                // 忽略无效的身高格式
            }
            
            // 转换并设置体重
            try {
                if (weightStr != null && !weightStr.isEmpty()) {
                    double weight = Double.parseDouble(weightStr);
                    profile.setWeight(weight);
                }
            } catch (NumberFormatException e) {
                // 忽略无效的体重格式
            }
            
            // 保存个人资料
            boolean success = profileService.saveOrUpdateProfile(profile);
            
            // 设置操作结果
            if (success) {
                request.setAttribute("successMessage", "个人资料保存成功");
            } else {
                request.setAttribute("errorMessage", "个人资料保存失败");
            }
            
            // 重定向到个人资料页面
            response.sendRedirect("profile");
        }
        // 默认操作，显示个人资料
        else {
            // 将个人资料放入请求属性
            request.setAttribute("profile", profile);
            // 转发到个人资料页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
            dispatcher.forward(request, response);
        }
    }
}