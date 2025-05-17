package com.campusdating.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登出Servlet
 * 处理用户登出请求
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * 处理GET请求，处理用户登出
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取当前会话，如果不存在则不创建新会话
        HttpSession session = request.getSession(false);
        
        // 如果会话存在，使其无效
        if (session != null) {
            session.invalidate();
        }
        
        // 重定向到登录页面
        response.sendRedirect("login");
    }
    
    /**
     * 处理POST请求，处理用户登出
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // POST请求也调用doGet方法
        doGet(request, response);
    }
}