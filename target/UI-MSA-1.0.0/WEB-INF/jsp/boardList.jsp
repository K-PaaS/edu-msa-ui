<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Micro Service Architecture Board</title>
<style type="text/css">
html, body {
	margin: 0;
	padding: 0;
	height: 100%;
}

div.header {
	margin: 0;
	padding: 0;
	height: 15%;
}

div.body {
	margin: 0;
	padding: 0;
	height: 70%;
	margin: 0% 15%;
	/* 	background-color: #EEEEEE; */
}

div.footer {
	margin: 0;
	padding: 0;
	height: 15%;
}

fieldset {
	margin: 0;
	padding: 0;
	border: 0px 0px;
}

table {
	margin: 0;
	padding: 0;
	border: 0px;
    border-collapse: collapse;
	width: 100%;
	border-top: solid 2px;
	border-bottom: solid 2px;
}

table thead {
	height: 30px;
	border-top: solid 1px;
	border-bottom: solid 1px #AAAAAA;
}

table tbody tr {
	border-bottom: solid 1px #404040;
	height: 25px;
}

.pagination {
	width: 100%;
	text-align: center;
	margin: 1% 0;
}

.pagination a {
	color: black;
	padding: 8px 16px;
	text-decoration: none;
}

.pagination .selected {
	font-weight: bold !important;
}

.searchArea {
	text-align: right;
}

/* selectbox ������ ����*/
select {

    -webkit-appearance: none;  /* ����Ƽ�� ���� ���߱� */
    -moz-appearance: none;
    appearance: none;
    
    width: 200px; /* ���ϴ� �ʺ��� */
    padding: .8em .5em; /* �������� ���� ���� */
    font-family: inherit;  /* ��Ʈ ��� */
    background: url('/images/select_icon.jpg') no-repeat 95% 50%; /* ����Ƽ�� ȭ��ǥ�� Ŀ���� ȭ��ǥ�� ��ü */
    border: 1px solid #999;
    border-radius: 0px; /* iOS �ձٸ𼭸� ���� */
    -webkit-appearance: none; /* ����Ƽ�� ���� ���߱� */
    -moz-appearance: none;
    appearance: none;
    
}

/* IE 10, 11�� ����Ƽ�� ȭ��ǥ ����� */
select::-ms-expand {
    display: none;
}
/* selectbox ������ ����*/

</style>

</head>
<body>
	<div class="header"></div>
	<div class="body">
		<article>
			<table>
				<thead>
					<tr>
						<th style="width:10%;">��ȣ</th>
						<th style="width:60%;">����</th>
						<th style="width:10%;">�ۼ���</th>
						<th style="width:20%;">�ۼ���</th>
					</tr>
				</thead>
				<tbody id="list">
					<tr>
						<td>1</td>
						<td>1</td>
						<td>1</td>
						<td>1</td>
					</tr>
				</tbody>
			</table>
			<div class="searchArea">
				<select name="searchType">
					<option value="aa">����</option>
					<option value="bb">�ۼ���</option>
				</select>
				<input type="text" name="searchValue" />
			</div>
			<div class="pagination">
				<a href="#">&laquo;</a>
				<a href="#" class="selected">1</a>
				<a href="#">2</a>
				<a href="#">3</a>
				<a href="#">4</a>
				<a href="#">5</a>
				<a href="#">6</a>
				<a href="#">&raquo;</a>
			</div>
		</article>
	</div>
	<div class="footer"></div>
</body>
</html>

