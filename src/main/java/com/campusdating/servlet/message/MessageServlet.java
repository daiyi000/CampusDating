package com.campusdating.servlet.message;

import com.campusdating.model.Message;
import com.campusdating.model.User;
import com.campusdating.service.MessageService;
import com.campusdating.service.UserService;
import com.campusdating.util.NotificationUtil;
import com.campusdating.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息Servlet
 * 处理用户消息相关请求
 */
@WebServlet("/message")
public class MessageServlet extends HttpServlet {

    private MessageService messageService;
    private UserService userService;
    
    @Override
    public void init() {
        messageService = new MessageService();
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示消息页面
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
            session.setAttribute("redirectURL", "message");
            // 重定向到登录页面
            response.sendRedirect("login");
            return;
        }
        
        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        
        // 获取消息伙伴ID参数
        String partnerIdParam = request.getParameter("with");
        
        // 如果指定了消息伙伴，显示与该用户的对话
        if (partnerIdParam != null && !partnerIdParam.isEmpty()) {
            try {
                int partnerId = Integer.parseInt(partnerIdParam);
                User partner = userService.getUserById(partnerId);
                
                // 检查用户是否存在
                if (partner != null) {
                    // 获取与该用户的消息
                    List<Message> messages = messageService.getMessagesBetweenUsers(user.getId(), partnerId);
                    
                    // 将消息伙伴和消息放入请求属性
                    request.setAttribute("partner", partner);
                    request.setAttribute("messages", messages);
                    
                    // 将所有消息标记为已读
                    messageService.markMessagesAsRead(partnerId, user.getId());
                    
                    // 转发到聊天页面
                    RequestDispatcher dispatcher = request.getRequestDispatcher("chat.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                // 忽略无效的用户ID
            }
        }
        
        // 获取消息伙伴列表
        List<Integer> partnerIds = messageService.getMessagePartners(user.getId());
        
        // 获取消息伙伴的用户信息和最后一条消息
        Map<Integer, User> partners = new HashMap<>();
        Map<Integer, Message> lastMessages = new HashMap<>();
        Map<Integer, Integer> unreadCounts = new HashMap<>();
        
        for (Integer partnerId : partnerIds) {
            User partner = userService.getUserById(partnerId);
            if (partner != null) {
                partners.put(partnerId, partner);
                
                // 获取最后一条消息
                Message lastMessage = messageService.getLastMessageBetweenUsers(user.getId(), partnerId);
                if (lastMessage != null) {
                    lastMessages.put(partnerId, lastMessage);
                }
                
                // 获取未读消息数量
                int unreadCount = messageService.getUnreadMessageCount(partnerId, user.getId());
                unreadCounts.put(partnerId, unreadCount);
            }
        }
        
        // 将消息伙伴和消息信息放入请求属性
        request.setAttribute("partners", partners);
        request.setAttribute("lastMessages", lastMessages);
        request.setAttribute("unreadCounts", unreadCounts);
        
        // 转发到消息列表页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("messages.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理发送消息操作
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
        
        // 处理发送消息请求
        if ("send".equals(action)) {
            String receiverIdParam = request.getParameter("receiverId");
            String content = request.getParameter("content");
            String messageType = request.getParameter("messageType");
            
            // 默认消息类型为文本
            if (messageType == null || messageType.isEmpty()) {
                messageType = "text";
            }
            
            // 验证参数
            if (ValidationUtil.isEmpty(receiverIdParam) || ValidationUtil.isEmpty(content)) {
                request.setAttribute("errorMessage", "接收者ID和消息内容不能为空");
                response.sendRedirect("message");
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
                    message.setMessageType(messageType);
                    message.setRead(false);
                    message.setDeleted(false);
                    
                    // 发送消息
                    boolean success = messageService.sendMessage(message);
                    
                    // 如果成功发送消息，发送通知
                    if (success) {
                        NotificationUtil.sendMessageNotification(receiverId, user.getUsername(), message.getId(), user.getId());
                    }
                    
                    // 重定向到与该用户的对话
                    response.sendRedirect("message?with=" + receiverId);
                    return;
                }
            } catch (NumberFormatException e) {
                // 忽略无效的接收者ID
            }
        }
        // 处理删除消息请求
        else if ("delete".equals(action)) {
            String messageIdParam = request.getParameter("messageId");
            
            if (ValidationUtil.isNotEmpty(messageIdParam)) {
                try {
                    int messageId = Integer.parseInt(messageIdParam);
                    
                    // 删除消息
                    boolean success = messageService.deleteMessage(messageId, user.getId());
                    
                    // 设置操作结果
                    if (success) {
                        request.setAttribute("successMessage", "消息已删除");
                    } else {
                        request.setAttribute("errorMessage", "删除消息失败");
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的消息ID
                }
            }
        }
        
        // 获取重定向URL
        String redirectURL = "message";
        String partnerIdParam = request.getParameter("with");
        if (ValidationUtil.isNotEmpty(partnerIdParam)) {
            redirectURL += "?with=" + partnerIdParam;
        }
        
        // 重定向到消息页面
        response.sendRedirect(redirectURL);
    }
}