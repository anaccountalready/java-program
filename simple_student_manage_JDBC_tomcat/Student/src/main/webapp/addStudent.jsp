<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%%>
<html>
<head>
    <title>添加学生</title>
    <link rel="stylesheet"  type="text/css"  href="./form.css"/>
</head>

<body>
    <form action="${pageContext.request.contextPath}/add" method="get" autocomplete="off">
   
        学号  ：<input type="text" name="stu_id"><br>
        学生姓名：<input type="text" name="stu_name"><br>
        学生性别：<select name="sex">
       
        <option value="女">女</option>
        <option value="男">男</option>
        </select><br>
        入学日期：<input type="date" name="stu_indate"><br>
        班级名称：<select name="class"><% 
        for(int i=0;i<class_name.length;i++)
        {%>
        <option value=<%=class_name[i] %>><%=class_name[i] %></option>
        <%} %>
        
        </select><br>
        专业名称：<select name=major>
        <option value="计算机科学与技术专业">计算机科学与技术专业</option>
        <option value="网络空间安全与技术专业">网络空间安全与技术专业</option>
        <option value="物联网工程专业">物联网工程专业</option>
        
        </select><br>
        班导  ：<input type="text" name="tea"><br>
       
        <button type="submit">保存</button>
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
         <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.4.2.min.js"></script>
<script>
if("${addstuteaid}"==null||"${addtea}"==null||"${addtea}"==""||"${addstuteaid}"=="")
alert("未指定教师或教师姓名错误，违反触发器，添加失败！");
else
	alert("添加学生成功");
</script>
</body>
</html>

