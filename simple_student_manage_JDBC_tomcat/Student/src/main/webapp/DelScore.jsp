<%@page import="java.sql.ResultSet"%>
<%@page import="cn.nankai.edu.cn.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page import="java.util.Vector" %>
<!DOCTYPE html>

<html>
<head>
  <link rel="stylesheet"  type="text/css"  href="./table.css"/>
<meta charset="utf-8">
<title>管理学生成绩</title>
</head>

<input type="hidden" id="checkID" name="checkID"/>
<body>
 <form action="${pageContext.request.contextPath}/Del" method="get" autocomplete="off">
 
 
<table>
<tr>
<td>
学号<input name="id" type="text" ></td>
<td>课程名称  <input name="course" type="text" ></td>
 <td><button type="submit">查看当前所有(或所选)信息</button></td>
  <td> <button type="submit" name="delsel">删除所选条件的成绩信息</button>
   </td>
  <td><input  type="button" value="批量删除" onclick="delall()"></td>

</table>

<table>
<tr>
<td>选择</td>
<td>学号</td>
<td>课程名称</td>
<td>成绩</td>
<td>操作</td>
</tr>
</table>
<div id="dvInput" class="readonly">
<table>
<c:if test="${count>0 }">
<c:forEach begin="0" end="${count-1 }" items ="${stucour}" var="stu">
<tr>
<td><input type="checkbox" name="check" value="${stu.couid}_${stu.stuid}"></td>
<td >${stu.stuid}</td>
<td> ${stu.couname }</td>
<td><input name="${stu.stuid}_${stu.couid}" id="value1" type="text"  value="${stu.score}" readonly> </td>
 <td><input name="${stu.stuid}_${stu.couid}" type="button" value="修改成绩" id="btnx"  onclick="btnClick(this)"/></td>
</tr>
</c:forEach>
</c:if>

</table>
</div>
<script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<script>
    function btnClick(btn) {
        var toEdit = btn.value == '修改成绩';
        var change=btn.getAttribute("name");
       // alert("chane"+change);
        $('#dvInput')[toEdit ? 'removeClass' : 'addClass']('readonly').find(':input').attr('readonly', toEdit ? false : true);
        btn.value = toEdit ? '保存' : '修改成绩';
        if (!toEdit) {
        	var value1 = document.getElementsByName(change)[0];
        //	alert("value1.value"+value1.value+"_"+change);
        	 location.href="${pageContext.request.contextPath }/Del?newscore="+value1.value+"_"+change;
        }
    }
</script>
</form>
<script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<script>

function delall(){
alert("确定要删除吗？");

var checkID=new Array();

	$("input[type='checkbox']:checked").each(function (i) {
		checkID.push($(this).val());
	});
	alert("删除成绩的课程号为"+checkID);
	
if(checkID.length==0){alert("请选择需要删除的行");}
else{
	
	var checkid=checkID.join(',');
	 location.href="${pageContext.request.contextPath }/Del?checkID="+checkid;

	
	}
return false;
}
</script>
</body>


</html>