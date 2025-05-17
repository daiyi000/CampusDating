<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="notifications-container">
    <h3>通知</h3>
    
    <c:choose>
        <c:when test="${not empty notifications}">
            <ul class="notification-list">
                <c:forEach items="${notifications}" var="notification">
                    <li class="notification-item ${notification.isRead() ? '' : 'unread'}">
                        <div class="notification-icon">
                            <i class="fa ${getNotificationIcon(notification.getNotificationType())}"></i>
                        </div>
                        <div class="notification-content">
                            <p>${notification.getContent()}</p>
                            <small class="notification-time">${formatTimestamp(notification.getCreateTime())}</small>
                        </div>
                        <div class="notification-actions">
                            <a href="${pageContext.request.contextPath}/${notification.getLink()}" class="btn-view">查看</a>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            
            <div class="notification-actions-all">
                <a href="${pageContext.request.contextPath}/notifications?action=markAllAsRead" class="mark-all-read">
                    全部标记为已读
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-notifications">
                <i class="fa fa-bell-slash"></i>
                <p>暂无通知</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%!
    // 根据通知类型获取图标
    public String getNotificationIcon(String notificationType) {
        if (notificationType == null) {
            return "fa-bell";
        }
        
        switch (notificationType) {
            case "match":
                return "fa-heart";
            case "message":
                return "fa-envelope";
            case "event":
                return "fa-calendar";
            case "system":
                return "fa-info-circle";
            default:
                return "fa-bell";
        }
    }
    
    // 格式化时间戳
    public String formatTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        
        java.util.Date now = new java.util.Date();
        long diff = now.getTime() - timestamp.getTime();
        
        // 如果时间差小于1分钟
        if (diff < 60 * 1000) {
            return "刚刚";
        }
        
        // 如果时间差小于1小时
        if (diff < 60 * 60 * 1000) {
            return (diff / (60 * 1000)) + "分钟前";
        }
        
        // 如果时间差小于24小时
        if (diff < 24 * 60 * 60 * 1000) {
            return (diff / (60 * 60 * 1000)) + "小时前";
        }
        
        // 如果时间差小于7天
        if (diff < 7 * 24 * 60 * 60 * 1000) {
            return (diff / (24 * 60 * 60 * 1000)) + "天前";
        }
        
        // 否则返回完整日期
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(timestamp);
    }
%>