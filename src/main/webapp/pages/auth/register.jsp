<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园约会平台 - 注册</title>
    
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
                    <h2>注册</h2>
                    <p>创建一个新账号，加入校园约会平台</p>
                    
                    <!-- 显示错误消息 -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">
                            ${errorMessage}
                        </div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                        <div class="form-group">
                            <label class="form-label" for="username">用户名</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-user"></i></span>
                                <input type="text" id="username" name="username" class="form-control" placeholder="请输入用户名" value="${username}" required>
                            </div>
                            <c:if test="${not empty usernameError}">
                                <span class="error-message">${usernameError}</span>
                            </c:if>
                            <small class="form-text text-muted">用户名必须以字母开头，只能包含字母、数字和下划线，长度为4-16个字符。</small>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label" for="email">邮箱</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-envelope"></i></span>
                                <input type="email" id="email" name="email" class="form-control" placeholder="请输入邮箱" value="${email}" required>
                            </div>
                            <c:if test="${not empty emailError}">
                                <span class="error-message">${emailError}</span>
                            </c:if>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label" for="password">密码</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-lock"></i></span>
                                <input type="password" id="password" name="password" class="form-control" placeholder="请输入密码" required>
                            </div>
                            <c:if test="${not empty passwordError}">
                                <span class="error-message">${passwordError}</span>
                            </c:if>
                            <small class="form-text text-muted">密码必须包含至少一个字母和一个数字，长度为6-20个字符。</small>
                        </div>
                        
                        <div class="form-group">
                            <label class="form-label" for="confirmPassword">确认密码</label>
                            <div class="input-group">
                                <span class="input-icon"><i class="fa fa-lock"></i></span>
                                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="请再次输入密码" required>
                            </div>
                            <c:if test="${not empty confirmPasswordError}">
                                <span class="error-message">${confirmPasswordError}</span>
                            </c:if>
                        </div>
                        
                        <div class="form-group">
                            <div class="checkbox">
                                <input type="checkbox" id="agreement" name="agreement" required>
                                <label for="agreement">我已阅读并同意 <a href="${pageContext.request.contextPath}/terms.jsp" target="_blank">服务条款</a> 和 <a href="${pageContext.request.contextPath}/privacy.jsp" target="_blank">隐私政策</a></label>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary btn-block">注册</button>
                        </div>
                    </form>
                    
                    <div class="auth-footer">
                        <p>已有账号？ <a href="${pageContext.request.contextPath}/login">立即登录</a></p>
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
            // 注册表单验证规则
            var registerRules = {
                username: {
                    required: true,
                    pattern: /^[a-zA-Z][a-zA-Z0-9_]{3,15}$/
                },
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{6,20}$/
                },
                confirmPassword: {
                    required: true,
                    equalTo: 'password'
                },
                agreement: {
                    required: true
                }
            };
            
            // 注册表单错误消息
            var registerMessages = {
                username: {
                    required: "请输入用户名",
                    pattern: "用户名必须以字母开头，只能包含字母、数字和下划线，长度为4-16个字符"
                },
                email: {
                    required: "请输入邮箱",
                    email: "请输入有效的邮箱地址"
                },
                password: {
                    required: "请输入密码",
                    pattern: "密码必须包含至少一个字母和一个数字，长度为6-20个字符"
                },
                confirmPassword: {
                    required: "请再次输入密码",
                    equalTo: "两次输入的密码不一致"
                },
                agreement: {
                    required: "请阅读并同意服务条款和隐私政策"
                }
            };
            
            // 初始化表单验证
            initFormValidation('registerForm', registerRules, registerMessages);
        });
    </script>
</body>
</html>