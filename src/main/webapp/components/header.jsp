<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<header class="main-header">
    <div class="header-container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/index.jsp">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="校园约会平台">
                <span>校园约会平台</span>
            </a>
        </div>
        
        <nav class="main-nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="${pageContext.request.servletPath == '/index.jsp' ? 'active' : ''}">首页</a></li>
                
                <c:if test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/matches" class="${fn:startsWith(pageContext.request.servletPath, '/pages/matches/') ? 'active' : ''}">匹配</a></li>
                    <li><a href="${pageContext.request.contextPath}/search" class="${pageContext.request.servletPath == '/pages/matches/search.jsp' ? 'active' : ''}">搜索</a></li>
                    <li><a href="${pageContext.request.contextPath}/message" class="${fn:startsWith(pageContext.request.servletPath, '/pages/messages/') ? 'active' : ''}">消息</a></li>
                    <li><a href="${pageContext.request.contextPath}/events" class="${fn:startsWith(pageContext.request.servletPath, '/pages/events/') ? 'active' : ''}">活动</a></li>
                </c:if>
            </ul>
        </nav>
        
        <div class="user-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <div class="notification-icon">
                        <a href="#" id="notificationToggle">
                            <i class="fa fa-bell"></i>
                            <c:if test="${unreadNotificationCount > 0}">
                                <span class="notification-badge">${unreadNotificationCount}</span>
                            </c:if>
                        </a>
                        <div class="notification-dropdown" id="notificationDropdown">
                            <jsp:include page="/components/notifications.jsp" />
                        </div>
                    </div>
                    
                    <div class="user-dropdown">
                        <a href="#" id="userToggle">
                            <img src="${not empty sessionScope.userProfile.avatarUrl ? sessionScope.userProfile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                 alt="${sessionScope.user.username}" class="user-avatar">
                            <span>${sessionScope.user.username}</span>
                        </a>
                        <div class="user-dropdown-menu" id="userDropdownMenu">
                            <ul>
                                <li><a href="${pageContext.request.contextPath}/profile"><i class="fa fa-user"></i> 个人资料</a></li>
                                <li><a href="${pageContext.request.contextPath}/interest"><i class="fa fa-heart"></i> 兴趣爱好</a></li>
                                <li class="divider"></li>
                                <li><a href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out"></i> 退出登录</a></li>
                            </ul>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-login">登录</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-register">注册</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<script>
    // 通知下拉菜单
    document.getElementById('notificationToggle').addEventListener('click', function(e) {
        e.preventDefault();
        document.getElementById('notificationDropdown').classList.toggle('show');
        document.getElementById('userDropdownMenu').classList.remove('show');
    });
    
    // 用户下拉菜单
    document.getElementById('userToggle').addEventListener('click', function(e) {
        e.preventDefault();
        document.getElementById('userDropdownMenu').classList.toggle('show');
        document.getElementById('notificationDropdown').classList.remove('show');
    });
    
    // 点击其他地方关闭下拉菜单
    document.addEventListener('click', function(e) {
        if (!e.target.matches('#notificationToggle') && !e.target.matches('#userToggle') && 
            !document.getElementById('notificationDropdown').contains(e.target) && 
            !document.getElementById('userDropdownMenu').contains(e.target)) {
            document.getElementById('notificationDropdown').classList.remove('show');
            document.getElementById('userDropdownMenu').classList.remove('show');
        }
    });
</script>