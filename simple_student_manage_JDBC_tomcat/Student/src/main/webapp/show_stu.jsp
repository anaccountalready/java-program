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
<c:forEach begin="0" end="${countstu }" items ="${student}" var="stu">
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

</c:forEach>
</table>
</form>
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
