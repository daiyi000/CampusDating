<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- 获取当前页面路径 --%>
<c:set var="currentPath" value="${pageContext.request.servletPath}" />

<div class="sidebar">
    <div class="sidebar-user">
        <c:if test="${not empty sessionScope.user}">
            <div class="sidebar-user-info">
                <img src="${not empty sessionScope.userProfile.avatarUrl ? sessionScope.userProfile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                     alt="${sessionScope.user.username}" class="sidebar-user-avatar">
                <div class="sidebar-user-details">
                    <h3>${sessionScope.user.username}</h3>
                    <c:if test="${not empty sessionScope.userProfile}">
                        <p>${sessionScope.userProfile.school} · ${sessionScope.userProfile.department}</p>
                    </c:if>
                </div>
            </div>
            
            <div class="sidebar-user-stats">
                <div class="stat-item">
                    <span class="stat-value">${matchCount}</span>
                    <span class="stat-label">匹配</span>
                </div>
                <div class="stat-item">
                    <span class="stat-value">${messagePartnerCount}</span>
                    <span class="stat-label">聊天</span>
                </div>
                <div class="stat-item">
                    <span class="stat-value">${eventCount}</span>
                    <span class="stat-label">活动</span>
                </div>
            </div>
            
            <div class="profile-completion">
                <div class="profile-completion-bar">
                    <div class="completion-progress" style="width: ${profileCompletionPercentage}%"></div>
                </div>
                <span class="completion-text">个人资料完整度: ${profileCompletionPercentage}%</span>
                <c:if test="${profileCompletionPercentage < 100}">
                    <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-sm btn-primary mt-2">完善资料</a>
                </c:if>
            </div>
        </c:if>
    </div>
    
    <div class="sidebar-nav">
        <%-- 根据当前页面路径显示不同的导航菜单 --%>
        <c:choose>
            <%-- 个人资料页面 --%>
            <c:when test="${fn:startsWith(currentPath, '/pages/profile/')}">
                <h3 class="sidebar-title">个人资料</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/profile" class="${currentPath == '/pages/profile/profile.jsp' ? 'active' : ''}">
                        <i class="fa fa-user"></i> 查看个人资料
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/profile?action=edit" class="${currentPath == '/pages/profile/edit-profile.jsp' ? 'active' : ''}">
                        <i class="fa fa-edit"></i> 编辑个人资料
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/interest" class="${currentPath == '/pages/profile/interests.jsp' ? 'active' : ''}">
                        <i class="fa fa-heart"></i> 兴趣爱好
                    </a></li>
                </ul>
            </c:when>
            
            <%-- 匹配页面 --%>
            <c:when test="${fn:startsWith(currentPath, '/pages/matches/')}">
                <h3 class="sidebar-title">匹配</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/matches" class="${currentPath == '/pages/matches/matches.jsp' ? 'active' : ''}">
                        <i class="fa fa-users"></i> 我的匹配
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/search" class="${currentPath == '/pages/matches/search.jsp' ? 'active' : ''}">
                        <i class="fa fa-search"></i> 搜索用户
                    </a></li>
                </ul>
                
                <%-- 显示匹配筛选选项 --%>
                <c:if test="${currentPath == '/pages/matches/matches.jsp'}">
                    <div class="sidebar-filters">
                        <h3 class="sidebar-subtitle">筛选</h3>
                        <form action="${pageContext.request.contextPath}/matches" method="get">
                            <div class="form-group">
                                <label for="matchStatus">状态</label>
                                <select name="matchStatus" id="matchStatus" class="form-control">
                                    <option value="all" ${param.matchStatus == 'all' || empty param.matchStatus ? 'selected' : ''}>所有</option>
                                    <option value="pending" ${param.matchStatus == 'pending' ? 'selected' : ''}>待处理</option>
                                    <option value="accepted" ${param.matchStatus == 'accepted' ? 'selected' : ''}>已接受</option>
                                    <option value="rejected" ${param.matchStatus == 'rejected' ? 'selected' : ''}>已拒绝</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="sortBy">排序</label>
                                <select name="sortBy" id="sortBy" class="form-control">
                                    <option value="recent" ${param.sortBy == 'recent' || empty param.sortBy ? 'selected' : ''}>最近更新</option>
                                    <option value="score" ${param.sortBy == 'score' ? 'selected' : ''}>匹配度</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm w-100">应用筛选</button>
                        </form>
                    </div>
                </c:if>
                
                <%-- 显示搜索筛选选项 --%>
                <c:if test="${currentPath == '/pages/matches/search.jsp'}">
                    <div class="sidebar-filters">
                        <h3 class="sidebar-subtitle">搜索条件</h3>
                        <form action="${pageContext.request.contextPath}/search" method="get">
                            <div class="form-group">
                                <label for="gender">性别</label>
                                <select name="gender" id="gender" class="form-control">
                                    <option value="" ${empty param.gender ? 'selected' : ''}>不限</option>
                                    <option value="男" ${param.gender == '男' ? 'selected' : ''}>男</option>
                                    <option value="女" ${param.gender == '女' ? 'selected' : ''}>女</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="minAge">年龄范围</label>
                                <div class="d-flex">
                                    <input type="number" name="minAge" id="minAge" class="form-control mr-2" placeholder="最小" value="${param.minAge}">
                                    <span class="mt-2">-</span>
                                    <input type="number" name="maxAge" id="maxAge" class="form-control ml-2" placeholder="最大" value="${param.maxAge}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="school">学校</label>
                                <input type="text" name="school" id="school" class="form-control" value="${param.school}">
                            </div>
                            <div class="form-group">
                                <label for="department">院系</label>
                                <input type="text" name="department" id="department" class="form-control" value="${param.department}">
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm w-100">搜索</button>
                        </form>
                    </div>
                </c:if>
            </c:when>
            
            <%-- 消息页面 --%>
            <c:when test="${fn:startsWith(currentPath, '/pages/messages/')}">
                <h3 class="sidebar-title">消息</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/message" class="${currentPath == '/pages/messages/messages.jsp' ? 'active' : ''}">
                        <i class="fa fa-envelope"></i> 所有消息
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/message?filter=unread" class="${param.filter == 'unread' ? 'active' : ''}">
                        <i class="fa fa-envelope-open"></i> 未读消息
                        <c:if test="${unreadMessageCount > 0}">
                            <span class="badge">${unreadMessageCount}</span>
                        </c:if>
                    </a></li>
                </ul>
                
                <%-- 最近联系人列表 --%>
                <c:if test="${not empty messagePartners}">
                    <div class="recent-contacts">
                        <h3 class="sidebar-subtitle">最近联系人</h3>
                        <ul class="contact-list">
                            <c:forEach items="${messagePartners}" var="partner">
                                <li>
                                    <a href="${pageContext.request.contextPath}/message?with=${partner.id}" class="${param.with == partner.id ? 'active' : ''}">
                                        <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                             alt="${partner.username}" class="contact-avatar">
                                        <div class="contact-info">
                                            <span class="contact-name">${partner.username}</span>
                                            <c:if test="${not empty lastMessages[partner.id]}">
                                                <span class="contact-last-message">${fn:substring(lastMessages[partner.id].content, 0, 20)}${fn:length(lastMessages[partner.id].content) > 20 ? '...' : ''}</span>
                                            </c:if>
                                        </div>
                                        <c:if test="${unreadCounts[partner.id] > 0}">
                                            <span class="contact-unread-badge">${unreadCounts[partner.id]}</span>
                                        </c:if>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </c:when>
            
            <%-- 活动页面 --%>
            <c:when test="${fn:startsWith(currentPath, '/pages/events/')}">
                <h3 class="sidebar-title">活动</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/events" class="${currentPath == '/pages/events/events.jsp' && empty param.source ? 'active' : ''}">
                        <i class="fa fa-calendar"></i> 所有活动
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/events?source=upcoming" class="${param.source == 'upcoming' ? 'active' : ''}">
                        <i class="fa fa-calendar-check-o"></i> 即将到来
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/events?source=created" class="${param.source == 'created' ? 'active' : ''}">
                        <i class="fa fa-user-plus"></i> 我创建的
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/events?source=participated" class="${param.source == 'participated' ? 'active' : ''}">
                        <i class="fa fa-users"></i> 我参与的
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/createEvent" class="${currentPath == '/pages/events/create-event.jsp' ? 'active' : ''}">
                        <i class="fa fa-plus-circle"></i> 创建活动
                    </a></li>
                </ul>
                
                <%-- 活动类型筛选 --%>
                <c:if test="${currentPath == '/pages/events/events.jsp' && not empty eventTypes}">
                    <div class="sidebar-filters">
                        <h3 class="sidebar-subtitle">活动类型</h3>
                        <ul class="event-types">
                            <li>
                                <a href="${pageContext.request.contextPath}/events${not empty param.source ? '?source='.concat(param.source) : ''}" 
                                   class="${empty param.type ? 'active' : ''}">所有类型</a>
                            </li>
                            <c:forEach items="${eventTypes}" var="type">
                                <li>
                                    <a href="${pageContext.request.contextPath}/events?type=${type}${not empty param.source ? '&source='.concat(param.source) : ''}" 
                                       class="${param.type == type ? 'active' : ''}">${type}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </c:when>
            
            <%-- 默认侧边栏 --%>
            <c:otherwise>
                <h3 class="sidebar-title">快捷导航</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/profile">
                        <i class="fa fa-user"></i> 个人资料
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/matches">
                        <i class="fa fa-users"></i> 匹配
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/message">
                        <i class="fa fa-envelope"></i> 消息
                        <c:if test="${unreadMessageCount > 0}">
                            <span class="badge">${unreadMessageCount}</span>
                        </c:if>
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/events">
                        <i class="fa fa-calendar"></i> 活动
                    </a></li>
                    <li><a href="${pageContext.request.contextPath}/interest">
                        <i class="fa fa-heart"></i> 兴趣爱好
                    </a></li>
                </ul>
                
                <%-- 最近活动 --%>
                <c:if test="${not empty recentEvents}">
                    <div class="recent-events">
                        <h3 class="sidebar-subtitle">最近活动</h3>
                        <ul class="event-list">
                            <c:forEach items="${recentEvents}" var="event" end="2">
                                <li>
                                    <a href="${pageContext.request.contextPath}/eventDetails?id=${event.id}">
                                        <div class="event-date">
                                            <span class="event-day">${fn:substring(event.eventDate, 8, 10)}</span>
                                            <span class="event-month">${fn:substring(event.eventDate, 5, 7)}月</span>
                                        </div>
                                        <div class="event-info">
                                            <span class="event-title">${event.title}</span>
                                            <span class="event-location">${event.location}</span>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                        <a href="${pageContext.request.contextPath}/events" class="view-all">查看所有活动</a>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>