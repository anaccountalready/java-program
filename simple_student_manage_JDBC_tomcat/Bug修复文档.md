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
查看学生信息、查看学生学分绩、管理学生成绩、查看班级信息四个页面的按钮太小，根本看不清里面的字。

### 问题原因
**第一次问题原因（已修复）**：
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

**第二次问题原因（补充修复）**：
虽然第一次修复将 `font-size` 从 `1px` 改为 `14px`，但按钮样式仍然显示异常，原因包括：

1. **CSS 选择器不够具体**：`button` 选择器优先级太低，可能被其他更具体的选择器覆盖
2. **继承问题**：按钮位于 `<table>` 和 `<td>` 元素内部，可能继承了 `table td` 的 `font-size: 12px` 样式
3. **缺少 `!important`**：没有使用 `!important` 标记来确保样式优先级，导致被其他样式覆盖
4. **缺少行高设置**：没有设置 `line-height`，文字可能显示不正常
5. **缺少最小宽度**：没有设置 `min-width`，按钮可能过窄

**第三次问题原因（最终修复 - 根本原因）**：
经过深入分析，发现了问题的根本原因：**HTML 结构严重错误**。

**`show_stu.jsp` 原始错误结构**：
```html
</head>
<form ...>              <!-- 第 13 行 - 没有 <body> 标签！ -->
<input ...>
<table>...</table>
<button>确定</button>
<body>                   <!-- 第 46 行 - body 才开始！ -->
...
</body>
</form>                   <!-- form 关闭在 body 之后 -->
</html>
```

**`DelScore.jsp` 原始错误结构**：
```html
</head>
<input type="hidden">    <!-- 在 <body> 之前！ -->
<body>
...
```

**问题分析**：
1. **元素位置错误**：`<form>`、`<input>`、`<button>` 等元素在 `<body>` 标签之前或之外
2. **无效 HTML**：这是完全不符合 HTML 规范的结构
3. **浏览器解析异常**：浏览器会尝试"修复"这种错误结构，但结果不可预测
4. **CSS 无法正确匹配**：由于元素在 DOM 树中的位置异常，CSS 选择器无法正确匹配

### 修复方案

**第一次修复**：
将 `table.css` 中的按钮样式调整为合理大小：
- `padding: 3px 10px` → `padding: 10px 20px`
- `font-size: 1px` → `font-size: 14px`
- `border-radius: 3px` → `border-radius: 5px`

**第二次修复（补充）**：
为确保样式正确生效，进行了以下优化：

1. **使用更具体的选择器**：
   - `button` → `button, form button, table button, td button, body button`
   - `input[type=submit]` → `input[type=submit], form input[type=submit], table input[type=submit], td input[type=submit]`
   - `input[type=button]` → `input[type=button], form input[type=button], table input[type=button], td input[type=button]`

2. **添加 `!important` 标记**：所有样式属性后添加 `!important` 确保优先级

3. **增加额外样式属性**：
   - `line-height: 1.5` - 提高文字可读性
   - `min-width: 80px` - 确保按钮最小宽度
   - `margin: 5px 2px` - 确保按钮之间有间距
   - `padding: 12px 24px` - 进一步增大内边距
   - `border-radius: 6px` - 略微增大圆角

**第三次修复（最终修复 - 根本解决）**：

1. **修复 HTML 结构**：
   - **`show_stu.jsp`**：将 `<form>` 移到 `<body>` 内部，确保所有表单元素都在 `<body>` 内，`</form>` 在 `</body>` 之前
   - **`DelScore.jsp`**：将 `<input type="hidden">` 移到 `<form>` 内部

2. **在每个页面添加内联样式**（最可靠的方式）：
   在所有 4 个页面的 `<head>` 中添加 `<style>` 标签，直接定义按钮样式：
   ```css
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
   ...
   </style>
   ```

3. **添加浏览器兼容性属性**：
   - `-webkit-appearance: none` - 移除 WebKit 浏览器的默认按钮样式
   - `appearance: none` - 标准属性，移除浏览器默认样式

### 修改文件
- `src/main/webapp/table.css`（多次修改优化）
- `src/main/webapp/show_stu.jsp` - 修复 HTML 结构 + 添加内联样式
- `src/main/webapp/DelScore.jsp` - 修复 HTML 结构 + 添加内联样式
- `src/main/webapp/show_score.jsp` - 添加内联样式
- `src/main/webapp/show_cla.jsp` - 添加内联样式

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
| `src/main/webapp/table.css` | 修改 | 增大按钮尺寸和字体大小，使用更具体的选择器和 `!important` 确保优先级 |
| `src/main/webapp/show_stu.jsp` | 重大修改 | 修复 HTML 结构（form/body 位置错误）+ 添加内联按钮样式 |
| `src/main/webapp/DelScore.jsp` | 重大修改 | 修复 HTML 结构（input 在 body 之前）+ 添加内联按钮样式 |
| `src/main/webapp/show_score.jsp` | 修改 | 添加内联按钮样式 |
| `src/main/webapp/show_cla.jsp` | 修改 | 添加内联按钮样式 |

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
- 登录后点击"查看学生信息"
  - 预期结果："展示所有学生"按钮和"确定"按钮大小适中，文字清晰可见
- 登录后点击"查看学生学分绩"
  - 预期结果："查看所有或已选条件的学分绩信息"按钮文字清晰可见
- 登录后点击"管理学生成绩"
  - 预期结果："查看当前所有(或所选)信息"、"删除所选条件的成绩信息"、"批量删除"、"修改成绩"等按钮文字清晰可见
- 登录后点击"查看班级信息"
  - 预期结果："查看"按钮文字清晰可见

**特别说明**：由于 CSS 使用了 `!important` 标记，可能需要清除浏览器缓存才能看到最新样式（使用 Ctrl+F5 强制刷新）。

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

4. **按钮样式 - 根本原因**：
   - **最严重的问题**：HTML 结构错误
     - `show_stu.jsp`：`<form>` 在 `<body>` 之前，`</form>` 在 `</body>` 之后
     - `DelScore.jsp`：`<input type="hidden">` 在 `<body>` 之前
     - 这种无效 HTML 导致浏览器解析异常，CSS 无法正确匹配元素

   - **外部 CSS 问题**：
     - `form.css` 中的按钮样式原本就是正常的（`padding: 10px 25px`，未设置 font-size 继承默认值）
     - `table.css` 原始 `font-size: 1px` 导致文字几乎看不见
     - 选择器不够具体，且缺少 `!important` 标记，可能被其他样式覆盖

   - **最终修复方案**：
     1. **修复 HTML 结构**：确保所有元素都在正确的位置
     2. **在每个页面添加内联样式**：这是最可靠的方式，优先级最高，不受外部 CSS 缓存和选择器优先级问题影响
     3. **使用 `!important`**：确保样式不被覆盖
     4. **添加浏览器兼容性属性**：`-webkit-appearance: none`、`appearance: none`

5. **CSS 缓存问题**：
   - 修改外部 CSS 后，浏览器可能缓存旧的样式
   - 建议使用 `Ctrl+F5` 强制刷新，或清除浏览器缓存后再测试
   - **内联样式不受缓存影响**，这是选择内联样式的重要原因之一

6. **HTML 规范重要性**：
   - 无效的 HTML 结构会导致各种不可预测的问题
   - 浏览器会尝试"容错"解析，但结果因浏览器而异
   - 开发时应确保 HTML 结构符合规范
