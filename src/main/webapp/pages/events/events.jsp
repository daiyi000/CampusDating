<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="活动列表" cssFile="events">
    <div class="events-container">
        <!-- 活动过滤器 -->
        <div class="events-filter">
            <div class="filter-header">
                <h2>活动列表</h2>
                <a href="${pageContext.request.contextPath}/createEvent" class="btn btn-primary">
                    <i class="fa fa-plus"></i> 创建活动
                </a>
            </div>
            
            <div class="filter-tabs">
                <a href="${pageContext.request.contextPath}/events" class="${empty param.source ? 'active' : ''}">所有活动</a>
                <a href="${pageContext.request.contextPath}/events?source=upcoming" class="${param.source == 'upcoming' ? 'active' : ''}">即将到来</a>
                <a href="${pageContext.request.contextPath}/events?source=created" class="${param.source == 'created' ? 'active' : ''}">我创建的</a>
                <a href="${pageContext.request.contextPath}/events?source=participated" class="${param.source == 'participated' ? 'active' : ''}">我参与的</a>
            </div>
            
            <div class="filter-search">
                <form action="${pageContext.request.contextPath}/events" method="get" id="searchEventForm">
                    <div class="input-group">
                        <input type="text" name="search" class="form-control" placeholder="搜索活动..." value="${param.search}">
                        <div class="input-group-append">
                            <button type="submit" class="btn btn-primary">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- 活动类型标签 -->
        <c:if test="${not empty eventTypes}">
            <div class="event-type-tags">
                <a href="${pageContext.request.contextPath}/events${not empty param.source ? '?source='.concat(param.source) : ''}" 
                   class="type-tag ${empty param.type ? 'active' : ''}">
                    全部
                </a>
                <c:forEach items="${eventTypes}" var="type">
                    <a href="${pageContext.request.contextPath}/events?type=${type}${not empty param.source ? '&source='.concat(param.source) : ''}" 
                       class="type-tag ${param.type == type ? 'active' : ''}">
                        ${type}
                    </a>
                </c:forEach>
            </div>
        </c:if>
        
        <!-- 活动列表 -->
        <div class="events-list">
            <c:choose>
                <c:when test="${not empty events}">
                    <div class="row">
                        <c:forEach items="${events}" var="event">
                            <div class="col-4 mb-4">
                                <div class="event-card">
                                    <div class="event-image">
                                        <img src="${not empty event.imageUrl ? event.imageUrl : pageContext.request.contextPath.concat('/assets/images/event-placeholder.jpg')}" 
                                             alt="${event.title}">
                                        <div class="event-date">
                                            <span class="event-month">${fn:substring(event.eventDate, 5, 7)}月</span>
                                            <span class="event-day">${fn:substring(event.eventDate, 8, 10)}</span>
                                        </div>
                                        <c:if test="${not empty event.eventType}">
                                            <div class="event-type-badge">${event.eventType}</div>
                                        </c:if>
                                    </div>
                                    <div class="event-content">
                                        <h3 class="event-title">${event.title}</h3>
                                        <div class="event-meta">
                                            <span class="event-location"><i class="fa fa-map-marker"></i> ${event.location}</span>
                                            <span class="event-time"><i class="fa fa-clock-o"></i> ${fn:substring(event.startTime, 11, 16)}</span>
                                        </div>
                                        <div class="event-description">
                                            ${fn:substring(event.description, 0, 100)}${fn:length(event.description) > 100 ? '...' : ''}
                                        </div>
                                        <div class="event-footer">
                                            <div class="event-participants">
                                                <span class="participants-count">
                                                    <i class="fa fa-users"></i> ${event.currentParticipants}/${event.maxParticipants}
                                                </span>
                                                <c:if test="${event.currentParticipants >= event.maxParticipants}">
                                                    <span class="event-full-badge">已满</span>
                                                </c:if>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/eventDetails?id=${event.id}" class="btn btn-sm btn-primary">查看详情</a>
                                        </div>
                                    </div>
                                    <c:if test="${event.creatorId == sessionScope.user.id}">
                                        <div class="event-creator-badge">
                                            <i class="fa fa-star"></i> 我创建的
                                        </div>
                                    </c:if>
                                    <c:if test="${participatedEvents.contains(event.id)}">
                                        <div class="event-participated-badge">
                                            <i class="fa fa-check-circle"></i> 已参与
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="no-events">
                        <i class="fa fa-calendar-o"></i>
                        <h3>暂无活动</h3>
                        <p>
                            <c:choose>
                                <c:when test="${param.source == 'created'}">
                                    您还没有创建任何活动，点击"创建活动"按钮开始创建吧！
                                </c:when>
                                <c:when test="${param.source == 'participated'}">
                                    您还没有参与任何活动，浏览活动列表并参与感兴趣的活动吧！
                                </c:when>
                                <c:when test="${not empty param.search}">
                                    未找到与"${param.search}"相关的活动，请尝试其他关键词。
                                </c:when>
                                <c:when test="${not empty param.type}">
                                    暂无"${param.type}"类型的活动，请尝试其他类型。
                                </c:when>
                                <c:otherwise>
                                    暂无活动，请稍后再来查看或创建一个新活动！
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/createEvent" class="btn btn-primary">创建活动</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- 分页 -->
        <c:if test="${not empty events && events.size() > 9}">
            <div class="pagination-container">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/events?page=${currentPage - 1}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.source ? '&source='.concat(param.source) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}">
                                <i class="fa fa-angle-left"></i>
                            </a>
                        </li>
                    </c:if>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/events?page=${i}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.source ? '&source='.concat(param.source) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                    
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/events?page=${currentPage + 1}${not empty param.type ? '&type='.concat(param.type) : ''}${not empty param.source ? '&source='.concat(param.source) : ''}${not empty param.search ? '&search='.concat(param.search) : ''}">
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </c:if>
    </div>
    
    <script>
        // 搜索表单提交
        document.addEventListener('DOMContentLoaded', function() {
            var searchForm = document.getElementById('searchEventForm');
            if (searchForm) {
                searchForm.addEventListener('submit', function(e) {
                    var searchInput = this.querySelector('input[name="search"]');
                    if (searchInput.value.trim() === '') {
                        e.preventDefault();
                        searchInput.focus();
                    }
                });
            }
            
            // 在搜索框中按Enter键提交表单
            var searchInput = document.querySelector('input[name="search"]');
            if (searchInput) {
                searchInput.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter') {
                        if (this.value.trim() !== '') {
                            searchForm.submit();
                        } else {
                            e.preventDefault();
                            this.focus();
                        }
                    }
                });
            }
        });
    </script>
</t:mainTemplate>