<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page import="java.util.Vector" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"  type="text/css"  href="./table.css"/>
<meta charset="utf-8">
<title>查看班级信息</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/Show_cla" method="get" autocomplete="off">
<button type="submit">查看</button>
<table><tr>
<td>名称</td>
<td>人数</td>
<td>班导</td>
</tr>
<c:forEach begin="0" end="${countcla }" items ="${clainfo}" var="cla">
<tr>
<td>${cla.name}</td>
<td> ${cla.num }</td>
<td> ${cla.teaname }</td>
</tr>
</c:forEach>
</table>
</form>
</body>
</html>