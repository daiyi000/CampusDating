<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="搜索用户" cssFile="matches">
    <div class="search-container">
        <!-- 搜索表单 -->
        <div class="search-form-card card">
            <div class="card-header">
                <h3>搜索条件</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/search" method="get" id="searchForm">
                    <div class="row">
                        <div class="col-4">
                            <div class="form-group">
                                <label for="gender">性别</label>
                                <select id="gender" name="gender" class="form-control">
                                    <option value="" ${empty param.gender ? 'selected' : ''}>不限</option>
                                    <option value="男" ${param.gender == '男' ? 'selected' : ''}>男</option>
                                    <option value="女" ${param.gender == '女' ? 'selected' : ''}>女</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="col-4">
                            <div class="form-group">
                                <label>年龄范围</label>
                                <div class="d-flex align-items-center">
                                    <input type="number" id="minAge" name="minAge" class="form-control" placeholder="最小" min="18" max="100" value="${param.minAge}">
                                    <span class="mx-2">-</span>
                                    <input type="number" id="maxAge" name="maxAge" class="form-control" placeholder="最大" min="18" max="100" value="${param.maxAge}">
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-4">
                            <div class="form-group">
                                <label for="school">学校</label>
                                <input type="text" id="school" name="school" class="form-control" placeholder="输入学校名称" value="${param.school}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-4">
                            <div class="form-group">
                                <label for="department">院系</label>
                                <input type="text" id="department" name="department" class="form-control" placeholder="输入院系名称" value="${param.department}">
                            </div>
                        </div>
                        
                        <div class="col-4">
                            <div class="form-group">
                                <label for="major">专业</label>
                                <input type="text" id="major" name="major" class="form-control" placeholder="输入专业名称" value="${param.major}">
                            </div>
                        </div>
                        
                        <div class="col-4">
                            <div class="form-group">
                                <label for="interests">兴趣爱好</label>
                                <select id="interests" name="interest" class="form-control">
                                    <option value="" ${empty param.interest ? 'selected' : ''}>不限</option>
                                    <c:forEach items="${allInterests}" var="interest">
                                        <option value="${interest.id}" ${param.interest == interest.id ? 'selected' : ''}>${interest.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    
                    <div class="text-center mt-3">
                        <button type="submit" class="btn btn-primary">
                            <i class="fa fa-search"></i> 搜索
                        </button>
                        <button type="reset" class="btn btn-secondary ml-2">
                            <i class="fa fa-refresh"></i> 重置
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- 搜索结果 -->
        <div class="search-results mt-4">
            <c:choose>
                <c:when test="${searchPerformed}">
                    <div class="result-header">
                        <h2>搜索结果 <small>(共 ${fn:length(profiles)} 条结果)</small></h2>
                    </div>
                    
                    <c:choose>
                        <c:when test="${not empty profiles}">
                            <div class="row">
                                <c:forEach items="${profiles}" var="profile">
                                    <c:set var="user" value="${users[profile.userId]}" />
                                    
                                    <div class="col-4 mb-4">
                                        <div class="user-card">
                                            <div class="user-avatar">
                                                <img src="${not empty profile.avatarUrl ? profile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                                                     alt="${user.username}">
                                            </div>
                                            <div class="user-info">
                                                <h3 class="user-name">${user.username}</h3>
                                                <p class="user-details">
                                                    <c:if test="${not empty profile.gender}">
                                                        <span>${profile.gender}</span>
                                                    </c:if>
                                                    <c:if test="${profile.age > 0}">
                                                        <span class="divider">|</span>
                                                        <span>${profile.age}岁</span>
                                                    </c:if>
                                                </p>
                                                <p class="user-details">
                                                    <c:if test="${not empty profile.school}">
                                                        <span>${profile.school}</span>
                                                    </c:if>
                                                    <c:if test="${not empty profile.department}">
                                                        <span class="divider">|</span>
                                                        <span>${profile.department}</span>
                                                    </c:if>
                                                </p>
                                                
                                                <c:if test="${not empty userInterestsMap[profile.userId]}">
                                                    <div class="user-interests">
                                                        <c:forEach items="${userInterestsMap[profile.userId]}" var="interest" end="2">
                                                            <span class="interest-tag small">${interest.name}</span>
                                                        </c:forEach>
                                                        <c:if test="${fn:length(userInterestsMap[profile.userId]) > 3}">
                                                            <span class="interest-more">+${fn:length(userInterestsMap[profile.userId]) - 3}</span>
                                                        </c:if>
                                                    </div>
                                                </c:if>
                                                
                                                <div class="user-actions">
                                                    <a href="${pageContext.request.contextPath}/profile?id=${profile.userId}" class="btn btn-sm btn-secondary">查看资料</a>
                                                    <form action="${pageContext.request.contextPath}/match" method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="create">
                                                        <input type="hidden" name="userId" value="${profile.userId}">
                                                        <button type="submit" class="btn btn-sm btn-primary">发起匹配</button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="no-results">
                                <i class="fa fa-search"></i>
                                <h3>未找到符合条件的用户</h3>
                                <p>请尝试修改搜索条件</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <div class="no-search">
                        <i class="fa fa-search"></i>
                        <h3>填写搜索条件开始查找心仪对象</h3>
                        <p>您可以通过性别、年龄、学校、院系等条件筛选用户</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <script>
        // 重置表单
        document.querySelector('button[type="reset"]').addEventListener('click', function(e) {
            e.preventDefault();
            document.getElementById('gender').value = '';
            document.getElementById('minAge').value = '';
            document.getElementById('maxAge').value = '';
            document.getElementById('school').value = '';
            document.getElementById('department').value = '';
            document.getElementById('major').value = '';
            document.getElementById('interests').value = '';
        });
    </script>
</t:mainTemplate>