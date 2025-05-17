package com.campusdating.servlet.user;

import com.campusdating.model.User;
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

/**
 * 登录Servlet
 * 处理用户登录请求
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    
    @Override
    public void init() {
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示登录页面
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // 如果用户已登录，重定向到首页
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        // 转发到登录页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理用户登录
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 参数验证
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            request.setAttribute("errorMessage", "用户名和密码不能为空");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // 尝试登录
        User user = userService.login(username, password);
        
        // 登录成功
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // 检查是否有重定向URL
            String redirectURL = (String) session.getAttribute("redirectURL");
            if (redirectURL != null) {
                session.removeAttribute("redirectURL");
                response.sendRedirect(redirectURL);
            } else {
                response.sendRedirect("index.jsp");
            }
        } 
        // 登录失败
        else {
            request.setAttribute("errorMessage", "用户名或密码错误");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
}