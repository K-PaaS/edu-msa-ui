<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="shortcut icon" href="../../images/favicon.ico" type="image/x-icon" />
	<link href='http://fonts.googleapis.com/css?family=Roboto&subset=latin,greek,greek-ext,latin-ext,cyrillic' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" href="../../css/layout.css">
	<script src="/js/jquery-3.6.0.min.js"></script>
	<title>파스-타 활용 교육실습포털</title>
</head>
<script>
$(document).ready(function(){
	$('#updateBtn').click(function(){
		// ajax 타는 부분
		var result = confirm('수정하시겠습니까?');			
		if(result) {
			var paramMap = new Map();
			/* paramMap["commentSeq"] = $('#comment-input').val(); */
			paramMap["boardSeq"] = $('input[name="boardSeq"]').val();
			paramMap["boardTitle"] = $('#boardTitle').val();
			paramMap["boardText"] = $('#boardText').val();
			paramMap["writeUserId"] = '${cookie.user_id.value}';
			paramMap["writeUserName"] = '${cookie.user_name.value}';
			$.ajax({
			    url:'./boardUpdateJSON',
		        type:'post',
		        data: paramMap,
		        dataType:'json',
			    success:function(data){
			    	if(data.result == 'SUCCESS') {
			    		alert("정상적으로 수정하였습니다");
			    		location.href = 'boardDetail?boardSeq=' + $('input[name="boardSeq"]').val();
			    	} else {
			    		alert("수정에 실패하였습니다.");
			    	}
			    }
			});
		}
	});
	
	$('#cancelBtn').click(function(){
		location.href = 'boardDetail?boardSeq=' + $('input[name="boardSeq"]').val();
	});
});
</script>
<body>
	<div id="wrap">
		<!-- Header -->
		<header>
		<form id="writeForm">
			<input type="hidden" name="writeUserName" value="${resultData.userName}" /><!-- 추후 로그인 처리 후 로그인 사용자명 -->
			<input type="hidden" name="writeUserId" value="${resultData.userId}" /><!-- 추후 로그인 처리 후 로그인 사용자ID -->
			<input type="hidden" name="boardSeq" value="${resultData.boardSeq}" />
		</form>
			<div>
				<h1>
					<a href="<c:url value='/board' />"><img src="../../images/logo_header.png" alt="PaaS-TA로고"></a>
				</h1>
				<div>
					<c:if test="${cookie.session_id.value eq null}">
						<a href="<c:url value='/user/join' />">회원가입</a>
						<a href="<c:url value='/user/login' />">로그인</a>
					</c:if>
					<c:if test="${cookie.session_id.value ne null}">
						<a href="<c:url value='/user/userInfo' />?userId=${cookie.user_id.value}">My Page</a>
						<a href="<c:url value='/user/logout' />">로그아웃</a>
					</c:if>
				</div>
			</div>
		</header>
		<!-- // Header -->
		<!-- Container -->
		<div id="content" class="noticeWrite">
            <div>
                <h5>게시판 수정</h5>
                <div class="write">
                    <dl>
                        <dt><label for="entitle01">제목</label></dt>
                        <dd>
                            <input type="text" id="boardTitle" name="boardTitle" autocomplete="off" id="entitle01" placeholder="제목을 입력해주세요" value="${resultData.boardTitle}" />
                        </dd>
                    </dl>
                    <dl>
                        <dt>내용</dt>
                        <dd>
                            <textarea id="boardText" name="boardText" title="내용" placeholder="내용을 작성해 주세요.">${resultData.boardText}</textarea>
                        </dd>
                    </dl>
                </div>
                <div class="btn03">
                    <a id="cancelBtn" href="#">취소</a>
                    <button  id="updateBtn" type="submit">수정</button>
                </div>
            </div>
		</div>
		<!-- //Container -->
		<!-- Footer -->
        <footer>
            <div>
                <div class="company">
                    <ul>
                        <li>
                            <a href="#">COMPANY</a>
                        </li>
                        <li>
                            <a href="#">사이트 이용약관</a>
                        </li>
                        <li>
                            <a href="#">개인정보취급방침</a>
                        </li>
                    </ul>
                    <div>
                        <a href="https://www.facebook.com/PaaS-TA-136351040351980/"><img src="../images/facebook.png" target="_blank" alt="페이스북" /></a>
                        <a href="https://twitter.com/PaaS_TA" target="_blank"><img src="../images/tw.png" alt="트위터" /></a>
                    </div>
                </div>
                <div class="company-info">
					<div>
						<h5>개방형 클라우드플랫폼 센터 PaaS-TA</h5>
						<address>서울시 중구 세종대로 39 서울상공회의소 7F</address>
						<a href="#">위치안내</a>
					</div>
					<div>
						<h5>개방형 클라우드 플랫폼센터</h5>
						<dl>
							<dt>문의전화</dt>
							<dd>1522-0089</dd>
						</dl>
						<dl>
							<dt>문의메일</dt>
							<dd><a href="mailto:paasta@paas-ta.kr">paasta@paas-ta.ta.kr</a></dd>
						</dl>
					</div>
					<p>COPYRIGHT 2020 PaaS-TA. ALL RIGHTS RESERVED.</p>
                </div>
            </div>
        </footer>
        <!-- // Footer -->
	</div>
</body>
</html>