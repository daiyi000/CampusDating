<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="我的匹配" cssFile="matches">
    <div class="matches-container">
        <!-- 匹配数据统计 -->
        <div class="matches-stats">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fa fa-users"></i>
                </div>
                <div class="stat-content">
                    <div class="stat-value">${fn:length(matches)}</div>
                    <div class="stat-label">总匹配数</div>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fa fa-heart"></i>
                </div>
                <div class="stat-content">
                    <c:set var="acceptedCount" value="0" />
                    <c:forEach items="${matches}" var="match">
                        <c:if test="${match.matchStatus == 'accepted'}">
                            <c:set var="acceptedCount" value="${acceptedCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <div class="stat-value">${acceptedCount}</div>
                    <div class="stat-label">已接受</div>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fa fa-clock-o"></i>
                </div>
                <div class="stat-content">
                    <c:set var="pendingCount" value="0" />
                    <c:forEach items="${matches}" var="match">
                        <c:if test="${match.matchStatus == 'pending'}">
                            <c:set var="pendingCount" value="${pendingCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <div class="stat-value">${pendingCount}</div>
                    <div class="stat-label">待处理</div>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fa fa-calendar"></i>
                </div>
                <div class="stat-content">
                    <c:set var="recentCount" value="0" />
                    <c:forEach items="${matches}" var="match">
                        <c:if test="${match.updateTime != null && (now.time - match.updateTime.time) / (1000 * 60 * 60 * 24) <= 7}">
                            <c:set var="recentCount" value="${recentCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <div class="stat-value">${recentCount}</div>
                    <div class="stat-label">最近一周</div>
                </div>
            </div>
        </div>
        
        <!-- 匹配过滤器 -->
        <div class="matches-filter">
            <form action="${pageContext.request.contextPath}/matches" method="get">
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="matchStatus">状态：</label>
                        <select name="matchStatus" id="matchStatus" class="form-control">
                            <option value="all" ${param.matchStatus == 'all' || empty param.matchStatus ? 'selected' : ''}>所有</option>
                            <option value="pending" ${param.matchStatus == 'pending' ? 'selected' : ''}>待处理</option>
                            <option value="accepted" ${param.matchStatus == 'accepted' ? 'selected' : ''}>已接受</option>
                            <option value="rejected" ${param.matchStatus == 'rejected' ? 'selected' : ''}>已拒绝</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="sortBy">排序：</label>
                        <select name="sortBy" id="sortBy" class="form-control">
                            <option value="recent" ${param.sortBy == 'recent' || empty param.sortBy ? 'selected' : ''}>最近更新</option>
                            <option value="score" ${param.sortBy == 'score' ? 'selected' : ''}>匹配度</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <button type="submit" class="btn btn-primary">应用筛选</button>
                    </div>
                </div>
            </form>
        </div>
        
        <!-- 匹配列表 -->
        <div class="matches-list">
            <c:choose>
                <c:when test="${not empty matches}">
                    <div class="row">
                        <c:forEach items="${matches}" var="match">
                            <c:set var="otherUserId" value="${match.userOneId == sessionScope.user.id ? match.userTwoId : match.userOneId}" />
                            <c:set var="otherUser" value="${matchUsers[otherUserId]}" />
                            <c:set var="otherProfile" value="${matchProfiles[otherUserId]}" />
                            
                            <div class="col-4">
                                <div class="match-card">
                                    <div class="match-status ${match.matchStatus}">
                                        <c:choose>
                                            <c:when test="${match.matchStatus == 'accepted'}">
                                                <i class="fa fa-check-circle"></i> 已接受
                                            </c:when>
                                            <c:when test="${match.matchStatus == 'pending'}">
                                                <i class="fa fa-clock-o"></i> 待处理
                                            </c:when>
                                            <c:when test="${match.matchStatus == 'rejected'}">
                                                <i class="fa fa-times-circle"></i> 已拒绝
                                            </c:when>
                                        </c:choose>
                                    </div>
                                    
                                    <div class="match-avatar">
                                        <img src="${not empty otherProfile.avatarUrl ? otherProfile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                             alt="${otherUser.username}">
                                    </div>
                                    
                                    <div class="match-info">
                                        <h3 class="match-name">${otherUser.username}</h3>
                                        <p class="match-details">
                                            <c:if test="${not empty otherProfile}">
                                                <span>${otherProfile.school}</span>
                                                <c:if test="${not empty otherProfile.department}">
                                                    <span class="divider">|</span>
                                                    <span>${otherProfile.department}</span>
                                                </c:if>
                                            </c:if>
                                        </p>
                                    </div>
                                    
                                    <div class="match-score">
                                        <div class="score-circle">
                                            <div class="score-value">${match.matchScore}</div>
                                        </div>
                                        <span class="score-label">匹配度</span>
                                    </div>
                                    
                                    <div class="match-actions">
                                        <a href="${pageContext.request.contextPath}/match?id=${match.id}" class="btn btn-sm btn-primary">查看详情</a>
                                        
                                        <c:if test="${match.matchStatus == 'pending'}">
                                            <div class="match-buttons">
                                                <form action="${pageContext.request.contextPath}/match" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="accept">
                                                    <input type="hidden" name="matchId" value="${match.id}">
                                                    <button type="submit" class="btn btn-sm btn-success">接受</button>
                                                </form>
                                                <form action="${pageContext.request.contextPath}/match" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="reject">
                                                    <input type="hidden" name="matchId" value="${match.id}">
                                                    <button type="submit" class="btn btn-sm btn-danger">拒绝</button>
                                                </form>
                                            </div>
                                        </c:if>
                                        
                                        <c:if test="${match.matchStatus == 'accepted'}">
                                            <a href="${pageContext.request.contextPath}/message?with=${otherUserId}" class="btn btn-sm btn-secondary">
                                                <i class="fa fa-envelope"></i> 发送消息
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="no-matches">
                        <i class="fa fa-heart-o"></i>
                        <h3>暂无匹配</h3>
                        <p>去搜索页面寻找心仪的对象吧！</p>
                        <a href="${pageContext.request.contextPath}/search" class="btn btn-primary">搜索用户</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- 推荐匹配 -->
        <c:if test="${not empty recommendedUsers}">
            <div class="recommended-matches">
                <h2 class="section-title">为您推荐</h2>
                <div class="row">
                    <c:forEach items="${recommendedUsers}" var="user" end="3">
                        <div class="col-3">
                            <div class="user-card">
                                <div class="user-avatar">
                                    <img src="${not empty user.profile.avatarUrl ? user.profile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                         alt="${user.username}">
                                </div>
                                <div class="user-info">
                                    <h3 class="user-name">${user.username}</h3>
                                    <p class="user-details">${user.profile.school} · ${user.profile.department}</p>
                                    <div class="match-score">
                                        <span class="score-value">${user.matchScore}</span>
                                        <span class="score-label">匹配度</span>
                                    </div>
                                    <div class="user-actions">
                                        <a href="${pageContext.request.contextPath}/profile?id=${user.id}" class="btn btn-sm btn-secondary">查看资料</a>
                                        <form action="${pageContext.request.contextPath}/match" method="post">
                                            <input type="hidden" name="action" value="create">
                                            <input type="hidden" name="userId" value="${user.id}">
                                            <button type="submit" class="btn btn-sm btn-primary">发起匹配</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>
</t:mainTemplate>