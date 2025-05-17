<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="活动详情" cssFile="events">
    <c:if test="${not empty event}">
        <div class="event-details-container">
            <!-- 活动头部 -->
            <div class="event-header">
                <div class="event-cover" style="background-image: url('${not empty event.imageUrl ? event.imageUrl : pageContext.request.contextPath.concat('/assets/images/event-placeholder.jpg')}')">
                    <div class="event-overlay"></div>
                    <div class="event-title-container">
                        <h1 class="event-title">${event.title}</h1>
                        <div class="event-meta">
                            <span class="event-type"><i class="fa fa-tag"></i> ${event.eventType}</span>
                            <span class="event-date"><i class="fa fa-calendar"></i> ${formatDate(event.eventDate)}</span>
                            <span class="event-time"><i class="fa fa-clock-o"></i> ${formatTime(event.startTime)} - ${formatTime(event.endTime)}</span>
                            <span class="event-location"><i class="fa fa-map-marker"></i> ${event.location}</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 活动内容 -->
            <div class="event-content">
                <div class="row">
                    <div class="col-8">
                        <div class="card">
                            <div class="card-header">
                                <h3><i class="fa fa-info-circle"></i> 活动详情</h3>
                            </div>
                            <div class="card-body">
                                <div class="event-description">
                                    <p>${event.description}</p>
                                </div>
                                
                                <c:if test="${not empty event.latitude && not empty event.longitude && event.latitude != 0 && event.longitude != 0}">
                                    <div class="event-map">
                                        <h4>活动地点</h4>
                                        <div id="eventMap" style="width: 100%; height: 300px;"></div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="card mt-4">
                            <div class="card-header">
                                <h3><i class="fa fa-users"></i> 参与者 (${event.currentParticipants}/${event.maxParticipants})</h3>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty participants}">
                                        <div class="participants-list">
                                            <c:forEach items="${participants}" var="participant">
                                                <div class="participant-item">
                                                    <a href="${pageContext.request.contextPath}/profile?id=${participant.id}" class="participant-avatar">
                                                        <img src="${not empty participant.avatarUrl ? participant.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                                             alt="${participant.username}">
                                                    </a>
                                                    <div class="participant-info">
                                                        <a href="${pageContext.request.contextPath}/profile?id=${participant.id}" class="participant-name">${participant.username}</a>
                                                        <c:if test="${participant.id == event.creatorId}">
                                                            <span class="creator-badge">创建者</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="no-participants">
                                            <p>暂无参与者，成为第一个参与者吧！</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-4">
                        <div class="card event-actions-card">
                            <div class="card-body">
                                <div class="event-status">
                                    <c:choose>
                                        <c:when test="${event.eventDate.before(today)}">
                                            <div class="status-badge past">
                                                <i class="fa fa-calendar-check-o"></i> 已结束
                                            </div>
                                        </c:when>
                                        <c:when test="${event.currentParticipants >= event.maxParticipants && !isParticipated}">
                                            <div class="status-badge full">
                                                <i class="fa fa-users"></i> 名额已满
                                            </div>
                                        </c:when>
                                        <c:when test="${isParticipated}">
                                            <div class="status-badge joined">
                                                <i class="fa fa-check-circle"></i> 已参与
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="status-badge active">
                                                <i class="fa fa-calendar"></i> 可参与
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <div class="event-info-list">
                                    <div class="info-item">
                                        <div class="info-label">创建者</div>
                                        <div class="info-value">
                                            <a href="${pageContext.request.contextPath}/profile?id=${creator.id}" class="creator-link">
                                                <img src="${not empty creator.avatarUrl ? creator.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                                     alt="${creator.username}" class="creator-avatar">
                                                <span>${creator.username}</span>
                                            </a>
                                        </div>
                                    </div>
                                    
                                    <div class="info-item">
                                        <div class="info-label">创建时间</div>
                                        <div class="info-value">${formatDateTime(event.createTime)}</div>
                                    </div>
                                    
                                    <div class="info-item">
                                        <div class="info-label">参与人数</div>
                                        <div class="info-value">
                                            <div class="progress">
                                                <div class="progress-bar" style="width: ${(event.currentParticipants / event.maxParticipants) * 100}%"></div>
                                            </div>
                                            <span class="participants-text">${event.currentParticipants}/${event.maxParticipants}</span>
                                        </div>
                                    </div>
                                    
                                    <div class="info-item">
                                        <div class="info-label">活动类型</div>
                                        <div class="info-value">${event.eventType}</div>
                                    </div>
                                    
                                    <div class="info-item">
                                        <div class="info-label">活动状态</div>
                                        <div class="info-value">${event.active ? '进行中' : '已取消'}</div>
                                    </div>
                                    
                                    <div class="info-item">
                                        <div class="info-label">是否公开</div>
                                        <div class="info-value">${event.public ? '公开' : '私密'}</div>
                                    </div>
                                </div>
                                
                                <div class="event-actions">
                                    <c:choose>
                                        <c:when test="${isCreator}">
                                            <a href="${pageContext.request.contextPath}/editEvent?id=${event.id}" class="btn btn-primary btn-block mb-2">
                                                <i class="fa fa-edit"></i> 编辑活动
                                            </a>
                                            <form action="${pageContext.request.contextPath}/eventDetails" method="post" onsubmit="return confirm('确定要删除该活动吗？此操作不可撤销。')">
                                                <input type="hidden" name="eventId" value="${event.id}">
                                                <input type="hidden" name="action" value="delete">
                                                <button type="submit" class="btn btn-danger btn-block">
                                                    <i class="fa fa-trash"></i> 删除活动
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${isParticipated}">
                                            <form action="${pageContext.request.contextPath}/eventDetails" method="post" onsubmit="return confirm('确定要取消参与该活动吗？')">
                                                <input type="hidden" name="eventId" value="${event.id}">
                                                <input type="hidden" name="action" value="leave">
                                                <button type="submit" class="btn btn-warning btn-block">
                                                    <i class="fa fa-sign-out"></i> 取消参与
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${event.currentParticipants < event.maxParticipants && !event.eventDate.before(today)}">
                                            <form action="${pageContext.request.contextPath}/eventDetails" method="post">
                                                <input type="hidden" name="eventId" value="${event.id}">
                                                <input type="hidden" name="action" value="join">
                                                <button type="submit" class="btn btn-success btn-block">
                                                    <i class="fa fa-sign-in"></i> 参与活动
                                                </button>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                    
                                    <a href="${pageContext.request.contextPath}/events" class="btn btn-secondary btn-block mt-2">
                                        <i class="fa fa-arrow-left"></i> 返回活动列表
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="card mt-4">
                            <div class="card-header">
                                <h3><i class="fa fa-share-alt"></i> 分享活动</h3>
                            </div>
                            <div class="card-body">
                                <div class="share-buttons">
                                    <a href="javascript:void(0);" class="share-button wechat" onclick="shareToWechat()">
                                        <i class="fa fa-weixin"></i>
                                    </a>
                                    <a href="javascript:void(0);" class="share-button qq" onclick="shareToQQ()">
                                        <i class="fa fa-qq"></i>
                                    </a>
                                    <a href="javascript:void(0);" class="share-button weibo" onclick="shareToWeibo()">
                                        <i class="fa fa-weibo"></i>
                                    </a>
                                    <a href="javascript:void(0);" class="share-button link" onclick="copyEventLink()">
                                        <i class="fa fa-link"></i>
                                    </a>
                                </div>
                                <div class="event-link">
                                    <input type="text" id="eventLink" class="form-control" readonly 
                                           value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/eventDetails?id=${event.id}">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <c:if test="${not empty event.latitude && not empty event.longitude && event.latitude != 0 && event.longitude != 0}">
            <!-- 地图API -->
            <script src="https://api.map.baidu.com/api?v=3.0&ak=YOUR_API_KEY"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    // 初始化地图
                    var map = new BMap.Map("eventMap");
                    var point = new BMap.Point(${event.longitude}, ${event.latitude});
                    map.centerAndZoom(point, 15);
                    map.enableScrollWheelZoom(true);
                    
                    // 添加标记
                    var marker = new BMap.Marker(point);
                    map.addOverlay(marker);
                    
                    // 添加信息窗口
                    var infoWindow = new BMap.InfoWindow("${event.title}<br>${event.location}");
                    marker.addEventListener("click", function(){
                        map.openInfoWindow(infoWindow, point);
                    });
                });
                
                // 分享功能
                function shareToWechat() {
                    alert("请截图或复制链接，在微信中分享");
                }
                
                function shareToQQ() {
                    var url = encodeURIComponent(document.getElementById('eventLink').value);
                    var title = encodeURIComponent("${event.title}");
                    var summary = encodeURIComponent("${fn:substring(event.description, 0, 100)}");
                    var shareUrl = "http://connect.qq.com/widget/shareqq/index.html?url=" + url + "&title=" + title + "&summary=" + summary;
                    window.open(shareUrl, "_blank");
                }
                
                function shareToWeibo() {
                    var url = encodeURIComponent(document.getElementById('eventLink').value);
                    var title = encodeURIComponent("${event.title}");
                    var shareUrl = "http://service.weibo.com/share/share.php?url=" + url + "&title=" + title;
                    window.open(shareUrl, "_blank");
                }
                
                function copyEventLink() {
                    var linkInput = document.getElementById('eventLink');
                    linkInput.select();
                    document.execCommand('copy');
                    alert("链接已复制到剪贴板");
                }
            </script>
        </c:if>
    </c:if>
</t:mainTemplate>

<%!
    // 格式化日期
    public String formatDate(java.sql.Date date) {
        if (date == null) {
            return "";
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }
    
    // 格式化时间
    public String formatTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(timestamp);
    }
    
    // 格式化日期时间
    public String formatDateTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(timestamp);
    }
%>