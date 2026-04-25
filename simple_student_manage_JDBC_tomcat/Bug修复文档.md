# 学生管理系统 Bug 修复文档

## 修复日期
2026-04-25

## 修复概述
本次修复共解决了项目中的 5 个主要 Bug，涉及登录安全、页面交互逻辑、数据展示和 UI 样式等方面。

---

## Bug 1：登录绕过问题

### 问题描述
当前实现可以越过登录直接访问管理页面的路由，未登录用户可以直接通过 URL 访问如 `show_stu.jsp`、`addStudent.jsp` 等管理页面。

### 问题原因
项目中缺少登录过滤器（LoginFilter），没有对请求进行身份验证。虽然 `LoginAdminServlet` 会在登录成功后将用户名存入 Session，但没有任何机制检查后续请求是否已登录。

### 修复方案
创建 `LoginFilter.java` 过滤器，使用 `@WebFilter("/*")` 注解拦截所有请求：

1. **白名单机制**：允许访问的页面和资源包括：
   - `login.jsp` - 登录页面
   - `/login` - 登录 Servlet
   - `login.css` - 登录样式
   - `index.jsp` - 首页
   - 静态资源（css、js、images、icon 等）

2. **验证逻辑**：
   - 检查 Session 中是否存在 `username` 属性
   - 未登录用户重定向到 `login.jsp`
   - 已登录用户继续访问请求资源

### 修改文件
- 新增：`src/main/java/cn/nankai/edu/cn/LoginFilter.java`

---

## Bug 2：添加学生页面进入即触发错误

### 问题描述
点击添加学生时，刚进入页面就弹出"未指定教师或教师姓名错误，违反触发器，添加失败！"的提醒。该提醒应该在点击保存按钮后才会触发。

### 问题原因
`addStudent.jsp` 中的 JavaScript 代码无条件执行：
```javascript
if("${addstuteaid}"==null||"${addtea}"==null||"${addtea}"==""||"${addstuteaid}"=="")
alert("未指定教师或教师姓名错误，违反触发器，添加失败！");
else
    alert("添加学生成功");
```

当首次进入页面时，`addstuteaid` 和 `addtea` 都是空值，导致错误提示立即弹出。

### 修复方案
1. **添加操作结果标记**：在 `AddstudentServlet.java` 中添加 `addResult` 属性：
   - `"success"` - 添加成功
   - `"fail"` - 添加失败

2. **修改 JSP 逻辑**：
   - 在页面顶部读取并清除 Session 中的结果属性（避免刷新页面重复提示）
   - JavaScript 只在 `addResult` 有值时才显示提示

3. **完善 Servlet 逻辑**：
   - 检查 `teaid` 和 `tea` 是否为空
   - 空值时设置 `addResult = "fail"`
   - 成功插入后设置 `addResult = "success"`
   - 异常时设置 `addResult = "fail"`

### 修改文件
- `src/main/webapp/addStudent.jsp`
- `src/main/java/cn/nankai/edu/cn/AddstudentServlet.java`

---

## Bug 3：删除学生页面进入即触发删除

### 问题描述
点击删除学生时，刚进入页面就弹出"删除成功"的提醒。该提醒应该在点击删除按钮后才会触发。

### 问题原因
`del_stu.jsp` 中的 JavaScript 代码逻辑有缺陷：
```javascript
if("${t_error}"==1)
alert("学号错误，请重新输入！");
else
    alert("删除成功");
```

当首次进入页面时，`t_error` 不存在（EL 表达式输出空字符串），不等于 1，所以会走 else 分支显示"删除成功"。

### 修复方案
1. **添加操作结果标记**：在 `delstuServelet.java` 中添加 `delResult` 属性：
   - `"success"` - 删除成功
   - `"fail"` - 删除失败

2. **修改 JSP 逻辑**：
   - 在页面顶部读取并清除 Session 中的 `delResult` 属性
   - JavaScript 只在 `delResult` 有值时才显示提示

3. **完善 Servlet 逻辑**：
   - 检查 `stu_id` 参数是否为空
   - 添加 `NumberFormatException` 异常处理
   - 根据存储过程返回值 `output` 判断结果：
     - `output == 1` 表示失败
     - 其他值表示成功

### 修改文件
- `src/main/webapp/del_stu.jsp`
- `src/main/java/cn/nankai/edu/cn/delstuServelet.java`

---

## Bug 4：查看信息和管理成绩页面初始化为空白

### 问题描述
查看信息（`show_stu.jsp`）、管理成绩（`show_score.jsp`、`DelScore.jsp`）和查看班级信息（`show_cla.jsp`）的页面，点进去应该初始化显示全部数据，但实际是一片空白。

### 问题原因
1. **导航链接错误**：`left.jsp` 中的链接直接指向 JSP 页面，而不是先调用 Servlet 加载数据：
   ```html
   <dd><a href="${pageContext.request.contextPath}/show_stu.jsp" target="right">查看学生信息</a></dd>
   ```

2. **Servlet 逻辑问题**：
   - `show_stuServlet.java` 中，当没有任何查询参数时，仍会尝试构建查询条件，可能导致 SQL 错误
   - `Show_claServlet.java` 中缺少 `JDBCemo.getInstence()` 调用，导致数据库连接未初始化

### 修复方案
1. **修改导航链接**：`left.jsp` 中的链接改为指向 Servlet：
   - `show_stu.jsp` → `show_stu`
   - `show_score.jsp` → `Show_score`
   - `DelScore.jsp` → `Del`
   - `show_cla.jsp` → `show_cla`

