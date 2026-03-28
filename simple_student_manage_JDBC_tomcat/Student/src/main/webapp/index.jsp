<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>学生管理系统首页</title>
</head>

<iframe src="top.jsp"  height="100" style="float :top; width: 100%" name="top">管理界面</iframe>
<iframe src="left.jsp"  height="1024" style="float :left; width: 20%" name="left"></iframe>
<iframe src="right.jsp"  height="1024" style="float :right; width: 79%" name="right"></iframe>
<body>
<%--
    获取会话域中的数据
    如果获取到了则显示添加和查看功能的超连接
    如果没有获取到则显示登录功能的超链接
    <a href="${pageContext.request.contextPath}/addStudent.jsp">添加学生</a>
            <a href="${pageContext.request.contextPath}/list">查看学生</a>
--%>
    <c:if test="${sessionScope.username eq null}" >
   
    </c:if>
 

    <c:if test="${sessionScope.username ne null}" >
            
    </c:if>

</body>
</html>
