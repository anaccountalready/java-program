<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- <html>
<head>
<link rel="stylesheet"  type="text/css"  href="./form.css"/>
    <title>登录界面</title>
  
</head>
<body>
    <form action="${pageContext.request.contextPath}/login" method="get" autocomplete="off">
        姓名：<input type="text" name="username"><br>${msg}
        密码：<input type="password" name="password"><br>
        <button type="submit">登录</button>
    </form>
</body>
</html>--%>
<!DOCTYPE html>


<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="login.css">

    <title>登录界面</title>
</head>

<body>
    <section>
        <!-- 背景颜色 -->
        <div class="color"></div>
        <div class="color"></div>
        <div class="color"></div>
        <div class="box">
            <!-- 背景圆 -->
            <div class="circle" style="--x:0"></div>
            <div class="circle" style="--x:1"></div>
            <div class="circle" style="--x:2"></div>
            <div class="circle" style="--x:3"></div>
            <div class="circle" style="--x:4"></div>
            <!-- 登录框 -->
            <div class="container">
                <div class="form">
                    <h2>登录</h2>
                    <form action="${pageContext.request.contextPath}/login" method="get" autocomplete="off">
                   
                        <div class="inputBox">
                            <input name="username" type="text" placeholder="姓名">${msg}

                        </div>
                        <div class="inputBox">
                            <input name="password" type="password" placeholder="密码">${pwd }

                        </div>
                        <div class="inputBox">
                            <input type="submit" value="登录">

                        </div>
             
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>

</html>

