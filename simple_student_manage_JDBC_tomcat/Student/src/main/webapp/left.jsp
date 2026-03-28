<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet"  type="text/css"  href="./Ul.css"/>
</head>
<style>

body
{
	background-color:#b0c4de;
}
</style>
<%--<div class="main_left" style="height:80%"> --%>
 <ul >       
 <li>学生管理
                    
  <dl>
      
       <dd><a href="${pageContext.request.contextPath}/addStudent.jsp" target="right">添加学生</a></dd>
       <dd><a href="${pageContext.request.contextPath}/del_stu.jsp" target="right">删除学生</a></dd>
      <dd><a href="${pageContext.request.contextPath}/show_stu.jsp" target="right">查看学生信息</a></dd>
      <dd><a href="${pageContext.request.contextPath}/show_score.jsp" target="right">查看学生学分绩</a></dd>
      <dd><a href="${pageContext.request.contextPath}/DelScore.jsp" target="right">管理学生成绩</a></dd>
      <dd><a href="${pageContext.request.contextPath}/show_cla.jsp" target="right">查看班级信息</a></dd>
      
      </dl>
  </li></ul>
  </html>
 <%-- </div>--%> 