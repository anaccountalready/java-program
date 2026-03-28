<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
if("${t_error}"==1)
alert("学号错误，请重新输入！");
else
	alert("删除成功");
</script>
</html>