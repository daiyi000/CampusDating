<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="首页" cssFile="index">
    <!-- 轮播图 -->
    <div class="hero-slider">
        <div class="slider-item active" style="background-image: url('${pageContext.request.contextPath}/assets/images/hero-1.jpg')">
            <div class="slider-content">
                <h2>找到你的校园缘分</h2>
                <p>基于兴趣爱好和共同点进行智能匹配，发现校园里志同道合的伙伴</p>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">立即注册</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary">开始匹配</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="slider-item" style="background-image: url('${pageContext.request.contextPath}/assets/images/hero-2.jpg')">
            <div class="slider-content">
                <h2>参与有趣的校园活动</h2>
                <p>发现并参与各种有趣的校园活动，认识更多新朋友</p>
                <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">浏览活动</a>
            </div>
        </div>
        <div class="slider-item" style="background-image: url('${pageContext.request.contextPath}/assets/images/hero-3.jpg')">
            <div class="slider-content">
                <h2>即时沟通无障碍</h2>
                <p>与心仪的对象即时聊天，增进了解</p>
                <a href="${pageContext.request.contextPath}/message" class="btn btn-primary">开始聊天</a>
            </div>
        </div>
        <div class="slider-controls">
            <span class="control-dot active" data-index="0"></span>
            <span class="control-dot" data-index="1"></span>
            <span class="control-dot" data-index="2"></span>
        </div>
    </div>
    
    <!-- 特色功能 -->
    <div class="features section">
        <h2 class="section-title">我们的特色</h2>
        <div class="row">
            <div class="col-4">
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fa fa-users"></i>
                    </div>
                    <h3>智能匹配</h3>
                    <p>基于兴趣爱好、学术背景和共同点进行智能匹配，帮助你找到最合适的伙伴。</p>
                </div>
            </div>
            <div class="col-4">
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fa fa-calendar-check-o"></i>
                    </div>
                    <h3>校园活动</h3>
                    <p>创建或参与各种校园活动，结交新朋友，拓展你的社交圈。</p>
                </div>
            </div>
            <div class="col-4">
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fa fa-lock"></i>
                    </div>
                    <h3>安全可靠</h3>
                    <p>我们注重用户隐私保护，提供安全的社交环境，让你放心交友。</p>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 最新活动 -->
    <c:if test="${not empty recentEvents}">
        <div class="recent-events section">
            <h2 class="section-title">最新活动</h2>
            <div class="row">
                <c:forEach items="${recentEvents}" var="event" end="2">
                    <div class="col-4">
                        <div class="event-card">
                            <div class="event-image">
                                <img src="${not empty event.imageUrl ? event.imageUrl : pageContext.request.contextPath.concat('/assets/images/event-placeholder.jpg')}" 
                                     alt="${event.title}">
                                <div class="event-date">
                                    <span class="event-month">${fn:substring(event.eventDate, 5, 7)}月</span>
                                    <span class="event-day">${fn:substring(event.eventDate, 8, 10)}</span>
                                </div>
                            </div>
                            <div class="event-content">
                                <h3 class="event-title">${event.title}</h3>
                                <div class="event-meta">
                                    <span class="event-location"><i class="fa fa-map-marker"></i> ${event.location}</span>
                                    <span class="event-time"><i class="fa fa-clock-o"></i> ${fn:substring(event.startTime, 11, 16)}</span>
                                </div>
                                <p class="event-description">${fn:substring(event.description, 0, 100)}${fn:length(event.description) > 100 ? '...' : ''}</p>
                                <a href="${pageContext.request.contextPath}/eventDetails?id=${event.id}" class="btn btn-sm btn-primary">查看详情</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/events" class="btn btn-primary">查看更多活动</a>
            </div>
        </div>
    </c:if>
    
    <!-- 用户推荐 -->
    <c:if test="${not empty sessionScope.user && not empty recommendedUsers}">
        <div class="user-recommendations section">
            <h2 class="section-title">推荐用户</h2>
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
                                <a href="${pageContext.request.contextPath}/profile?id=${user.id}" class="btn btn-sm btn-secondary">查看资料</a>
                                <a href="${pageContext.request.contextPath}/match?action=create&userId=${user.id}" class="btn btn-sm btn-primary">发起匹配</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary">查看更多推荐</a>
            </div>
        </div>
    </c:if>
    
    <!-- 用户评价 -->
    <div class="testimonials section">
        <h2 class="section-title">用户评价</h2>
        <div class="row">
            <div class="col-4">
                <div class="testimonial-card">
                    <div class="testimonial-avatar">
                        <img src="${pageContext.request.contextPath}/assets/images/testimonial-1.jpg" alt="用户头像">
                    </div>
                    <div class="testimonial-content">
                        <p>"在这个平台上认识了很多志同道合的朋友，甚至找到了我现在的男朋友。真的很感谢这个平台！"</p>
                        <div class="testimonial-author">
                            <span class="author-name">李小红</span>
                            <span class="author-school">北京大学</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="testimonial-card">
                    <div class="testimonial-avatar">
                        <img src="${pageContext.request.contextPath}/assets/images/testimonial-2.jpg" alt="用户头像">
                    </div>
                    <div class="testimonial-content">
                        <p>"平台上的活动非常丰富，参加了几次后认识了很多新朋友，大家一起学习一起玩，很开心。"</p>
                        <div class="testimonial-author">
                            <span class="author-name">张明</span>
                            <span class="author-school">清华大学</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-4">
                <div class="testimonial-card">
                    <div class="testimonial-avatar">
                        <img src="${pageContext.request.contextPath}/assets/images/testimonial-3.jpg" alt="用户头像">
                    </div>
                    <div class="testimonial-content">
                        <p>"智能匹配功能真的很准，推荐给我的人都很合我的胃口，聊天也很愉快。强烈推荐这个平台！"</p>
                        <div class="testimonial-author">
                            <span class="author-name">王小明</span>
                            <span class="author-school">复旦大学</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 注册号召 -->
    <c:if test="${empty sessionScope.user}">
        <div class="cta-section">
            <div class="cta-content">
                <h2>加入我们，开始你的校园社交之旅</h2>
                <p>已有超过10,000名学生加入了我们的平台，你还在等什么？</p>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-lg">立即注册</a>
            </div>
        </div>
    </c:if>
</t:mainTemplate>