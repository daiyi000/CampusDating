<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer class="main-footer">
    <div class="footer-container">
        <div class="footer-logo">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="校园约会平台">
            <p>校园约会平台</p>
        </div>
        
        <div class="footer-links">
            <div class="footer-section">
                <h4>平台</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp">首页</a></li>
                    <li><a href="${pageContext.request.contextPath}/about.jsp">关于我们</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact.jsp">联系我们</a></li>
                </ul>
            </div>
            
            <div class="footer-section">
                <h4>功能</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/matches">匹配</a></li>
                    <li><a href="${pageContext.request.contextPath}/search">搜索</a></li>
                    <li><a href="${pageContext.request.contextPath}/events">活动</a></li>
                </ul>
            </div>
            
            <div class="footer-section">
                <h4>支持</h4>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/help.jsp">帮助中心</a></li>
                    <li><a href="${pageContext.request.contextPath}/privacy.jsp">隐私政策</a></li>
                    <li><a href="${pageContext.request.contextPath}/terms.jsp">服务条款</a></li>
                </ul>
            </div>
        </div>
        
        <div class="footer-social">
            <h4>关注我们</h4>
            <div class="social-icons">
                <a href="#"><i class="fa fa-weixin"></i></a>
                <a href="#"><i class="fa fa-qq"></i></a>
                <a href="#"><i class="fa fa-weibo"></i></a>
                <a href="#"><i class="fa fa-github"></i></a>
            </div>
        </div>
    </div>
    
    <div class="footer-bottom">
        <p>&copy; ${java.time.Year.now().getValue()} 校园约会平台 - 版权所有</p>
    </div>
</footer>