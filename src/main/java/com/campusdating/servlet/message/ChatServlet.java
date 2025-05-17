package com.campusdating.servlet.message;

import com.campusdating.model.Message;
import com.campusdating.model.User;
import com.campusdating.service.MessageService;
import com.campusdating.service.UserService;
import com.campusdating.util.NotificationUtil;
import com.campusdating.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * 聊天Servlet - 使用自定义JSON处理而不是org.json库
 * 处理聊天相关的AJAX请求
 */
@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private MessageService messageService;
    private UserService userService;
    
    @Override
    public void init() {
        messageService = new MessageService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，获取与特定用户的消息
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置响应类型为JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 获取当前会话
        HttpSession session = request.getSession(false);
        
        // 检查用户是否已登录
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取伙伴ID参数
        String partnerIdParam = request.getParameter("partnerId");
        
        // 获取上次消息ID参数（用于增量加载）
        String lastMessageIdParam = request.getParameter("lastMessageId");
        
        // 如果未指定伙伴ID，返回错误
        if (ValidationUtil.isEmpty(partnerIdParam)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int partnerId = Integer.parseInt(partnerIdParam);
            int lastMessageId = 0;
            
            if (ValidationUtil.isNotEmpty(lastMessageIdParam)) {
                try {
                    lastMessageId = Integer.parseInt(lastMessageIdParam);
                } catch (NumberFormatException e) {
                    // 忽略无效的消息ID
                }
            }
            
            // 获取消息列表
            List<Message> messages;
            if (lastMessageId > 0) {
                // 获取新消息
                messages = messageService.getNewMessagesBetweenUsers(user.getId(), partnerId, lastMessageId);
            } else {
                // 获取所有消息
                messages = messageService.getMessagesBetweenUsers(user.getId(), partnerId);
            }
            
            // 将消息标记为已读
            messageService.markMessagesAsRead(partnerId, user.getId());
            
            // 将消息转换为JSON数组
            StringBuilder jsonBuilder = new StringBuilder("[");
            boolean first = true;
            
            for (Message message : messages) {
                if (!first) {
                    jsonBuilder.append(",");
                }
                first = false;
                
                // 构建JSON对象
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(message.getId()).append(",");
                jsonBuilder.append("\"senderId\":").append(message.getSenderId()).append(",");
                jsonBuilder.append("\"receiverId\":").append(message.getReceiverId()).append(",");
                jsonBuilder.append("\"content\":\"").append(escapeJsonString(message.getContent())).append("\",");
                jsonBuilder.append("\"sendTime\":\"").append(message.getSendTime().toString()).append("\",");
                jsonBuilder.append("\"isRead\":").append(message.isRead()).append(",");
                jsonBuilder.append("\"messageType\":\"").append(escapeJsonString(message.getMessageType())).append("\",");
                jsonBuilder.append("\"isCurrentUser\":").append(message.getSenderId() == user.getId());
                jsonBuilder.append("}");
            }
            
            jsonBuilder.append("]");
            
            // 将JSON字符串写入响应
            PrintWriter out = response.getWriter();
            out.print(jsonBuilder.toString());
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * 处理POST请求，发送消息
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置响应类型为JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 获取当前会话
        HttpSession session = request.getSession(false);
        
        // 检查用户是否已登录
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取请求参数
        String receiverIdParam = request.getParameter("receiverId");
        String content = request.getParameter("content");
        String messageType = request.getParameter("messageType");
        
        // 默认消息类型为文本
        if (ValidationUtil.isEmpty(messageType)) {
            messageType = "text";
        }
        
        // 验证参数
        if (ValidationUtil.isEmpty(receiverIdParam) || ValidationUtil.isEmpty(content)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        try {
            int receiverId = Integer.parseInt(receiverIdParam);
            User receiver = userService.getUserById(receiverId);
            
            // 检查接收者是否存在
            if (receiver != null) {
                // 创建消息对象
                Message message = new Message();
                message.setSenderId(user.getId());
                message.setReceiverId(receiverId);
                message.setContent(content);
                message.setSendTime(new Timestamp(System.currentTimeMillis()));
                message.setRead(false);
                message.setDeleted(false);
                message.setMessageType(messageType);
                
                // 发送消息
                boolean success = messageService.sendMessage(message);
                
                // 如果成功发送消息，发送通知
                if (success) {
                    NotificationUtil.sendMessageNotification(receiverId, user.getUsername(), message.getId(), user.getId());
                    
                    // 创建响应JSON对象
                    StringBuilder jsonBuilder = new StringBuilder();
                    jsonBuilder.append("{");
                    jsonBuilder.append("\"success\":true,");
                    jsonBuilder.append("\"messageId\":").append(message.getId());
                    jsonBuilder.append("}");
                    
                    // 将JSON字符串写入响应
                    PrintWriter out = response.getWriter();
                    out.print(jsonBuilder.toString());
                    out.flush();
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * 转义JSON字符串中的特殊字符
     * @param input 输入字符串
     * @return 转义后的字符串
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    // 处理控制字符
                    if (ch < ' ') {
                        String hex = "000" + Integer.toHexString(ch);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }
}