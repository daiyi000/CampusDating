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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动列表Servlet
 * 处理活动列表相关请求
 */
@WebServlet("/events")
public class EventListServlet extends HttpServlet {

    private EventService eventService;
    private UserService userService;
    
    @Override
    public void init() {
        eventService = new EventService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示活动列表
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
            session.setAttribute("redirectURL", "events");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取活动类型参数
        String eventType = request.getParameter("type");
        
        // 获取活动来源参数（即将到来的、我创建的、我参与的）
        String source = request.getParameter("source");
        
        // 获取活动列表
        List<Event> events;
        
        if (ValidationUtil.isNotEmpty(eventType)) {
            // 按类型获取活动
            events = eventService.getEventsByType(eventType);
            request.setAttribute("currentType", eventType);
        } else if ("upcoming".equals(source)) {
            // 获取即将到来的活动
            events = eventService.getUpcomingEvents();
            request.setAttribute("currentSource", "upcoming");
        } else if ("created".equals(source)) {
            // 获取用户创建的活动
            events = eventService.getEventsByCreator(user.getId());
            request.setAttribute("currentSource", "created");
        } else if ("participated".equals(source)) {
            // 获取用户参与的活动
            events = eventService.getEventsByParticipant(user.getId());
            request.setAttribute("currentSource", "participated");
        } else {
            // 默认获取所有活动
            events = eventService.getAllEvents();
        }
        
        // 获取活动创建者信息
        Map<Integer, User> creators = new HashMap<>();
        for (Event event : events) {
            int creatorId = event.getCreatorId();
            if (!creators.containsKey(creatorId)) {
                User creator = userService.getUserById(creatorId);
                if (creator != null) {
                    creators.put(creatorId, creator);
                }
            }
        }
        
        // 获取活动类型列表
        List<String> eventTypes = eventService.getAllEventTypes();
        
        // 将活动列表和相关信息放入请求属性
        request.setAttribute("events", events);
        request.setAttribute("creators", creators);
        request.setAttribute("eventTypes", eventTypes);
        request.setAttribute("participatedEvents", eventService.getEventIdsByParticipant(user.getId()));
        
        // 转发到活动列表页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("events.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理活动过滤
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
        
        // 获取过滤参数
        String eventType = request.getParameter("eventType");
        String source = request.getParameter("source");
        
        // 构建重定向URL
        StringBuilder redirectURL = new StringBuilder("events?");
        
        if (ValidationUtil.isNotEmpty(eventType)) {
            redirectURL.append("type=").append(eventType);
        } else if (ValidationUtil.isNotEmpty(source)) {
            redirectURL.append("source=").append(source);
        }
        
        // 重定向到活动列表页面
        response.sendRedirect(redirectURL.toString());
    }
}