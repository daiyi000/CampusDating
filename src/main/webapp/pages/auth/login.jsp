<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园约会平台 - 登录</title>
    
    <!-- CSS文件 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
    
    <!-- Font Awesome图标 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-panel">
            <div class="auth-content">
                <div class="auth-header">
                    <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
                        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="校园约会平台">
                        <span>校园约会平台</span>
                    </a>
                </div>
                
                <div class="auth-form">
                    <h2>登录</h2>
                    <p>欢迎回来，请登录您的账号</p>
                    
                    <!-- 显示成功消息 -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">
                            ${successMessage}
                        </div>
                    </c:if>
                    
                    <!-- 显示错误消息 -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">
                            ${errorMessage}
                        </div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
                        <div class="form-group">
                            <label class="form-label" for="username">用户名</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-user"></i></span>
                                <input type="text" id="username" name="username" class="form-control" placeholder="请输入用户名" required>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label" for="password">密码</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-lock"></i></span>
                                <input type="password" id="password" name="password" class="form-control" placeholder="请输入密码" required>
                            </div>
                        </div>
                        
                        <div class="form-group remember-forgot">
                            <div class="checkbox">
                                <input type="checkbox" id="remember" name="remember">
                                <label for="remember">记住我</label>
                            </div>
                            <a href="${pageContext.request.contextPath}/forgotPassword" class="forgot-link">忘记密码？</a>
                        </div>
                        
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary btn-block">登录</button>
                        </div>
                    </form>
                    
                    <div class="auth-footer">
                        <p>还没有账号？ <a href="${pageContext.request.contextPath}/register">立即注册</a></p>
                    </div>
                </div>
            </div>
            
            <div class="auth-image" style="background-image: url('${pageContext.request.contextPath}/assets/images/auth-bg.jpg')">
                <div class="auth-overlay"></div>
                <div class="image-content">
                    <h2>发现校园里的美好缘分</h2>
                    <p>加入我们的平台，认识志同道合的伙伴，参与有趣的校园活动。</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- JavaScript文件 -->
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
    <script>
        // 初始化表单验证
        document.addEventListener('DOMContentLoaded', function() {
            // 登录表单验证规则
            var loginRules = {
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            };
            
            // 登录表单错误消息
            var loginMessages = {
                username: {
                    required: "请输入用户名"
                },
                password: {
                    required: "请输入密码"
                }
            };
            
            // 初始化表单验证
            initFormValidation('loginForm', loginRules, loginMessages);
        });
    </script>
</body>
</html>