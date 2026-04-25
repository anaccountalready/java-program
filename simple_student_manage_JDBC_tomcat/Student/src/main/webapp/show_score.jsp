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
<style>
button,
input[type="submit"],
input[type="button"] {
    display: inline-block !important;
    padding: 12px 24px !important;
    font-size: 14px !important;
    font-weight: normal !important;
    line-height: 1.5 !important;
    cursor: pointer !important;
    text-align: center !important;
    text-decoration: none !important;
    outline: none !important;
    color: #ffffff !important;
    background-color: #800080 !important;
    border: none !important;
    border-radius: 6px !important;
    box-shadow: 0 3px #999 !important;
    min-width: 80px !important;
    margin: 5px 5px !important;
    -webkit-appearance: none !important;
    appearance: none !important;
}

button:hover,
input[type="submit"]:hover,
input[type="button"]:hover {
    background-color: #ff80ff !important;
}

button:active,
input[type="submit"]:active,
input[type="button"]:active {
    background-color: #ff00ff !important;
    box-shadow: 0 3px #666 !important;
    transform: translateY(4px) !important;
}
</style>
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
