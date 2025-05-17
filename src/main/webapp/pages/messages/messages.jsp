<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="消息" cssFile="messages">
    <div class="messages-container">
        <c:choose>
            <c:when test="${empty partners}">
                <div class="no-messages">
                    <i class="fa fa-envelope-o"></i>
                    <h3>暂无聊天记录</h3>
                    <p>去匹配页面寻找心仪的对象，开始聊天吧！</p>
                    <a href="${pageContext.request.contextPath}/matches" class="btn btn-primary">查看匹配</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="messages-list-container">
                    <div class="messages-list">
                        <div class="list-header">
                            <div class="search-box">
                                <i class="fa fa-search"></i>
                                <input type="text" id="searchContact" placeholder="搜索联系人..." class="form-control">
                            </div>
                            <div class="list-actions">
                                <div class="dropdown">
                                    <button class="btn btn-sm btn-secondary dropdown-toggle" id="messageFilterButton">
                                        <i class="fa fa-filter"></i>
                                        <c:choose>
                                            <c:when test="${param.filter == 'unread'}">未读消息</c:when>
                                            <c:otherwise>所有消息</c:otherwise>
                                        </c:choose>
                                    </button>
                                    <div class="dropdown-menu" id="messageFilterMenu">
                                        <a href="${pageContext.request.contextPath}/message" class="dropdown-item ${empty param.filter ? 'active' : ''}">所有消息</a>
                                        <a href="${pageContext.request.contextPath}/message?filter=unread" class="dropdown-item ${param.filter == 'unread' ? 'active' : ''}">未读消息</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="contacts-list">
                            <c:forEach items="${partners}" var="partner">
                                <a href="${pageContext.request.contextPath}/message?with=${partner.id}" class="contact-item ${param.with == partner.id ? 'active' : ''}">
                                    <div class="contact-avatar">
                                        <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                             alt="${partner.username}">
                                        <c:if test="${unreadCounts[partner.id] > 0}">
                                            <span class="unread-badge">${unreadCounts[partner.id]}</span>
                                        </c:if>
                                    </div>
                                    <div class="contact-info">
                                        <div class="contact-name">${partner.username}</div>
                                        <div class="contact-preview">
                                            <c:choose>
                                                <c:when test="${not empty lastMessages[partner.id]}">
                                                    <c:set var="lastMsg" value="${lastMessages[partner.id]}" />
                                                    <c:choose>
                                                        <c:when test="${lastMsg.senderId == sessionScope.user.id}">
                                                            <span class="sent-indicator">我: </span>
                                                        </c:when>
                                                    </c:choose>
                                                    ${fn:substring(lastMsg.content, 0, 20)}${fn:length(lastMsg.content) > 20 ? '...' : ''}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">暂无消息</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="contact-time">
                                        <c:if test="${not empty lastMessages[partner.id]}">
                                            <span>${formatTimestamp(lastMessages[partner.id].sendTime)}</span>
                                        </c:if>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty param.with}">
                            <div class="chat-container">
                                <c:set var="partner" value="${partners[Integer.parseInt(param.with)]}" />
                                
                                <div class="chat-header">
                                    <div class="chat-contact">
                                        <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                             alt="${partner.username}" class="chat-avatar">
                                        <div class="chat-contact-info">
                                            <div class="chat-contact-name">${partner.username}</div>
                                            <div class="chat-contact-status">
                                                <span class="status-indicator online"></span>
                                                <span>在线</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="chat-actions">
                                        <a href="${pageContext.request.contextPath}/profile?id=${partner.id}" class="btn btn-sm btn-secondary">
                                            <i class="fa fa-user"></i> 查看资料
                                        </a>
                                    </div>
                                </div>
                                
                                <div class="chat-messages" id="chatMessages">
                                    <c:forEach items="${messages}" var="message">
                                        <div class="message-item ${message.senderId == sessionScope.user.id ? 'outgoing' : 'incoming'}">
                                            <c:if test="${message.senderId != sessionScope.user.id}">
                                                <div class="message-avatar">
                                                    <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                                         alt="${partner.username}">
                                                </div>
                                            </c:if>
                                            <div class="message-bubble">
                                                <div class="message-content">
                                                    ${message.content}
                                                </div>
                                                <div class="message-meta">
                                                    <span class="message-time">${formatMessageTime(message.sendTime)}</span>
                                                    <c:if test="${message.senderId == sessionScope.user.id}">
                                                        <span class="message-status">
                                                            <i class="fa ${message.isRead() ? 'fa-check-circle' : 'fa-check'}"></i>
                                                        </span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                
                                <div class="chat-input">
                                    <form id="messageForm" action="${pageContext.request.contextPath}/message" method="post">
                                        <input type="hidden" name="action" value="send">
                                        <input type="hidden" name="receiverId" value="${param.with}">
                                        <div class="input-group">
                                            <textarea name="content" id="messageContent" class="form-control" placeholder="输入消息..." required></textarea>
                                            <div class="input-group-append">
                                                <button type="submit" class="btn btn-primary">
                                                    <i class="fa fa-paper-plane"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="no-chat-selected">
                                <i class="fa fa-comments-o"></i>
                                <h3>选择一个联系人开始聊天</h3>
                                <p>从左侧列表选择联系人，开始愉快的交流吧！</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script>
        // 聊天区域滚动到底部
        function scrollToBottom() {
            var chatMessages = document.getElementById('chatMessages');
            if (chatMessages) {
                chatMessages.scrollTop = chatMessages.scrollHeight;
            }
        }
        
        // 页面加载完成后滚动到底部
        document.addEventListener('DOMContentLoaded', function() {
            scrollToBottom();
            
            // 搜索联系人功能
            var searchInput = document.getElementById('searchContact');
            if (searchInput) {
                searchInput.addEventListener('input', function() {
                    var searchValue = this.value.toLowerCase();
                    var contactItems = document.querySelectorAll('.contact-item');
                    
                    contactItems.forEach(function(item) {
                        var contactName = item.querySelector('.contact-name').innerText.toLowerCase();
                        if (contactName.includes(searchValue)) {
                            item.style.display = '';
                        } else {
                            item.style.display = 'none';
                        }
                    });
                });
            }
            
            // 消息过滤下拉菜单
            var filterButton = document.getElementById('messageFilterButton');
            var filterMenu = document.getElementById('messageFilterMenu');
            
            if (filterButton && filterMenu) {
                filterButton.addEventListener('click', function(e) {
                    e.preventDefault();
                    filterMenu.classList.toggle('show');
                });
                
                document.addEventListener('click', function(e) {
                    if (!filterButton.contains(e.target)) {
                        filterMenu.classList.remove('show');
                    }
                });
            }
            
            // 消息表单提交
            var messageForm = document.getElementById('messageForm');
            if (messageForm) {
                messageForm.addEventListener('submit', function(e) {
                    e.preventDefault();
                    
                    var content = document.getElementById('messageContent').value.trim();
                    if (content === '') {
                        return;
                    }
                    
                    var receiverId = this.elements['receiverId'].value;
                    
                    // 先在界面显示消息
                    var chatMessages = document.getElementById('chatMessages');
                    var messageItem = document.createElement('div');
                    messageItem.className = 'message-item outgoing';
                    messageItem.innerHTML = `
                        <div class="message-bubble">
                            <div class="message-content">
                                ${content}
                            </div>
                            <div class="message-meta">
                                <span class="message-time">刚刚</span>
                                <span class="message-status">
                                    <i class="fa fa-clock-o"></i>
                                </span>
                            </div>
                        </div>
                    `;
                    chatMessages.appendChild(messageItem);
                    
                    // 滚动到底部
                    scrollToBottom();
                    
                    // 清空输入框
                    document.getElementById('messageContent').value = '';
                    
                    // 发送AJAX请求
                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', '${pageContext.request.contextPath}/chat', true);
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState === XMLHttpRequest.DONE) {
                            if (xhr.status === 200) {
                                var response = JSON.parse(xhr.responseText);
                                if (response.success) {
                                    // 更新消息状态为已发送
                                    messageItem.querySelector('.message-status i').className = 'fa fa-check';
                                } else {
                                    // 显示错误消息
                                    alert('发送消息失败，请重试');
                                }
                            } else {
                                // 显示错误消息
                                alert('发送消息失败，请重试');
                            }
                        }
                    };
                    xhr.send('action=send&receiverId=' + receiverId + '&content=' + encodeURIComponent(content));
                });
            }
            
            // 处理Textarea中的Enter键发送消息
            var messageContent = document.getElementById('messageContent');
            if (messageContent) {
                messageContent.addEventListener('keydown', function(e) {
                    if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault();
                        document.getElementById('messageForm').dispatchEvent(new Event('submit'));
                    }
                });
            }
        });
    </script>
