<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="匹配详情" cssFile="matches">
    <div class="match-details-container">
        <c:if test="${not empty match && not empty otherUser && not empty otherProfile}">
            <!-- 匹配状态栏 -->
            <div class="match-status-bar ${match.matchStatus}">
                <div class="status-icon">
                    <c:choose>
                        <c:when test="${match.matchStatus == 'accepted'}">
                            <i class="fa fa-check-circle"></i>
                        </c:when>
                        <c:when test="${match.matchStatus == 'pending'}">
                            <i class="fa fa-clock-o"></i>
                        </c:when>
                        <c:when test="${match.matchStatus == 'rejected'}">
                            <i class="fa fa-times-circle"></i>
                        </c:when>
                    </c:choose>
                </div>
                <div class="status-text">
                    <c:choose>
                        <c:when test="${match.matchStatus == 'accepted'}">
                            已接受匹配 - 你们现在可以开始聊天了
                        </c:when>
                        <c:when test="${match.matchStatus == 'pending' && match.userOneId == sessionScope.user.id}">
                            等待对方回应
                        </c:when>
                        <c:when test="${match.matchStatus == 'pending' && match.userTwoId == sessionScope.user.id}">
                            等待你的回应
                        </c:when>
                        <c:when test="${match.matchStatus == 'rejected'}">
                            已拒绝匹配
                        </c:when>
                    </c:choose>
                </div>
                <div class="match-date">
                    匹配时间: ${match.createTime}
                </div>
            </div>
            
            <!-- 匹配信息卡片 -->
            <div class="match-info-card">
                <div class="match-profile">
                    <div class="profile-header">
                        <div class="profile-avatar">
                            <img src="${not empty otherProfile.avatarUrl ? otherProfile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                 alt="${otherUser.username}">
                        </div>
                        <div class="profile-info">
                            <h1 class="profile-name">${otherUser.username}</h1>
                            <p class="profile-headline">
                                <c:if test="${not empty otherProfile.fullName}">
                                    <span>${otherProfile.fullName}</span>
                                </c:if>
                                <c:if test="${not empty otherProfile.school}">
                                    <span class="divider">|</span>
                                    <span>${otherProfile.school}</span>
                                </c:if>
                                <c:if test="${not empty otherProfile.department}">
                                    <span class="divider">|</span>
                                    <span>${otherProfile.department}</span>
                                </c:if>
                            </p>
                        </div>
                    </div>
                    
                    <div class="profile-body">
                        <div class="profile-section">
                            <h2 class="section-title">基本信息</h2>
                            <div class="info-grid">
                                <div class="info-item">
                                    <div class="info-label">性别</div>
                                    <div class="info-value">${not empty otherProfile.gender ? otherProfile.gender : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">年龄</div>
                                    <div class="info-value">${otherProfile.age > 0 ? otherProfile.age : '未填写'} 岁</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">星座</div>
                                    <div class="info-value">${not empty otherProfile.zodiacSign ? otherProfile.zodiacSign : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">专业</div>
                                    <div class="info-value">${not empty otherProfile.major ? otherProfile.major : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">家乡</div>
                                    <div class="info-value">${not empty otherProfile.hometown ? otherProfile.hometown : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">恋爱状态</div>
                                    <div class="info-value">${not empty otherProfile.relationshipStatus ? otherProfile.relationshipStatus : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">寻找</div>
                                    <div class="info-value">${not empty otherProfile.lookingFor ? otherProfile.lookingFor : '未填写'}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">性格类型</div>
                                    <div class="info-value">${not empty otherProfile.personalityType ? otherProfile.personalityType : '未填写'}</div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="profile-section">
                            <h2 class="section-title">个人介绍</h2>
                            <div class="profile-bio">
                                <c:choose>
                                    <c:when test="${not empty otherProfile.bio}">
                                        <p>${otherProfile.bio}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">暂无个人介绍</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <c:if test="${not empty commonInterests}">
                            <div class="profile-section">
                                <h2 class="section-title">共同兴趣 (${fn:length(commonInterests)})</h2>
                                <div class="interest-tags">
                                    <c:forEach items="${commonInterests}" var="interest">
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
                    </div>
                </div>
                
                <div class="match-actions-panel">
                    <div class="match-score-display">
                        <div class="score-circle large">
                            <div class="score-value">${match.matchScore}</div>
                        </div>
                        <span class="score-label">匹配度</span>
                    </div>
                    
                    <div class="match-actions">
                        <c:choose>
                            <c:when test="${match.matchStatus == 'pending' && match.userTwoId == sessionScope.user.id}">
                                <div class="action-buttons">
                                    <form action="${pageContext.request.contextPath}/match" method="post">
                                        <input type="hidden" name="action" value="accept">
                                        <input type="hidden" name="matchId" value="${match.id}">
                                        <button type="submit" class="btn btn-success btn-block mb-2">
                                            <i class="fa fa-check"></i> 接受匹配
                                        </button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/match" method="post">
                                        <input type="hidden" name="action" value="reject">
                                        <input type="hidden" name="matchId" value="${match.id}">
                                        <button type="submit" class="btn btn-danger btn-block">
                                            <i class="fa fa-times"></i> 拒绝匹配
                                        </button>
                                    </form>
                                </div>
                            </c:when>
                            <c:when test="${match.matchStatus == 'accepted'}">
                                <a href="${pageContext.request.contextPath}/message?with=${otherUser.id}" class="btn btn-primary btn-block mb-2">
                                    <i class="fa fa-envelope"></i> 发送消息
                                </a>
                            </c:when>
                        </c:choose>
                        
                        <a href="${pageContext.request.contextPath}/profile?id=${otherUser.id}" class="btn btn-secondary btn-block">
                            <i class="fa fa-user"></i> 查看完整资料
                        </a>
                    </div>
                    
                    <c:if test="${match.matchStatus == 'accepted'}">
                        <div class="match-notes mt-4">
                            <h4>匹配备注</h4>
                            <form action="${pageContext.request.contextPath}/match" method="post">
                                <input type="hidden" name="action" value="updateNotes">
                                <input type="hidden" name="matchId" value="${match.id}">
                                <div class="form-group">
                                    <textarea name="matchNotes" class="form-control" rows="3" placeholder="添加备注...">${match.matchNotes}</textarea>
                                </div>
                                <button type="submit" class="btn btn-sm btn-primary">保存备注</button>
                            </form>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <!-- 相似度分析 -->
            <div class="similarity-analysis">
                <h2 class="section-title">相似度分析</h2>
                <div class="similarity-charts">
                    <div class="row">
                        <div class="col-6">
                            <div class="chart-card">
                                <h3>学术背景</h3>
                                <div class="chart-container">
                                    <canvas id="academicChart"></canvas>
                                </div>
                                <div class="chart-description">
                                    <p>你们就读于 <strong>${sessionScope.userProfile.school == otherProfile.school ? '同一所' : '不同'}</strong> 学校。</p>
                                    <p>你们的专业 <strong>${sessionScope.userProfile.major == otherProfile.major ? '相同' : '不同'}</strong>。</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="chart-card">
                                <h3>个人特质</h3>
                                <div class="chart-container">
                                    <canvas id="personalityChart"></canvas>
                                </div>
                                <div class="chart-description">
                                    <p>你们的性格类型 <strong>${sessionScope.userProfile.personalityType == otherProfile.personalityType ? '相同' : '不同'}</strong>。</p>
                                    <p>共同兴趣数量: <strong>${fn:length(commonInterests)}</strong></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </c:if>
    </div>
    
    <!-- 加载 Chart.js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
    <script>
        // 初始化图表
        document.addEventListener('DOMContentLoaded', function() {
            // 学术背景相似度图表
            var academicCtx = document.getElementById('academicChart').getContext('2d');
            var academicChart = new Chart(academicCtx, {
                type: 'radar',
                data: {
                    labels: ['学校', '院系', '专业', '年级'],
                    datasets: [{
                        label: '相似度',
                        data: [
                            ${sessionScope.userProfile.school == otherProfile.school ? 100 : 30},
                            ${sessionScope.userProfile.department == otherProfile.department ? 100 : 40},
                            ${sessionScope.userProfile.major == otherProfile.major ? 100 : 50},
                            ${sessionScope.userProfile.grade == otherProfile.grade ? 100 : 60 + 10 * (4 - Math.abs(sessionScope.userProfile.grade - otherProfile.grade))}
                        ],
                        backgroundColor: 'rgba(52, 152, 219, 0.2)',
                        borderColor: 'rgba(52, 152, 219, 1)',
                        pointBackgroundColor: 'rgba(52, 152, 219, 1)',
                        pointBorderColor: '#fff',
                        pointHoverBackgroundColor: '#fff',
                        pointHoverBorderColor: 'rgba(52, 152, 219, 1)'
                    }]
                },
                options: {
                    scale: {
                        ticks: {
                            beginAtZero: true,
                            max: 100
                        }
                    }
                }
            });
            
            // 个人特质相似度图表
            var personalityCtx = document.getElementById('personalityChart').getContext('2d');
            var personalityChart = new Chart(personalityCtx, {
                type: 'radar',
                data: {
                    labels: ['性格类型', '星座', '兴趣爱好', '恋爱观'],
                    datasets: [{
                        label: '相似度',
                        data: [
                            ${sessionScope.userProfile.personalityType == otherProfile.personalityType ? 100 : 40},
                            ${sessionScope.userProfile.zodiacSign == otherProfile.zodiacSign ? 100 : 60},
                            ${(fn:length(commonInterests) * 100) / (fn:length(userInterests) > 0 ? fn:length(userInterests) : 1)},
                            ${sessionScope.userProfile.lookingFor == otherProfile.lookingFor ? 100 : 50}
                        ],
                        backgroundColor: 'rgba(46, 204, 113, 0.2)',
                        borderColor: 'rgba(46, 204, 113, 1)',
                        pointBackgroundColor: 'rgba(46, 204, 113, 1)',
                        pointBorderColor: '#fff',
                        pointHoverBackgroundColor: '#fff',
                        pointHoverBorderColor: 'rgba(46, 204, 113, 1)'
                    }]
                },
                options: {
                    scale: {
                        ticks: {
                            beginAtZero: true,
                            max: 100
                        }
                    }
                }
            });
        });
    </script>
</t:mainTemplate>