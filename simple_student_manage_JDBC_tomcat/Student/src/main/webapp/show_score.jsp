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

.pagination {
    margin: 20px 0;
    padding: 10px;
    text-align: center;
}

.pagination a {
    display: inline-block;
    padding: 8px 16px;
    margin: 0 4px;
    text-decoration: none;
    color: #800080;
    border: 1px solid #800080;
    border-radius: 4px;
}

.pagination a:hover {
    background-color: #ff80ff;
    color: white;
}

.pagination a.disabled {
    color: #999;
    border-color: #999;
    pointer-events: none;
    cursor: default;
}

.pagination span {
    display: inline-block;
    padding: 8px 16px;
    margin: 0 4px;
    color: #800080;
    font-weight: bold;
}

.export-btn {
    background-color: #28a745 !important;
}

.export-btn:hover {
    background-color: #218838 !important;
}

.export-btn:active {
    background-color: #1e7e34 !important;
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
<td>最高分：<input type="text" name="high">
</td>
</tr>
</table>
<button type="submit">查看所有或已选条件的学分绩信息</button>
<button type="button" class="export-btn" onclick="exportExcel()">导出Excel</button>
<table><tr>
<td>学号</td>
<td>学分绩</td>
</tr>
<c:forEach begin="0" end="${countavg - 1}" items ="${avg_score}" var="avg" varStatus="status">
<c:if test="${status.index < countavg}">
<tr>
<td>${avg.stu_id}</td>
<td> ${avg.avg_score }</td>
</tr>
</c:if>
</c:forEach>
</table>

<c:if test="${pageBean != null && pageBean.totalPage > 0}">
<div class="pagination">
    <c:set var="searchParams" value="${searchParams != null ? searchParams : ''}" />
    
    <c:if test="${pageBean.hasPrevious}">
        <a href="${pageContext.request.contextPath}/Show_score?page=1${searchParams}">首页</a>
        <a href="${pageContext.request.contextPath}/Show_score?page=${pageBean.previousPage}${searchParams}">上一页</a>
    </c:if>
    <c:if test="${!pageBean.hasPrevious}">
        <a href="#" class="disabled">首页</a>
        <a href="#" class="disabled">上一页</a>
    </c:if>
    
    <span>第 ${pageBean.currentPage} 页 / 共 ${pageBean.totalPage} 页</span>
    <span>共 ${pageBean.totalCount} 条记录</span>
    
    <c:if test="${pageBean.hasNext}">
        <a href="${pageContext.request.contextPath}/Show_score?page=${pageBean.nextPage}${searchParams}">下一页</a>
        <a href="${pageContext.request.contextPath}/Show_score?page=${pageBean.totalPage}${searchParams}">末页</a>
    </c:if>
    <c:if test="${!pageBean.hasNext}">
        <a href="#" class="disabled">下一页</a>
        <a href="#" class="disabled">末页</a>
    </c:if>
</div>
</c:if>

</form>

<script>
function exportExcel() {
    window.location.href = '${pageContext.request.contextPath}/exportScore';
}
</script>

</body>
</html>
