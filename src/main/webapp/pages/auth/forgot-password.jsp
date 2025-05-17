<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园约会平台 - 忘记密码</title>
    
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
                    <h2>忘记密码</h2>
                    <p>请输入您的注册邮箱，我们将发送密码重置链接给您</p>
                    
                    <!-- 显示成功消息 -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">
                            ${successMessage}
                            <p>请检查您的邮箱，按照指引重置密码。</p>
                            <p><a href="${pageContext.request.contextPath}/login">返回登录</a></p>
                        </div>
                    </c:if>
                    
                    <!-- 显示错误消息 -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">
                            ${errorMessage}
                        </div>
                    </c:if>
                    
                    <c:if test="${empty successMessage}">
                        <form action="${pageContext.request.contextPath}/forgotPassword" method="post" id="forgotPasswordForm">
                            <div class="form-group">
                                <label class="form-label" for="email">邮箱</label>
                                <div class="input-group">
                                    <span class="input-icon"><i class="fa fa-envelope"></i></span>
                                    <input type="email" id="email" name="email" class="form-control" placeholder="请输入注册邮箱" required>
                                </div>
                                <c:if test="${not empty emailError}">
                                    <span class="error-message">${emailError}</span>
                                </c:if>
                            </div>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">发送重置链接</button>
                            </div>
                        </form>
                        
                        <div class="auth-footer">
                            <p><a href="${pageContext.request.contextPath}/login"><i class="fa fa-arrow-left"></i> 返回登录</a></p>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="auth-image" style="background-image: url('${pageContext.request.contextPath}/assets/images/auth-bg.jpg')">
                <div class="auth-overlay"></div>
                <div class="image-content">
                    <h2>找回您的账户</h2>
                    <p>重置密码后，您可以重新登录校园约会平台，继续探索精彩体验。</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- JavaScript文件 -->
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
    <script>
        // 初始化表单验证
        document.addEventListener('DOMContentLoaded', function() {
            // 忘记密码表单验证规则
            var forgotPasswordRules = {
                email: {
                    required: true,
                    email: true
                }
            };
            
            // 忘记密码表单错误消息
            var forgotPasswordMessages = {
                email: {
                    required: "请输入邮箱",
                    email: "请输入有效的邮箱地址"
                }
            };
            
            // 初始化表单验证
            initFormValidation('forgotPasswordForm', forgotPasswordRules, forgotPasswordMessages);
        });
    </script>
</body>
</html>