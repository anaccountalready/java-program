<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
    String delResult = (String)session.getAttribute("delResult");
    if(delResult != null) {
        session.removeAttribute("delResult");
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet"  type="text/css"  href="./form.css"/>
<title>删除学生</title>
</head>

<body>
<form action="${pageContext.request.contextPath}/delstu" method="get" autocomplete="off">
        学号：<input type="text" name="stu_id">
     <button type="submit">删除</button>
</form>

</body>
<script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<script>
var delResult = "<%= delResult != null ? delResult : "" %>";
if(delResult == "success") {
    alert("删除成功");
} else if(delResult == "fail") {
    alert("学号错误，请重新输入！");
}
</script>
</html>