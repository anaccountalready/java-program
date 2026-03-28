<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page import="java.util.Vector" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"  type="text/css"  href="./table.css"/>
<meta charset="utf-8">
<title>学分绩</title>
</head>

<body>
<form action="${pageContext.request.contextPath}/Show_score" method="get" autocomplete="off">
<table>
<tr>
<td>学号：<input type="text" name="stu_id"></td>
<td>
最低分：<input type="text" name="low">
</td>
<td>最高分：<input type="text" name="hign"></td>
</tr>
</table>
<button type="submit">查看所有或已选条件的学分绩信息</button>
<table><tr>
<td>学号</td>
<td>学分绩</td>
</tr>
<c:forEach begin="0" end="${countavg}" items ="${avg_score}" var="avg">
<tr>
<td>${avg.stu_id}</td>
<td> ${avg.avg_score }</td>
</tr>
</c:forEach>
</table>
</form>
</body>
</html>