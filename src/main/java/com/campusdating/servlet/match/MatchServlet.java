package com.campusdating.servlet.match;

import com.campusdating.model.Match;
import com.campusdating.model.Profile;
import com.campusdating.model.User;
import com.campusdating.service.MatchService;
import com.campusdating.service.ProfileService;
import com.campusdating.service.UserService;
import com.campusdating.util.NotificationUtil;

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
 * 匹配Servlet
 * 处理用户匹配相关请求
 */
@WebServlet("/match")
public class MatchServlet extends HttpServlet {

    private MatchService matchService;
    private UserService userService;
    private ProfileService profileService;
    
    @Override
    public void init() {
        matchService = new MatchService();
        userService = new UserService();
        profileService = new ProfileService();
    }
    
    /**
     * 处理GET请求，显示匹配页面
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
            session.setAttribute("redirectURL", "match");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 检查是否有匹配ID参数，显示特定匹配
        String matchIdParam = request.getParameter("id");
        if (matchIdParam != null && !matchIdParam.isEmpty()) {
            try {
                int matchId = Integer.parseInt(matchIdParam);
                Match match = matchService.getMatchById(matchId);
                
                // 检查匹配是否存在且当前用户是匹配的一方
                if (match != null && match.involvesUser(user.getId())) {
                    // 获取匹配对象的用户信息
                    int otherUserId = match.getOtherUserId(user.getId());
                    User otherUser = userService.getUserById(otherUserId);
                    Profile otherProfile = profileService.getProfileByUserId(otherUserId);
                    
                    // 将匹配和用户信息放入请求属性
                    request.setAttribute("match", match);
                    request.setAttribute("otherUser", otherUser);
                    request.setAttribute("otherProfile", otherProfile);
                    
                    // 转发到匹配详情页面
                    RequestDispatcher dispatcher = request.getRequestDispatcher("matchDetails.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // 忽略无效的匹配ID
            }
        }
        
        // 获取用户的所有匹配
        List<Match> matches = matchService.getUserMatches(user.getId());
        
        // 获取匹配用户的信息
        Map<Integer, User> matchUsers = new HashMap<>();
        Map<Integer, Profile> matchProfiles = new HashMap<>();
        
        for (Match match : matches) {
            int otherUserId = match.getOtherUserId(user.getId());
            User otherUser = userService.getUserById(otherUserId);
            Profile otherProfile = profileService.getProfileByUserId(otherUserId);
            
            if (otherUser != null) {
                matchUsers.put(otherUserId, otherUser);
            }
            
            if (otherProfile != null) {
                matchProfiles.put(otherUserId, otherProfile);
            }
        }
        
        // 将匹配列表和用户信息放入请求属性
        request.setAttribute("matches", matches);
        request.setAttribute("matchUsers", matchUsers);
        request.setAttribute("matchProfiles", matchProfiles);
        
        // 转发到匹配列表页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("matches.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理匹配操作
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
        String matchIdParam = request.getParameter("matchId");
        String userIdParam = request.getParameter("userId");
        
        // 处理接受匹配请求
        if ("accept".equals(action) && matchIdParam != null && !matchIdParam.isEmpty()) {
            try {
                int matchId = Integer.parseInt(matchIdParam);
                Match match = matchService.getMatchById(matchId);
                
                // 检查匹配是否存在且当前用户是匹配的一方
                if (match != null && match.involvesUser(user.getId())) {
                    // 接受匹配
                    boolean success = matchService.acceptMatch(matchId);
                    
                    // 如果成功接受匹配，发送通知
                    if (success) {
                        int otherUserId = match.getOtherUserId(user.getId());
                        User otherUser = userService.getUserById(otherUserId);
                        
                        if (otherUser != null) {
                            NotificationUtil.sendMatchNotification(otherUserId, user.getUsername(), matchId, user.getId());
                        }
                    }
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "匹配已接受");
                    } else {
                        request.setAttribute("errorMessage", "操作失败，请稍后再试");
                    }
                }
            } catch (NumberFormatException e) {
                // 忽略无效的匹配ID
            }
        }
        // 处理拒绝匹配请求
        else if ("reject".equals(action) && matchIdParam != null && !matchIdParam.isEmpty()) {
            try {
                int matchId = Integer.parseInt(matchIdParam);
                Match match = matchService.getMatchById(matchId);
                
                // 检查匹配是否存在且当前用户是匹配的一方
                if (match != null && match.involvesUser(user.getId())) {
                    // 拒绝匹配
                    boolean success = matchService.rejectMatch(matchId);
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "匹配已拒绝");
                    } else {
                        request.setAttribute("errorMessage", "操作失败，请稍后再试");
                    }
                }
            } catch (NumberFormatException e) {
                // 忽略无效的匹配ID
            }
        }
        // 处理创建匹配请求
        else if ("create".equals(action) && userIdParam != null && !userIdParam.isEmpty()) {
            try {
                int targetUserId = Integer.parseInt(userIdParam);
                User targetUser = userService.getUserById(targetUserId);
                
                // 检查目标用户是否存在且不是当前用户
                if (targetUser != null && targetUser.getId() != user.getId()) {
                    // 创建匹配
                    Match match = new Match(user.getId(), targetUserId, 0);
                    boolean success = matchService.createMatch(match);
                    
                    // 如果成功创建匹配，发送通知
                    if (success) {
                        NotificationUtil.sendMatchNotification(targetUserId, user.getUsername(), match.getId(), user.getId());
                    }
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "匹配请求已发送");
                    } else {
                        request.setAttribute("errorMessage", "操作失败，请稍后再试");
                    }
                }
            } catch (NumberFormatException e) {
                // 忽略无效的用户ID
            }
        }
        
        // 重定向到匹配列表页面
        response.sendRedirect("match");
    }
}