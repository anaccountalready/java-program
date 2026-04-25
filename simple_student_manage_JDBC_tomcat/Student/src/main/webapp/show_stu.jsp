<%@page import="cn.nankai.edu.cn.JDBCemo"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page import="java.util.Vector" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"  type="text/css"  href="./table.css"/>
<meta charset="utf-8">
<title>学生信息</title>
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
<form action="${pageContext.request.contextPath}/show_stu" method="get" autocomplete="off">
<input name="showall" type="submit" value="展示所有学生"/>
<table>
<tr>
<td>专业名称：<select name=major>
        <option value="计算机科学与技术专业">计算机科学与技术专业</option>
        <option value="网络空间安全与技术专业">网络空间安全与技术专业</option>
        <option value="物联网工程专业">物联网工程专业</option>
        
        </select></td>
<td> 班级名称：<select name="class"><% 
        for(int i=0;i<class_name.length;i++)
        {%>
        <option value=<%=class_name[i] %>><%=class_name[i] %></option>
        <%} %>
  </select><td>
  <td>
  学生性别：<select name="sex">
       
        <option value="女">女</option>
        <option value="男">男</option>
        </select>
        </td>
   <td>
  班导<input name="tea" type="text" >
   </td>
    <td>
  学号<input name="id" type="text" >
   </td>
   
</tr>
</table>
 <button type="submit">确定</button>
 <button type="button" class="export-btn" onclick="exportExcel()">导出Excel</button>
<table>
<tr>
<td>学号</td>
<td>姓名</td>
<td>性别</td>
<td>入学日期</td>
<td>专业</td>
<td>班级</td>
<td>班导</td>
</tr>
<c:forEach begin="0" end="${countstu - 1}" items ="${student}" var="stu" varStatus="status">
<c:if test="${status.index < countstu}">
<tr>
<td>${stu.id}</td>
<td> ${stu.name }</td>
<td> ${stu.sex }</td>
<td>${stu.indate }</td>
<td> ${stu.major }</td>
<td>${stu.claname }</td>
 
<td> ${stu.teacher }
</td>
</tr>
</c:if>
</c:forEach>
</table>

<c:if test="${pageBean != null && pageBean.totalPage > 0}">
<div class="pagination">
    <c:set var="searchParams" value="${searchParams != null ? searchParams : ''}" />
    
    <c:if test="${pageBean.hasPrevious}">
        <a href="${pageContext.request.contextPath}/show_stu?page=1${searchParams}">首页</a>
        <a href="${pageContext.request.contextPath}/show_stu?page=${pageBean.previousPage}${searchParams}">上一页</a>
    </c:if>
    <c:if test="${!pageBean.hasPrevious}">
        <a href="#" class="disabled">首页</a>
        <a href="#" class="disabled">上一页</a>
    </c:if>
    
    <span>第 ${pageBean.currentPage} 页 / 共 ${pageBean.totalPage} 页</span>
    <span>共 ${pageBean.totalCount} 条记录</span>
    
    <c:if test="${pageBean.hasNext}">
        <a href="${pageContext.request.contextPath}/show_stu?page=${pageBean.nextPage}${searchParams}">下一页</a>
        <a href="${pageContext.request.contextPath}/show_stu?page=${pageBean.totalPage}${searchParams}">末页</a>
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
    window.location.href = '${pageContext.request.contextPath}/exportStudent';
}
</script>

<%!String [] class_name={
        	"计算机科学与技术一班",
        	"计算机科学与技术二班",
        	"计算机科学与技术三班",
        	"网络空间安全与技术一班",
        	"网络空间安全与技术二班",
        	"网络空间安全与技术三班",
        	"物联网工程一班",
        	"物联网工程二班",
        	"物联网工程三班"
        };%>
         <%!String []a={"男","女"};
        %>
</body>
</html>
