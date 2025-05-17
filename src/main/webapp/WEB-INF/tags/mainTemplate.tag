<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>校园约会平台 - ${pageTitle}</title>
    
    <!-- CSS文件 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sidebar.css">
    
    <!-- 根据页面动态加载对应的CSS -->
    <c:if test="${not empty cssFile}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/${cssFile}.css">
    </c:if>
    
    <!-- Font Awesome图标 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    
    <!-- jQuery和Bootstrap JS (CDN) -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
    <!-- 头部导航 -->
    <jsp:include page="/components/header.jsp" />
    
    <!-- 主内容区域 -->
    <div class="main-content">
        <div class="container">
            <div class="row">
                <!-- 侧边栏 -->
                <div class="col-3">
                    <jsp:include page="/components/sidebar.jsp" />
                </div>
                
                <!-- 页面主体内容 -->
                <div class="col-9">
                    <!-- 页面标题 -->
                    <div class="page-title">
                        <h1>${pageTitle}</h1>
                        <c:if test="${not empty pageSubtitle}">
                            <p>${pageSubtitle}</p>
                        </c:if>
                    </div>
                    
                    <!-- 成功消息显示 -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">
                            ${successMessage}
                        </div>
                    </c:if>
                    
                    <!-- 错误消息显示 -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">
                            ${errorMessage}
                        </div>
                    </c:if>
                    
                    <!-- 主体内容 -->
                    <jsp:doBody />
                </div>
            </div>
        </div>
    </div>
    
    <!-- 页脚 -->
    <jsp:include page="/components/footer.jsp" />
    
    <!-- 主JS文件 -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    
    <!-- 表单验证JS -->
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
    
    <!-- 根据页面动态加载对应的JS -->
    <c:if test="${not empty jsFile}">
        <script src="${pageContext.request.contextPath}/assets/js/${jsFile}.js"></script>
    </c:if>
</body>
</html>