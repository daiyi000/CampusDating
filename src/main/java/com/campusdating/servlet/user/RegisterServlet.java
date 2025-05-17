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
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 注册Servlet
 * 处理用户注册请求
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService;
    
    @Override
    public void init() {
        userService = new UserService();
    }
    
    /**
     * 处理GET请求，显示注册页面
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 转发到注册页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * 处理POST请求，处理用户注册
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 获取请求参数
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // 验证参数
        boolean hasError = false;
        
        // 验证用户名
        if (ValidationUtil.isEmpty(username)) {
            request.setAttribute("usernameError", "用户名不能为空");
            hasError = true;
        } else if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("usernameError", "用户名必须以字母开头，只能包含字母、数字和下划线，长度4-16");
            hasError = true;
        }
        
        // 验证邮箱
        if (ValidationUtil.isEmpty(email)) {
            request.setAttribute("emailError", "邮箱不能为空");
            hasError = true;
        } else if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("emailError", "请输入有效的邮箱地址");
            hasError = true;
        }
        
        // 验证密码
        if (ValidationUtil.isEmpty(password)) {
            request.setAttribute("passwordError", "密码不能为空");
            hasError = true;
        } else if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("passwordError", "密码必须包含至少一个字母和一个数字，长度6-20");
            hasError = true;
        }
        
        // 验证确认密码
        if (ValidationUtil.isEmpty(confirmPassword)) {
            request.setAttribute("confirmPasswordError", "确认密码不能为空");
            hasError = true;
        } else if (!ValidationUtil.isPasswordMatch(password, confirmPassword)) {
            request.setAttribute("confirmPasswordError", "两次输入的密码不一致");
            hasError = true;
        }
        
        // 如果有错误，重新显示注册页面
        if (hasError) {
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // 检查用户名和邮箱是否已存在
        if (userService.isUsernameExists(username)) {
            request.setAttribute("usernameError", "用户名已存在");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (userService.isEmailExists(email)) {
            request.setAttribute("emailError", "邮箱已存在");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // 实际密码会在Service层加密
        user.setActive(true);
        user.setRole("user");
        user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
        user.setVerified(false);
        
        // 注册用户
        boolean registerSuccess = userService.register(user);
        
        // 注册成功，重定向到登录页面
        if (registerSuccess) {
            request.setAttribute("successMessage", "注册成功，请登录");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        } 
        // 注册失败
        else {
            request.setAttribute("errorMessage", "注册失败，请稍后再试");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
            dispatcher.forward(request, response);
        }
    }
}