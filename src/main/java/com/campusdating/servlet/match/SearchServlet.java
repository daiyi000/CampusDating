package com.campusdating.servlet.match;

import com.campusdating.model.Profile;
import com.campusdating.model.User;
import com.campusdating.service.ProfileService;
import com.campusdating.service.UserService;
import com.campusdating.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索Servlet
 * 处理用户搜索相关请求
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private ProfileService profileService;
    private UserService userService;
    
    @Override
    public void init() {
        profileService = new ProfileService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示搜索页面
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
            session.setAttribute("redirectURL", "search");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取搜索参数
        String gender = request.getParameter("gender");
        String minAgeStr = request.getParameter("minAge");
        String maxAgeStr = request.getParameter("maxAge");
        String school = request.getParameter("school");
        String department = request.getParameter("department");
        
        // 处理搜索请求
        if (gender != null || minAgeStr != null || maxAgeStr != null || school != null || department != null) {
            // 解析年龄参数
            int minAge = 0;
            int maxAge = 100;
            
            if (minAgeStr != null && !minAgeStr.isEmpty()) {
                try {
                    minAge = Integer.parseInt(minAgeStr);
                } catch (NumberFormatException e) {
                    // 使用默认值
                }
            }
            
            if (maxAgeStr != null && !maxAgeStr.isEmpty()) {
                try {
                    maxAge = Integer.parseInt(maxAgeStr);
                } catch (NumberFormatException e) {
                    // 使用默认值
                }
            }
            
            // 搜索个人资料
            List<Profile> profiles = profileService.searchProfiles(gender, minAge, maxAge, school, department);
            
            // 过滤掉当前用户的个人资料
            List<Profile> filteredProfiles = new ArrayList<>();
            for (Profile profile : profiles) {
                if (profile.getUserId() != user.getId()) {
                    filteredProfiles.add(profile);
                }
            }
            
            // 获取用户信息
            Map<Integer, User> users = new HashMap<>();
            for (Profile profile : filteredProfiles) {
                User profileUser = userService.getUserById(profile.getUserId());
                if (profileUser != null) {
                    users.put(profile.getUserId(), profileUser);
                }
            }
            
            // 将搜索结果放入请求属性
            request.setAttribute("profiles", filteredProfiles);
            request.setAttribute("users", users);
            request.setAttribute("searchPerformed", true);
            
            // 保存搜索参数供前端使用
            request.setAttribute("gender", gender);
            request.setAttribute("minAge", minAgeStr);
            request.setAttribute("maxAge", maxAgeStr);
            request.setAttribute("school", school);
            request.setAttribute("department", department);
        }
        
        // 转发到搜索页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理搜索操作
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
        
        // 获取搜索参数
        String gender = request.getParameter("gender");
        String minAgeStr = request.getParameter("minAge");
        String maxAgeStr = request.getParameter("maxAge");
        String school = request.getParameter("school");
        String department = request.getParameter("department");
        
        // 构建重定向URL
        StringBuilder redirectURL = new StringBuilder("search?");
        
        if (gender != null && !gender.isEmpty()) {
            redirectURL.append("gender=").append(gender).append("&");
        }
        
        if (minAgeStr != null && !minAgeStr.isEmpty() && ValidationUtil.isInteger(minAgeStr)) {
            redirectURL.append("minAge=").append(minAgeStr).append("&");
        }
        
        if (maxAgeStr != null && !maxAgeStr.isEmpty() && ValidationUtil.isInteger(maxAgeStr)) {
            redirectURL.append("maxAge=").append(maxAgeStr).append("&");
        }
        
        if (school != null && !school.isEmpty()) {
            redirectURL.append("school=").append(school).append("&");
        }
        
        if (department != null && !department.isEmpty()) {
            redirectURL.append("department=").append(department).append("&");
        }
        
        // 移除最后一个&字符
        if (redirectURL.charAt(redirectURL.length() - 1) == '&') {
            redirectURL.deleteCharAt(redirectURL.length() - 1);
        }
        
        // 重定向到搜索结果页面
        response.sendRedirect(redirectURL.toString());
    }
}