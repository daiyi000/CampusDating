package com.campusdating.servlet.event;

import com.campusdating.model.Event;
import com.campusdating.model.User;
import com.campusdating.service.EventService;
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
import java.util.List;

/**
 * 活动详情Servlet
 * 处理活动详情相关请求
 */
@WebServlet("/eventDetails")
public class EventDetailsServlet extends HttpServlet {

    private EventService eventService;
    private UserService userService;
    
    @Override
    public void init() {
        eventService = new EventService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示活动详情
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
            session.setAttribute("redirectURL", "eventDetails");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取活动ID参数
        String eventIdParam = request.getParameter("id");
        
        // 验证活动ID
        if (ValidationUtil.isEmpty(eventIdParam)) {
            response.sendRedirect("events");
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventIdParam);
            
            // 获取活动详情
            Event event = eventService.getEventById(eventId);
            
            // 检查活动是否存在
            if (event == null) {
                response.sendRedirect("events");
                return;
            }
            
            // 获取活动创建者信息
            User creator = userService.getUserById(event.getCreatorId());
            
            // 获取活动参与者列表
            List<User> participants = eventService.getEventParticipants(eventId);
            
            // 检查当前用户是否已参与该活动
            boolean isParticipated = eventService.isUserParticipated(eventId, user.getId());
            
            // 检查当前用户是否是创建者
            boolean isCreator = event.getCreatorId() == user.getId();
            
            // 将活动详情和相关信息放入请求属性
            request.setAttribute("event", event);
            request.setAttribute("creator", creator);
            request.setAttribute("participants", participants);
            request.setAttribute("isParticipated", isParticipated);
            request.setAttribute("isCreator", isCreator);
            
            // 转发到活动详情页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("eventDetails.jsp");
            dispatcher.forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("events");
        }
    }
    
    /**
     * 处理POST请求，处理活动参与和取消参与
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
        
        // 获取活动ID参数
        String eventIdParam = request.getParameter("eventId");
        
        // 获取操作类型
        String action = request.getParameter("action");
        
        // 验证参数
        if (ValidationUtil.isEmpty(eventIdParam) || ValidationUtil.isEmpty(action)) {
            response.sendRedirect("events");
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventIdParam);
            
            // 获取活动详情
            Event event = eventService.getEventById(eventId);
            
            // 检查活动是否存在
            if (event == null) {
                response.sendRedirect("events");
                return;
            }
            
            // 处理参与活动请求
            if ("join".equals(action)) {
                // 检查活动是否已满
                if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
                    request.setAttribute("errorMessage", "活动已满，无法参与");
                } else {
                    // 参与活动
                    boolean success = eventService.joinEvent(eventId, user.getId());
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "已成功参与活动");
                    } else {
                        request.setAttribute("errorMessage", "参与活动失败，请稍后再试");
                    }
                }
            }
            // 处理取消参与活动请求
            else if ("leave".equals(action)) {
                // 取消参与活动
                boolean success = eventService.leaveEvent(eventId, user.getId());
                
                // 设置操作结果
                if (success) {
                    request.setAttribute("successMessage", "已成功取消参与活动");
                } else {
                    request.setAttribute("errorMessage", "取消参与活动失败，请稍后再试");
                }
            }
            // 处理删除活动请求
            else if ("delete".equals(action)) {
                // 检查当前用户是否是创建者
                if (event.getCreatorId() == user.getId()) {
                    // 删除活动
                    boolean success = eventService.deleteEvent(eventId);
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "已成功删除活动");
                        // 重定向到活动列表页面
                        response.sendRedirect("events");
                        return;
                    } else {
                        request.setAttribute("errorMessage", "删除活动失败，请稍后再试");
                    }
                } else {
                    request.setAttribute("errorMessage", "只有活动创建者才能删除活动");
                }
            }
            
            // 重定向到活动详情页面
            response.sendRedirect("eventDetails?id=" + eventId);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("events");
        }
    }
}