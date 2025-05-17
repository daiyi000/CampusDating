<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:mainTemplate pageTitle="编辑个人资料" cssFile="profile">
    <div class="edit-profile-container">
        <form action="${pageContext.request.contextPath}/profile" method="post" id="editProfileForm" enctype="multipart/form-data">
            <input type="hidden" name="action" value="save">
            
            <!-- 头像上传区域 -->
            <div class="avatar-upload-section">
                <div class="current-avatar">
                    <img src="${not empty profile.avatarUrl ? profile.avatarUrl : pageContext.request.contextPath.concat('/assets/images/default-avatar.png')}" 
                         alt="${sessionScope.user.username}" id="avatarPreview">
                </div>
                <div class="avatar-upload">
                    <label for="avatarFile" class="btn btn-secondary">
                        <i class="fa fa-camera"></i> 更换头像
                    </label>
                    <input type="file" id="avatarFile" name="avatarFile" accept="image/*" style="display: none;">
                </div>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h3>基本信息</h3>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="fullName">姓名</label>
                                <input type="text" id="fullName" name="fullName" class="form-control" value="${profile.fullName}">
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="gender">性别</label>
                                <select id="gender" name="gender" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="男" ${profile.gender == '男' ? 'selected' : ''}>男</option>
                                    <option value="女" ${profile.gender == '女' ? 'selected' : ''}>女</option>
                                    <option value="其他" ${profile.gender == '其他' ? 'selected' : ''}>其他</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="birthday">生日</label>
                                <input type="date" id="birthday" name="birthday" class="form-control" value="${not empty profile.birthday ? profile.birthday : ''}">
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="zodiacSign">星座</label>
                                <select id="zodiacSign" name="zodiacSign" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="白羊座" ${profile.zodiacSign == '白羊座' ? 'selected' : ''}>白羊座</option>
                                    <option value="金牛座" ${profile.zodiacSign == '金牛座' ? 'selected' : ''}>金牛座</option>
                                    <option value="双子座" ${profile.zodiacSign == '双子座' ? 'selected' : ''}>双子座</option>
                                    <option value="巨蟹座" ${profile.zodiacSign == '巨蟹座' ? 'selected' : ''}>巨蟹座</option>
                                    <option value="狮子座" ${profile.zodiacSign == '狮子座' ? 'selected' : ''}>狮子座</option>
                                    <option value="处女座" ${profile.zodiacSign == '处女座' ? 'selected' : ''}>处女座</option>
                                    <option value="天秤座" ${profile.zodiacSign == '天秤座' ? 'selected' : ''}>天秤座</option>
                                    <option value="天蝎座" ${profile.zodiacSign == '天蝎座' ? 'selected' : ''}>天蝎座</option>
                                    <option value="射手座" ${profile.zodiacSign == '射手座' ? 'selected' : ''}>射手座</option>
                                    <option value="摩羯座" ${profile.zodiacSign == '摩羯座' ? 'selected' : ''}>摩羯座</option>
                                    <option value="水瓶座" ${profile.zodiacSign == '水瓶座' ? 'selected' : ''}>水瓶座</option>
                                    <option value="双鱼座" ${profile.zodiacSign == '双鱼座' ? 'selected' : ''}>双鱼座</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="height">身高 (cm)</label>
                                <input type="number" id="height" name="height" class="form-control" value="${profile.height > 0 ? profile.height : ''}">
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="weight">体重 (kg)</label>
                                <input type="number" id="weight" name="weight" class="form-control" value="${profile.weight > 0 ? profile.weight : ''}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h3>学校信息</h3>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="school">学校</label>
                                <input type="text" id="school" name="school" class="form-control" value="${profile.school}">
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="department">院系</label>
                                <input type="text" id="department" name="department" class="form-control" value="${profile.department}">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="major">专业</label>
                                <input type="text" id="major" name="major" class="form-control" value="${profile.major}">
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="grade">年级</label>
                                <select id="grade" name="grade" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="1" ${profile.grade == 1 ? 'selected' : ''}>大一</option>
                                    <option value="2" ${profile.grade == 2 ? 'selected' : ''}>大二</option>
                                    <option value="3" ${profile.grade == 3 ? 'selected' : ''}>大三</option>
                                    <option value="4" ${profile.grade == 4 ? 'selected' : ''}>大四</option>
                                    <option value="5" ${profile.grade == 5 ? 'selected' : ''}>研一</option>
                                    <option value="6" ${profile.grade == 6 ? 'selected' : ''}>研二</option>
                                    <option value="7" ${profile.grade == 7 ? 'selected' : ''}>研三</option>
                                    <option value="8" ${profile.grade == 8 ? 'selected' : ''}>博一</option>
                                    <option value="9" ${profile.grade == 9 ? 'selected' : ''}>博二</option>
                                    <option value="10" ${profile.grade == 10 ? 'selected' : ''}>博三</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card mt-4">
                <div class="card-header">
                    <h3>个人信息</h3>
                </div>
                <div class="card-body">
                    <div class="form-group">
                        <label for="hometown">家乡</label>
                        <input type="text" id="hometown" name="hometown" class="form-control" value="${profile.hometown}">
                    </div>
                    
                    <div class="row">
                        <div class="col-6">
                            <div class="form-group">
                                <label for="relationshipStatus">恋爱状态</label>
                                <select id="relationshipStatus" name="relationshipStatus" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="单身" ${profile.relationshipStatus == '单身' ? 'selected' : ''}>单身</option>
                                    <option value="恋爱中" ${profile.relationshipStatus == '恋爱中' ? 'selected' : ''}>恋爱中</option>
                                    <option value="暗恋中" ${profile.relationshipStatus == '暗恋中' ? 'selected' : ''}>暗恋中</option>
                                    <option value="保密" ${profile.relationshipStatus == '保密' ? 'selected' : ''}>保密</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="form-group">
                                <label for="lookingFor">寻找</label>
                                <select id="lookingFor" name="lookingFor" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="恋爱" ${profile.lookingFor == '恋爱' ? 'selected' : ''}>恋爱</option>
                                    <option value="友谊" ${profile.lookingFor == '友谊' ? 'selected' : ''}>友谊</option>
                                    <option value="学习伙伴" ${profile.lookingFor == '学习伙伴' ? 'selected' : ''}>学习伙伴</option>
                                    <option value="未确定" ${profile.lookingFor == '未确定' ? 'selected' : ''}>未确定</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="personalityType">性格类型</label>
                        <select id="personalityType" name="personalityType" class="form-control">
                            <option value="">请选择</option>
                            <option value="INTJ" ${profile.personalityType == 'INTJ' ? 'selected' : ''}>INTJ - 建筑师</option>
                            <option value="INTP" ${profile.personalityType == 'INTP' ? 'selected' : ''}>INTP - 逻辑学家</option>
                            <option value="ENTJ" ${profile.personalityType == 'ENTJ' ? 'selected' : ''}>ENTJ - 指挥官</option>
                            <option value="ENTP" ${profile.personalityType == 'ENTP' ? 'selected' : ''}>ENTP - 辩论家</option>
                            <option value="INFJ" ${profile.personalityType == 'INFJ' ? 'selected' : ''}>INFJ - 提倡者</option>
                            <option value="INFP" ${profile.personalityType == 'INFP' ? 'selected' : ''}>INFP - 调停者</option>
                            <option value="ENFJ" ${profile.personalityType == 'ENFJ' ? 'selected' : ''}>ENFJ - 主人公</option>
                            <option value="ENFP" ${profile.personalityType == 'ENFP' ? 'selected' : ''}>ENFP - 竞选者</option>
                            <option value="ISTJ" ${profile.personalityType == 'ISTJ' ? 'selected' : ''}>ISTJ - 物流师</option>
                            <option value="ISFJ" ${profile.personalityType == 'ISFJ' ? 'selected' : ''}>ISFJ - 守卫者</option>
                            <option value="ESTJ" ${profile.personalityType == 'ESTJ' ? 'selected' : ''}>ESTJ - 总经理</option>
                            <option value="ESFJ" ${profile.personalityType == 'ESFJ' ? 'selected' : ''}>ESFJ - 执政官</option>
                            <option value="ISTP" ${profile.personalityType == 'ISTP' ? 'selected' : ''}>ISTP - 鉴赏家</option>
                            <option value="ISFP" ${profile.personalityType == 'ISFP' ? 'selected' : ''}>ISFP - 探险家</option>
                            <option value="ESTP" ${profile.personalityType == 'ESTP' ? 'selected' : ''}>ESTP - 企业家</option>
                            <option value="ESFP" ${profile.personalityType == 'ESFP' ? 'selected' : ''}>ESFP - 表演者</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="bio">个人介绍</label>
                        <textarea id="bio" name="bio" class="form-control" rows="5">${profile.bio}</textarea>
                    </div>
                </div>
            </div>
            
            <div class="form-group text-center mt-4">
                <button type="submit" class="btn btn-primary">保存资料</button>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary ml-2">取消</a>
            </div>
        </form>
    </div>
    
    <script>
        // 头像预览
        document.getElementById('avatarFile').addEventListener('change', function(e) {
            if (e.target.files && e.target.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('avatarPreview').src = e.target.result;
                }
                reader.readAsDataURL(e.target.files[0]);
            }
        });
        
        // 生日自动计算星座
        document.getElementById('birthday').addEventListener('change', function(e) {
            if (e.target.value) {
                var date = new Date(e.target.value);
                var month = date.getMonth() + 1;
                var day = date.getDate();
                
                var zodiacSign = '';
                if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
                    zodiacSign = '水瓶座';
                } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
                    zodiacSign = '双鱼座';
                } else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
                    zodiacSign = '白羊座';
                } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
                    zodiacSign = '金牛座';
                } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
                    zodiacSign = '双子座';
                } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
                    zodiacSign = '巨蟹座';
                } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
                    zodiacSign = '狮子座';
                } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
                    zodiacSign = '处女座';
                } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
                    zodiacSign = '天秤座';
                } else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
                    zodiacSign = '天蝎座';
                } else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
                    zodiacSign = '射手座';
                } else {
                    zodiacSign = '摩羯座';
                }
                
                document.getElementById('zodiacSign').value = zodiacSign;
            }
        });
    </script>
</t:mainTemplate>