<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="兴趣爱好" cssFile="profile">
    <div class="interests-container">
        <div class="card">
            <div class="card-header">
                <h3>我的兴趣爱好</h3>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty userInterests}">
                        <div class="my-interests">
                            <div class="interest-list">
                                <c:forEach items="${userInterests}" var="interest">
                                    <div class="interest-item">
                                        <div class="interest-info">
                                            <div class="interest-icon">
                                                <c:choose>
                                                    <c:when test="${not empty interest.iconUrl}">
                                                        <i class="${interest.iconUrl}"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fa fa-star"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="interest-details">
                                                <h4>${interest.name}</h4>
                                                <span class="interest-category">${interest.category}</span>
                                            </div>
                                        </div>
                                        <div class="interest-actions">
                                            <form action="${pageContext.request.contextPath}/interest" method="post" class="interest-level-form">
                                                <input type="hidden" name="action" value="updateLevel">
                                                <input type="hidden" name="interestId" value="${interest.id}">
                                                <div class="interest-level">
                                                    <label>感兴趣程度：</label>
                                                    <select name="level" class="interest-level-select" onchange="this.form.submit()">
                                                        <c:forEach begin="1" end="5" var="i">
                                                            <option value="${i}" 
                                                                <c:forEach items="${userInterests}" var="ui">
                                                                    <c:if test="${ui.interestId == interest.id && ui.interestLevel == i}">selected</c:if>
                                                                </c:forEach>
                                                            >${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/interest" method="post" class="remove-interest-form">
                                                <input type="hidden" name="action" value="remove">
                                                <input type="hidden" name="interestId" value="${interest.id}">
                                                <button type="submit" class="btn btn-sm btn-danger">
                                                    <i class="fa fa-trash"></i> 移除
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-interests">
                            <i class="fa fa-heart-o"></i>
                            <p>您还没有添加任何兴趣爱好</p>
                            <p>添加兴趣爱好可以帮助我们更好地为您推荐匹配对象和活动</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <div class="card mt-4">
            <div class="card-header">
                <h3>添加兴趣爱好</h3>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/interest" method="post" id="addInterestsForm">
                    <input type="hidden" name="action" value="add">
                    
                    <div class="interests-filter">
                        <div class="form-group">
                            <label for="categoryFilter">按类别筛选：</label>
                            <select id="categoryFilter" class="form-control">
                                <option value="all">所有类别</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category}">${category}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="searchInterest">搜索兴趣：</label>
                            <input type="text" id="searchInterest" class="form-control" placeholder="输入关键词搜索">
                        </div>
                    </div>
                    
                    <div class="available-interests">
                        <div class="interests-grid">
                            <c:forEach items="${allInterests}" var="interest">
                                <div class="interest-checkbox category-${interest.category}" data-category="${interest.category}" data-name="${interest.name}">
                                    <label>
                                        <input type="checkbox" name="interests" value="${interest.id}" 
                                            <c:forEach items="${userInterests}" var="ui">
                                                <c:if test="${ui.interestId == interest.id}">disabled checked</c:if>
                                            </c:forEach>
                                        >
                                        <span class="interest-name">${interest.name}</span>
                                        <c:if test="${not empty interest.description}">
                                            <span class="interest-tooltip" title="${interest.description}">
                                                <i class="fa fa-info-circle"></i>
                                            </span>
                                        </c:if>
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <div class="text-center mt-3">
                        <button type="submit" class="btn btn-primary">添加选中的兴趣</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        // 兴趣爱好筛选功能
        document.addEventListener('DOMContentLoaded', function() {
            var categoryFilter = document.getElementById('categoryFilter');
            var searchInput = document.getElementById('searchInterest');
            var interestCheckboxes = document.querySelectorAll('.interest-checkbox');
            
            // 类别筛选
            categoryFilter.addEventListener('change', function() {
                var selectedCategory = this.value;
                
                interestCheckboxes.forEach(function(checkbox) {
                    if (selectedCategory === 'all' || checkbox.getAttribute('data-category') === selectedCategory) {
                        checkbox.style.display = '';
                    } else {
                        checkbox.style.display = 'none';
                    }
                });
                
                // 同时应用搜索过滤
                applySearchFilter();
            });
            
            // 搜索过滤
            searchInput.addEventListener('input', function() {
                applySearchFilter();
            });
            
            function applySearchFilter() {
                var searchValue = searchInput.value.toLowerCase();
                var selectedCategory = categoryFilter.value;
                
                interestCheckboxes.forEach(function(checkbox) {
                    var interestName = checkbox.getAttribute('data-name').toLowerCase();
                    var categoryMatch = selectedCategory === 'all' || checkbox.getAttribute('data-category') === selectedCategory;
                    var nameMatch = interestName.includes(searchValue);
                    
                    if (categoryMatch && nameMatch) {
                        checkbox.style.display = '';
                    } else {
                        checkbox.style.display = 'none';
                    }
                });
            }
            
            // 初始化工具提示
            var tooltips = document.querySelectorAll('.interest-tooltip');
            tooltips.forEach(function(tooltip) {
                tooltip.addEventListener('mouseenter', function() {
                    var title = this.getAttribute('title');
                    if (title) {
                        var tooltipElement = document.createElement('div');
                        tooltipElement.className = 'tooltip-popup';
                        tooltipElement.innerHTML = title;
                        document.body.appendChild(tooltipElement);
                        
                        var rect = this.getBoundingClientRect();
                        tooltipElement.style.left = (rect.left + rect.width / 2 - tooltipElement.offsetWidth / 2) + 'px';
                        tooltipElement.style.top = (rect.top - tooltipElement.offsetHeight - 5) + 'px';
                        
                        this.addEventListener('mouseleave', function() {
                            document.body.removeChild(tooltipElement);
                        }, { once: true });
                    }
                });
            });
        });
    </script>
</t:mainTemplate>