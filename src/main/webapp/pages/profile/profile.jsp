jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="个人资料" cssFile="profile">
    <div class="profile-container">
        <div class="profile-header">
            <div class="profile-cover" style="background-image: url('${pageContext.request.contextPath}/assets/images/profile-cover.jpg')">
                <div class="profile-avatar">
                    <img src="${not empty profile.avatarUrl ? profile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                         alt="${not empty viewUser ? viewUser.username : sessionScope.user.username}">
                </div>
            </div>
            
            <div class="profile-info">
                <h1 class="profile-name">${not empty viewUser ? viewUser.username : sessionScope.user.username}</h1>
                <p class="profile-headline">
                    <c:if test="${not empty profile}">
                        <span>${profile.school}</span>
                        <c:if test="${not empty profile.department}">
                            <span class="divider">|</span>
                            <span>${profile.department}</span>
                        </c:if>
                        <c:if test="${not empty profile.major}">
                            <span class="divider">|</span>
                            <span>${profile.major}</span>
                        </c:if>
                    </c:if>
                </p>
                
                <div class="profile-actions">
                    <c:choose>
                        <c:when test="${empty viewUser}">
                            <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-primary">
                                <i class="fa fa-edit"></i> 编辑资料
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/message?with=${viewUser.id}" class="btn btn-primary">
                                <i class="fa fa-envelope"></i> 发送消息
                            </a>
                            <a href="${pageContext.request.contextPath}/match?action=create&userId=${viewUser.id}" class="btn btn-success">
                                <i class="fa fa-heart"></i> 发起匹配
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <div class="profile-body">
            <div class="profile-section">
                <h2 class="section-title">基本信息</h2>
                <div class="basic-info">
                    <div class="info-row">
                        <div class="info-label">姓名</div>
                        <div class="info-value">${not empty profile.fullName ? profile.fullName : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">性别</div>
                        <div class="info-value">${not empty profile.gender ? profile.gender : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">年龄</div>
                        <div class="info-value">${profile.age > 0 ? profile.age : '未填写'} 岁</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">星座</div>
                        <div class="info-value">${not empty profile.zodiacSign ? profile.zodiacSign : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">学校</div>
                        <div class="info-value">${not empty profile.school ? profile.school : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">院系</div>
                        <div class="info-value">${not empty profile.department ? profile.department : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">专业</div>
                        <div class="info-value">${not empty profile.major ? profile.major : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">年级</div>
                        <div class="info-value">${profile.grade > 0 ? profile.grade : '未填写'} 年级</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">家乡</div>
                        <div class="info-value">${not empty profile.hometown ? profile.hometown : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">身高</div>
                        <div class="info-value">${profile.height > 0 ? profile.height : '未填写'} cm</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">恋爱状态</div>
                        <div class="info-value">${not empty profile.relationshipStatus ? profile.relationshipStatus : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">寻找</div>
                        <div class="info-value">${not empty profile.lookingFor ? profile.lookingFor : '未填写'}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">性格类型</div>
                        <div class="info-value">${not empty profile.personalityType ? profile.personalityType : '未填写'}</div>
                    </div>
                </div>
            </div>
            
            <div class="profile-section">
                <h2 class="section-title">个人介绍</h2>
                <div class="profile-bio">
                    <c:choose>
                        <c:when test="${not empty profile.bio}">
                            <p>${profile.bio}</p>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted">暂无个人介绍</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <c:if test="${not empty userInterests}">
                <div class="profile-section">
                    <h2 class="section-title">兴趣爱好</h2>
                    <div class="interest-tags">
                        <c:forEach items="${userInterests}" var="interest">
                            <span class="interest-tag">
                                <c:if test="${not empty interest.iconUrl}">
                                    <i class="${interest.iconUrl}"></i>
                                </c:if>
                                ${interest.name}
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${empty viewUser && empty userInterests}">
                <div class="profile-section">
                    <div class="complete-profile-cta">
                        <i class="fa fa-exclamation-circle"></i>
                        <h3>完善您的兴趣爱好</h3>
                        <p>添加您的兴趣爱好可以帮助我们更好地为您推荐匹配对象和活动。</p>
                        <a href="${pageContext.request.contextPath}/interest" class="btn btn-primary">添加兴趣爱好</a>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty userEvents && empty viewUser}">
                <div class="profile-section">
                    <h2 class="section-title">我的活动</h2>
                    <div class="profile-events">
                        <div class="row">
                            <c:forEach items="${userEvents}" var="event" end="2">
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
                        <c:if test="${fn:length(userEvents) > 3}">
                            <div class="text-center mt-3">
                                <a href="${pageContext.request.contextPath}/events?source=created" class="btn btn-secondary">查看全部活动</a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</t:mainTemplate>
