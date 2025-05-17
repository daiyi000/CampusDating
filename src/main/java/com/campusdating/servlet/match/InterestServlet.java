package com.campusdating.servlet.match;

import com.campusdating.model.Interest;
import com.campusdating.model.User;
import com.campusdating.model.UserInterest;
import com.campusdating.service.InterestService;
import com.campusdating.service.UserInterestService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 兴趣Servlet
 * 处理用户兴趣相关请求
 */
@WebServlet("/interest")
public class InterestServlet extends HttpServlet {

    private InterestService interestService;
    private UserInterestService userInterestService;
    
    @Override
    public void init() {
        interestService = new InterestService();
        userInterestService = new UserInterestService();
    }
    
    /**
     * 处理GET请求，显示兴趣页面
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
            session.setAttribute("redirectURL", "interest");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取所有兴趣类别
        List<String> categories = interestService.getAllCategories();
        
        // 获取所有兴趣
        List<Interest> allInterests = interestService.getAllInterests();
        
        // 获取用户的兴趣
        List<Interest> userInterests = interestService.getUserInterests(user.getId());
        
        // 将兴趣信息放入请求属性
        request.setAttribute("categories", categories);
        request.setAttribute("allInterests", allInterests);
        request.setAttribute("userInterests", userInterests);
        
        // 转发到兴趣页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("interests.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理兴趣操作
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
        
        // 获取请求参数
        String action = request.getParameter("action");
        
        // 处理添加兴趣请求
        if ("add".equals(action)) {
            String[] interestIds = request.getParameterValues("interests");
            
            if (interestIds != null && interestIds.length > 0) {
                List<Integer> ids = new ArrayList<>();
                for (String idStr : interestIds) {
                    try {
                        int id = Integer.parseInt(idStr);
                        ids.add(id);
                    } catch (NumberFormatException e) {
                        // 忽略无效的ID
                    }
                }
                
                // 添加用户兴趣
                int successCount = userInterestService.addUserInterests(user.getId(), ids);
                
                // 设置操作结果
                if (successCount > 0) {
                    request.setAttribute("successMessage", "成功添加 " + successCount + " 个兴趣");
                } else {
                    request.setAttribute("errorMessage", "添加兴趣失败，请稍后再试");
                }
            }
        }
        // 处理移除兴趣请求
        else if ("remove".equals(action)) {
            String interestIdStr = request.getParameter("interestId");
            
            if (interestIdStr != null && !interestIdStr.isEmpty()) {
                try {
                    int interestId = Integer.parseInt(interestIdStr);
                    
                    // 移除用户兴趣
                    boolean success = userInterestService.removeUserInterest(user.getId(), interestId);
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "已成功移除兴趣");
                    } else {
                        request.setAttribute("errorMessage", "移除兴趣失败，请稍后再试");
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的兴趣ID
                }
            }
        }
        // 处理更新兴趣等级请求
        else if ("updateLevel".equals(action)) {
            String interestIdStr = request.getParameter("interestId");
            String levelStr = request.getParameter("level");
            
            if (interestIdStr != null && !interestIdStr.isEmpty() && levelStr != null && !levelStr.isEmpty()) {
                try {
                    int interestId = Integer.parseInt(interestIdStr);
                    int level = Integer.parseInt(levelStr);
                    
                    // 更新用户兴趣等级
                    boolean success = userInterestService.updateInterestLevel(user.getId(), interestId, level);
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "已成功更新兴趣等级");
                    } else {
                        request.setAttribute("errorMessage", "更新兴趣等级失败，请稍后再试");
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的参数
                }
            }
        }
        
        // 重定向到兴趣页面
        response.sendRedirect("interest");
    }
}