2. **修复 `show_stuServlet.java`**：
   - 添加 `hasParams` 标志判断是否有查询参数
   - 无参数时直接设置 `condition = null` 并初始化数据库连接
   - 修复 `major` 和 `tea` 的空值检查（使用 `== null || == ""`）
   - 修复查询条件结尾的 `" and "` 问题

3. **修复 `Show_claServlet.java`**：
   - 在 `JDBCemo.select()` 之前添加 `JDBCemo.getInstence()` 调用

### 修改文件
- `src/main/webapp/left.jsp`
- `src/main/java/cn/nankai/edu/cn/show_stuServlet.java`
- `src/main/java/cn/nankai/edu/cn/Show_claServlet.java`

---

## Bug 5：查看信息和管理成绩页面按钮太小

### 问题描述
查看信息和管理成绩页面的按钮太小，根本看不清里面的字。

### 问题原因
`table.css` 中按钮的 `font-size` 设置为 `1px`，`padding` 也过小：
```css
button {
  padding: 3px 10px;
  font-size: 1px;
  ...
}
input[type=submit], input[type=button] {
  padding: 3px 10px;
  font-size: 1px;
  ...
}
```

### 修复方案
将 `table.css` 中的按钮样式调整为合理大小：
- `padding: 3px 10px` → `padding: 10px 20px`
- `font-size: 1px` → `font-size: 14px`
- `border-radius: 3px` → `border-radius: 5px`

### 修改文件
- `src/main/webapp/table.css`

---

## 修改文件清单

| 文件路径 | 操作类型 | 修改内容 |
|---------|---------|---------|
| `src/main/java/cn/nankai/edu/cn/LoginFilter.java` | 新增 | 登录过滤器，拦截所有请求进行身份验证 |
| `src/main/webapp/addStudent.jsp` | 修改 | 添加结果标记处理，避免进入页面即弹出提示 |
| `src/main/java/cn/nankai/edu/cn/AddstudentServlet.java` | 修改 | 添加 `addResult` 结果标记，完善空值检查 |
| `src/main/webapp/del_stu.jsp` | 修改 | 添加结果标记处理，避免进入页面即弹出提示 |
| `src/main/java/cn/nankai/edu/cn/delstuServelet.java` | 修改 | 添加 `delResult` 结果标记，完善参数校验和异常处理 |
| `src/main/webapp/left.jsp` | 修改 | 导航链接从 JSP 改为 Servlet，确保首次进入加载数据 |
| `src/main/java/cn/nankai/edu/cn/show_stuServlet.java` | 修改 | 添加无参数时的默认处理，修复空值检查逻辑 |
| `src/main/java/cn/nankai/edu/cn/Show_claServlet.java` | 修改 | 添加缺失的 `JDBCemo.getInstence()` 调用 |
| `src/main/webapp/table.css` | 修改 | 增大按钮尺寸和字体大小（1px → 14px） |

---

## 验证方法

### 1. 登录过滤器验证
- 未登录状态直接访问 `http://localhost:8080/Student/show_stu`
- 预期结果：自动重定向到 `login.jsp`

### 2. 添加学生页面验证
- 登录后进入添加学生页面
- 预期结果：不弹出任何提示
- 填写信息后点击保存
- 预期结果：根据操作结果弹出相应提示（成功/失败）
- 刷新页面
- 预期结果：不再弹出提示

### 3. 删除学生页面验证
- 登录后进入删除学生页面
- 预期结果：不弹出任何提示
- 输入学号后点击删除
- 预期结果：根据操作结果弹出相应提示
- 刷新页面
- 预期结果：不再弹出提示

### 4. 数据展示页面验证
- 登录后点击"查看学生信息"
- 预期结果：显示所有学生数据
- 登录后点击"查看学生学分绩"
- 预期结果：显示所有学分绩数据
- 登录后点击"管理学生成绩"
- 预期结果：显示所有成绩数据
- 登录后点击"查看班级信息"
- 预期结果：显示所有班级数据

### 5. 按钮样式验证
- 进入任意使用 `table.css` 的页面
- 预期结果：按钮大小适中，文字清晰可见（字体 14px）

---

## 部署说明

由于项目已配置在 Eclipse Tomcat 服务器中运行，修改代码后需要：

1. **重新编译**：在 Eclipse 中或使用 Maven 编译项目
   ```bash
   mvn clean compile
   ```

2. **重新部署**：
   - 如果使用 Eclipse WTP，需要在 Servers 视图中右键服务器选择 "Clean" 或 "Publish"
   - 或者直接重启 Tomcat 服务器

3. **清除浏览器缓存**：避免 CSS 等静态资源被缓存

---

## 注意事项

1. **Session 属性清理**：修改后的 JSP 页面在读取操作结果后会立即清除 Session 中的相关属性，避免刷新页面重复显示提示。

2. **过滤器顺序**：`LoginFilter` 使用 `@WebFilter("/*")` 拦截所有请求，确保在任何 Servlet 或 JSP 执行前进行身份验证。

3. **数据库连接**：`Show_claServlet` 修复了缺少 `JDBCemo.getInstence()` 的问题，确保数据库连接正确初始化。

4. **按钮样式**：`form.css` 中的按钮样式原本就是正常的（`padding: 10px 25px`，未设置 font-size 继承默认值），只有 `table.css` 有问题。
