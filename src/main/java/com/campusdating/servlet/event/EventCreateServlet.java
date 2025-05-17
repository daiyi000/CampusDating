package com.campusdating.servlet.event;

import com.campusdating.model.Event;
import com.campusdating.model.User;
import com.campusdating.service.EventService;
import com.campusdating.util.DateUtil;
import com.campusdating.util.FileUploadUtil;
import com.campusdating.util.NotificationUtil;
import com.campusdating.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 创建活动Servlet
 * 处理创建活动相关请求
 */
@WebServlet("/createEvent")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 5 * 1024 * 1024,   // 5 MB
    maxRequestSize = 10 * 1024 * 1024 // 10 MB
)
public class EventCreateServlet extends HttpServlet {

    private EventService eventService;
    
    @Override
    public void init() {
        eventService = new EventService();
    }
    
    /**
     * 处理GET请求，显示创建活动页面
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
            session.setAttribute("redirectURL", "createEvent");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取所有活动类型
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        // 转发到创建活动页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("createEvent.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理创建活动
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
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String eventDateStr = request.getParameter("eventDate");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String eventType = request.getParameter("eventType");
        String maxParticipantsStr = request.getParameter("maxParticipants");
        String isPublicStr = request.getParameter("isPublic");
        String latitudeStr = request.getParameter("latitude");
        String longitudeStr = request.getParameter("longitude");
        
        // 参数验证
        boolean hasError = false;
        
        // 验证标题
        if (ValidationUtil.isEmpty(title)) {
            request.setAttribute("titleError", "标题不能为空");
            hasError = true;
        }
        
        // 验证位置
        if (ValidationUtil.isEmpty(location)) {
            request.setAttribute("locationError", "位置不能为空");
            hasError = true;
        }
        
        // 验证日期和时间
        if (ValidationUtil.isEmpty(eventDateStr)) {
            request.setAttribute("eventDateError", "活动日期不能为空");
            hasError = true;
        }
        
        if (ValidationUtil.isEmpty(startTimeStr)) {
            request.setAttribute("startTimeError", "开始时间不能为空");
            hasError = true;
        }
        
        if (ValidationUtil.isEmpty(endTimeStr)) {
            request.setAttribute("endTimeError", "结束时间不能为空");
            hasError = true;
        }
        
        // 验证活动类型
        if (ValidationUtil.isEmpty(eventType)) {
            request.setAttribute("eventTypeError", "活动类型不能为空");
            hasError = true;
        }
        
        // 验证最大参与人数
        if (ValidationUtil.isEmpty(maxParticipantsStr)) {
            request.setAttribute("maxParticipantsError", "最大参与人数不能为空");
            hasError = true;
        } else {
            try {
                int maxParticipants = Integer.parseInt(maxParticipantsStr);
                if (maxParticipants <= 0) {
                    request.setAttribute("maxParticipantsError", "最大参与人数必须大于0");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("maxParticipantsError", "最大参与人数必须是有效的数字");
                hasError = true;
            }
        }
        
        // 如果有错误，重新显示创建活动页面
        if (hasError) {
            // 保存用户输入的数据
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("location", location);
            request.setAttribute("eventDate", eventDateStr);
            request.setAttribute("startTime", startTimeStr);
            request.setAttribute("endTime", endTimeStr);
            request.setAttribute("eventType", eventType);
            request.setAttribute("maxParticipants", maxParticipantsStr);
            request.setAttribute("isPublic", isPublicStr);
            request.setAttribute("latitude", latitudeStr);
            request.setAttribute("longitude", longitudeStr);
            
            // 获取所有活动类型
            List<String> eventTypes = eventService.getAllEventTypes();
            request.setAttribute("eventTypes", eventTypes);
            
            // 转发到创建活动页面
            RequestDispatcher dispatcher = request.getRequestDispatcher("createEvent.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        try {
            // 创建活动对象
            Event event = new Event();
            event.setCreatorId(user.getId());
            event.setTitle(title);
            event.setDescription(description);
            event.setLocation(location);
            
            // 设置活动日期
            Date eventDate = Date.valueOf(eventDateStr);
            event.setEventDate(eventDate);
            
            // 设置开始时间和结束时间
            String startDateTime = eventDateStr + " " + startTimeStr + ":00";
            String endDateTime = eventDateStr + " " + endTimeStr + ":00";
            Timestamp startTime = Timestamp.valueOf(startDateTime);
            Timestamp endTime = Timestamp.valueOf(endDateTime);
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            
            // 设置活动类型
            event.setEventType(eventType);
            
            // 设置最大参与人数
            int maxParticipants = Integer.parseInt(maxParticipantsStr);
            event.setMaxParticipants(maxParticipants);
            
            // 设置当前参与人数为1（创建者自动参加）
            event.setCurrentParticipants(1);
            
            // 设置是否公开
            boolean isPublic = "on".equals(isPublicStr);
            event.setPublic(isPublic);
            
            // 设置创建时间
            event.setCreateTime(new Timestamp(System.currentTimeMillis()));
            
            // 设置活动状态为活动
            event.setActive(true);
            
            // 设置经纬度
            if (ValidationUtil.isNotEmpty(latitudeStr) && ValidationUtil.isNotEmpty(longitudeStr)) {
                try {
                    double latitude = Double.parseDouble(latitudeStr);
                    double longitude = Double.parseDouble(longitudeStr);
                    event.setLatitude(latitude);
                    event.setLongitude(longitude);
                } catch (NumberFormatException e) {
                    // 忽略无效的经纬度
                }
            }
            
            // 处理活动图片上传
            Part filePart = request.getPart("eventImage");
            if (filePart != null && filePart.getSize() > 0) {
                // 获取上传目录的实际路径
                String realPath = request.getServletContext().getRealPath("/");
                
                // 上传活动图片
                String imageUrl = FileUploadUtil.uploadEventImage(filePart, realPath);
                
                // 设置活动图片URL
                if (imageUrl != null) {
                    event.setImageUrl(imageUrl);
                }
            }
            
            // 创建活动
            boolean success = eventService.createEvent(event);
            
            // 如果创建成功，自动将创建者添加为参与者
            if (success) {
                eventService.joinEvent(event.getId(), user.getId());
                
                // 发送活动通知
                if (isPublic) {
                    // 获取所有用户，除了创建者自己
                    List<Integer> userIds = eventService.getAllUserIds();
                    userIds.remove(Integer.valueOf(user.getId()));
                    
                    if (!userIds.isEmpty()) {
                        // 发送通知
                        NotificationUtil.sendBatchEventNotifications(userIds, event.getTitle(), event.getId());
                    }
                }
                
                // 重定向到活动详情页面
                response.sendRedirect("eventDetails?id=" + event.getId());
                return;
            } else {
                request.setAttribute("errorMessage", "创建活动失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "创建活动失败：" + e.getMessage());
        }
        
        // 如果创建失败，重新显示创建活动页面
        // 保存用户输入的数据
        request.setAttribute("title", title);
        request.setAttribute("description", description);
        request.setAttribute("location", location);
        request.setAttribute("eventDate", eventDateStr);
        request.setAttribute("startTime", startTimeStr);
        request.setAttribute("endTime", endTimeStr);
        request.setAttribute("eventType", eventType);
        request.setAttribute("maxParticipants", maxParticipantsStr);
        request.setAttribute("isPublic", isPublicStr);
        request.setAttribute("latitude", latitudeStr);
        request.setAttribute("longitude", longitudeStr);
        
        // 获取所有活动类型
        List<String> eventTypes = eventService.getAllEventTypes();
        request.setAttribute("eventTypes", eventTypes);
        
        // 转发到创建活动页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("createEvent.jsp");
        dispatcher.forward(request, response);
    }
}