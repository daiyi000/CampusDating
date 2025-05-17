<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="聊天" cssFile="messages">
    <div class="chat-page-container">
        <c:if test="${not empty partner}">
            <div class="chat-page-header">
                <div class="partner-info">
                    <a href="${pageContext.request.contextPath}/profile?id=${partner.id}" class="partner-avatar">
                        <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                             alt="${partner.username}">
                    </a>
                    <div class="partner-details">
                        <h2 class="partner-name">${partner.username}</h2>
                        <div class="partner-status">
                            <span class="status-indicator online"></span>
                            <span>在线</span>
                        </div>
                    </div>
                </div>
                <div class="chat-actions">
                    <a href="${pageContext.request.contextPath}/profile?id=${partner.id}" class="btn btn-secondary">
                        <i class="fa fa-user"></i> 查看资料
                    </a>
                    <a href="${pageContext.request.contextPath}/message" class="btn btn-primary">
                        <i class="fa fa-comments"></i> 所有会话
                    </a>
                </div>
            </div>
            
            <div class="chat-page-body">
                <div class="chat-messages" id="chatMessages">
                    <c:if test="${not empty messages}">
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
                    </c:if>
                </div>
                
                <div class="chat-input-container">
                    <form id="messageForm" action="${pageContext.request.contextPath}/message" method="post">
                        <input type="hidden" name="action" value="send">
                        <input type="hidden" name="receiverId" value="${partner.id}">
                        <div class="input-wrapper">
                            <div class="input-actions">
                                <button type="button" class="btn-action emoji-button" id="emojiButton">
                                    <i class="fa fa-smile-o"></i>
                                </button>
                                <input type="file" id="imageUpload" style="display: none;">
                                <button type="button" class="btn-action image-button" id="imageButton">
                                    <i class="fa fa-picture-o"></i>
                                </button>
                            </div>
                            <textarea name="content" id="messageContent" class="form-control" placeholder="输入消息..." required></textarea>
                            <button type="submit" class="send-button">
                                <i class="fa fa-paper-plane"></i>
                            </button>
                        </div>
                    </form>
                    <div class="emoji-picker" id="emojiPicker" style="display: none;">
                        <div class="emoji-list">
                            <span class="emoji" data-emoji="😊">😊</span>
                            <span class="emoji" data-emoji="😂">😂</span>
                            <span class="emoji" data-emoji="😍">😍</span>
                            <span class="emoji" data-emoji="😘">😘</span>
                            <span class="emoji" data-emoji="👍">👍</span>
                            <span class="emoji" data-emoji="👋">👋</span>
                            <span class="emoji" data-emoji="❤️">❤️</span>
                            <span class="emoji" data-emoji="🎉">🎉</span>
                            <span class="emoji" data-emoji="🌹">🌹</span>
                            <span class="emoji" data-emoji="🍻">🍻</span>
                            <span class="emoji" data-emoji="🎵">🎵</span>
                            <span class="emoji" data-emoji="🎮">🎮</span>
                            <span class="emoji" data-emoji="⚽">⚽</span>
                            <span class="emoji" data-emoji="🏀">🏀</span>
                            <span class="emoji" data-emoji="🎓">🎓</span>
                            <span class="emoji" data-emoji="📚">📚</span>
                            <span class="emoji" data-emoji="💻">💻</span>
                            <span class="emoji" data-emoji="🍔">🍔</span>
                            <span class="emoji" data-emoji="🍦">🍦</span>
                            <span class="emoji" data-emoji="☕">☕</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
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
            
            // 表情选择器
            var emojiButton = document.getElementById('emojiButton');
            var emojiPicker = document.getElementById('emojiPicker');
            var messageContent = document.getElementById('messageContent');
            
            emojiButton.addEventListener('click', function(e) {
                e.preventDefault();
                emojiPicker.style.display = emojiPicker.style.display === 'none' ? 'block' : 'none';
            });
            
            // 点击其他区域关闭表情选择器
            document.addEventListener('click', function(e) {
                if (!emojiButton.contains(e.target) && !emojiPicker.contains(e.target)) {
                    emojiPicker.style.display = 'none';
                }
            });
            
            // 添加表情
            var emojis = document.querySelectorAll('.emoji');
            emojis.forEach(function(emoji) {
                emoji.addEventListener('click', function() {
                    var emojiChar = this.getAttribute('data-emoji');
                    
                    // 在光标位置插入表情
                    var cursorPos = messageContent.selectionStart;
                    var textBefore = messageContent.value.substring(0, cursorPos);
                    var textAfter = messageContent.value.substring(cursorPos);
                    messageContent.value = textBefore + emojiChar + textAfter;
                    
                    // 设置光标位置
                    messageContent.selectionStart = cursorPos + emojiChar.length;
                    messageContent.selectionEnd = cursorPos + emojiChar.length;
                    messageContent.focus();
                });
            });
            
            // 图片上传
            var imageButton = document.getElementById('imageButton');
            var imageUpload = document.getElementById('imageUpload');
            
            imageButton.addEventListener('click', function(e) {
                e.preventDefault();
                imageUpload.click();
            });
            
            imageUpload.addEventListener('change', function() {
                if (this.files && this.files[0]) {
                    // 显示上传中提示
                    var messageItem = document.createElement('div');
                    messageItem.className = 'message-item outgoing';
                    messageItem.innerHTML = `
                        <div class="message-bubble">
                            <div class="message-content">
                                <div class="image-upload-preview">
                                    <div class="upload-progress">
                                        <i class="fa fa-spinner fa-spin"></i> 图片上传中...
                                    </div>
                                </div>
                            </div>
                            <div class="message-meta">
                                <span class="message-time">刚刚</span>
                            </div>
                        </div>
                    `;
                    document.getElementById('chatMessages').appendChild(messageItem);
                    scrollToBottom();
                    
                    // 创建FormData对象
                    var formData = new FormData();
                    formData.append('action', 'uploadImage');
                    formData.append('receiverId', '${partner.id}');
                    formData.append('imageFile', this.files[0]);
                    
                    // 发送AJAX请求
                    var xhr = new XMLHttpRequest();
                    xhr.open('POST', '${pageContext.request.contextPath}/chat', true);
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState === XMLHttpRequest.DONE) {
                            if (xhr.status === 200) {
                                try {
                                    var response = JSON.parse(xhr.responseText);
                                    if (response.success) {
                                        // 更新消息内容
                                        messageItem.innerHTML = `
                                            <div class="message-bubble">
                                                <div class="message-content">
                                                    <img src="${response.imageUrl}" alt="图片消息" class="message-image">
                                                </div>
                                                <div class="message-meta">
                                                    <span class="message-time">刚刚</span>
                                                    <span class="message-status">
                                                        <i class="fa fa-check"></i>
                                                    </span>
                                                </div>
                                            </div>
                                        `;
                                    } else {
                                        // 显示错误消息
                                        messageItem.innerHTML = `
                                            <div class="message-bubble">
                                                <div class="message-content">
                                                    <div class="upload-error">
                                                        <i class="fa fa-times-circle"></i> 图片上传失败，请重试
                                                    </div>
                                                </div>
                                                <div class="message-meta">
                                                    <span class="message-time">刚刚</span>
                                                </div>
                                            </div>
                                        `;
                                    }
                                } catch (e) {
                                    // 显示错误消息
                                    messageItem.innerHTML = `
                                        <div class="message-bubble">
                                            <div class="message-content">
                                                <div class="upload-error">
                                                    <i class="fa fa-times-circle"></i> 图片上传失败，请重试
                                                </div>
                                            </div>
                                            <div class="message-meta">
                                                <span class="message-time">刚刚</span>
                                            </div>
                                        </div>
                                    `;
                                }
                            } else {
                                // 显示错误消息
                                messageItem.innerHTML = `
                                    <div class="message-bubble">
                                        <div class="message-content">
                                            <div class="upload-error">
                                                <i class="fa fa-times-circle"></i> 图片上传失败，请重试
                                            </div>
                                        </div>
                                        <div class="message-meta">
                                            <span class="message-time">刚刚</span>
                                        </div>
                                    </div>
                                `;
                            }
                            
                            // 重置文件输入框
                            imageUpload.value = '';
                        }
                    };
                    xhr.send(formData);
                }
            });
            
            // 消息表单提交
            var messageForm = document.getElementById('messageForm');
            messageForm.addEventListener('submit', function(e) {
                e.preventDefault();
                
                var content = messageContent.value.trim();
                if (content === '') {
                    return;
                }
                
                // 先在界面显示消息
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
                document.getElementById('chatMessages').appendChild(messageItem);
                
                // 滚动到底部
                scrollToBottom();
                
                // 清空输入框
                messageContent.value = '';
                
                // 发送AJAX请求
                var xhr = new XMLHttpRequest();
                xhr.open('POST', '${pageContext.request.contextPath}/chat', true);
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            try {
                                var response = JSON.parse(xhr.responseText);
                                if (response.success) {
                                    // 更新消息状态为已发送
                                    messageItem.querySelector('.message-status i').className = 'fa fa-check';
                                } else {
                                    // 显示错误消息
                                    alert('发送消息失败，请重试');
                                }
                            } catch (e) {
                                // 显示错误消息
                                alert('发送消息失败，请重试');
                            }
                        } else {
                            // 显示错误消息
                            alert('发送消息失败，请重试');
                        }
                    }
                };
                xhr.send('action=send&receiverId=${partner.id}&content=' + encodeURIComponent(content));
            });
            
            // 处理Textarea中的Enter键发送消息
            messageContent.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    messageForm.dispatchEvent(new Event('submit'));
                }
            });
            
            // 实时消息更新（polling）
            function fetchNewMessages() {
                var lastMessageId = 0;
                var messageItems = document.querySelectorAll('.message-item');
                if (messageItems.length > 0) {
                    var lastMessage = messageItems[messageItems.length - 1];
                    var messageIdMatch = lastMessage.getAttribute('data-message-id');
                    if (messageIdMatch) {
                        lastMessageId = parseInt(messageIdMatch);
                    }
                }
                
                var xhr = new XMLHttpRequest();
                xhr.open('GET', '${pageContext.request.contextPath}/chat?action=getNewMessages&partnerId=${partner.id}&lastMessageId=' + lastMessageId, true);
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                        try {
                            var response = JSON.parse(xhr.responseText);
                            if (response.messages && response.messages.length > 0) {
                                var chatMessages = document.getElementById('chatMessages');
                                var wasAtBottom = chatMessages.scrollHeight - chatMessages.scrollTop <= chatMessages.clientHeight + 50;
                                
                                response.messages.forEach(function(message) {
                                    var messageItem = document.createElement('div');
                                    messageItem.className = 'message-item ' + (message.senderId == ${sessionScope.user.id} ? 'outgoing' : 'incoming');
                                    messageItem.setAttribute('data-message-id', message.id);
                                    
                                    var html = '';
                                    if (message.senderId != ${sessionScope.user.id}) {
                                        html += `
                                            <div class="message-avatar">
                                                <img src="${not empty partner.avatarUrl ? partner.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                                     alt="${partner.username}">
                                            </div>
                                        `;
                                    }
                                    
                                    html += `
                                        <div class="message-bubble">
                                            <div class="message-content">
                                                ${message.content}
                                            </div>
                                            <div class="message-meta">
                                                <span class="message-time">${formatMessageTime(new Date(message.sendTime))}</span>
                                    `;
                                    
                                    if (message.senderId == ${sessionScope.user.id}) {
                                        html += `
                                                <span class="message-status">
                                                    <i class="fa ${message.isRead ? 'fa-check-circle' : 'fa-check'}"></i>
                                                </span>
                                        `;
                                    }
                                    
                                    html += `
                                            </div>
                                        </div>
                                    `;
                                    
                                    messageItem.innerHTML = html;
                                    chatMessages.appendChild(messageItem);
                                });
                                
                                // 如果之前在底部，滚动到底部
                                if (wasAtBottom) {
                                    scrollToBottom();
                                }
                            }
                        } catch (e) {
                            console.error('Error parsing new messages:', e);
                        }
                    }
                };
                xhr.send();
            }
            
            // 每5秒获取一次新消息
            setInterval(fetchNewMessages, 5000);
        });
        
        // 格式化消息时间
        function formatMessageTime(timestamp) {
            if (!timestamp) {
                return '刚刚';
            }
            
            var date = new Date(timestamp);
            var now = new Date();
            var diffMs = now - date;
            var diffMins = Math.floor(diffMs / 60000);
            var diffHours = Math.floor(diffMs / 3600000);
            
            if (diffMins < 1) {
                return '刚刚';
            } else if (diffMins < 60) {
                return diffMins + '分钟前';
            } else if (diffHours < 24 && date.getDate() === now.getDate()) {
                return ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            } else if (date.getDate() === now.getDate() - 1) {
                return '昨天 ' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            } else {
                return date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2) + ' ' +
                       ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
            }
        }
    </script>
</t:mainTemplate>

<%!
    // 格式化消息时间
    public String formatMessageTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return "刚刚";
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