</t:mainTemplate>

<%!
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
        
        // 如果是今天
        java.util.Calendar nowCal = java.util.Calendar.getInstance();
        java.util.Calendar msgCal = java.util.Calendar.getInstance();
        msgCal.setTimeInMillis(timestamp.getTime());
        
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR) && 
            nowCal.get(java.util.Calendar.DAY_OF_YEAR) == msgCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            return new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是昨天
        nowCal.add(java.util.Calendar.DAY_OF_YEAR, -1);
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR) && 
            nowCal.get(java.util.Calendar.DAY_OF_YEAR) == msgCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            return "昨天 " + new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是前天
        nowCal.add(java.util.Calendar.DAY_OF_YEAR, -1);
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR) && 
            nowCal.get(java.util.Calendar.DAY_OF_YEAR) == msgCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            return "前天 " + new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是本周
        if (diff < 7 * 24 * 60 * 60 * 1000) {
            String[] weekdays = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            return weekdays[msgCal.get(java.util.Calendar.DAY_OF_WEEK) - 1] + " " + new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是本年
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR)) {
            return new java.text.SimpleDateFormat("MM-dd").format(timestamp);
        }
        
        // 其他情况
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(timestamp);
    }
    
    // 格式化消息时间
    public String formatMessageTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        
        java.util.Date now = new java.util.Date();
        
        // 如果是今天
        java.util.Calendar nowCal = java.util.Calendar.getInstance();
        java.util.Calendar msgCal = java.util.Calendar.getInstance();
        msgCal.setTimeInMillis(timestamp.getTime());
        
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR) && 
            nowCal.get(java.util.Calendar.DAY_OF_YEAR) == msgCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            return new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是昨天
        nowCal.add(java.util.Calendar.DAY_OF_YEAR, -1);
        if (nowCal.get(java.util.Calendar.YEAR) == msgCal.get(java.util.Calendar.YEAR) && 
            nowCal.get(java.util.Calendar.DAY_OF_YEAR) == msgCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            return "昨天 " + new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 如果是本周
        if (now.getTime() - timestamp.getTime() < 7 * 24 * 60 * 60 * 1000) {
            String[] weekdays = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            return weekdays[msgCal.get(java.util.Calendar.DAY_OF_WEEK) - 1] + " " + new java.text.SimpleDateFormat("HH:mm").format(timestamp);
        }
        
        // 其他情况
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp);
    }
%>