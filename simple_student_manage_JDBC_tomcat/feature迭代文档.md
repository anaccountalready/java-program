# simple_student_manage_JDBC_tomcat 项目 Feature 迭代文档

## 迭代日期
2026年4月25日

## 迭代概述
本次迭代为 `simple_student_manage_JDBC_tomcat` 项目新增了两个核心功能：
1. **分页展示功能**：当表格数据超过10条时自动分页展示
2. **Excel 导出功能**：支持将学生信息、学分绩、班级信息三个页面的表格数据导出为 .xlsx 文件

---

## 一、分页展示功能

### 1.1 功能说明
- 每页默认显示10条数据
- 当数据量超过10条时，自动生成分页导航栏
- 分页导航包含：首页、上一页、当前页码/总页数、下一页、末页
- 显示总记录数
- 支持条件查询后的分页（搜索条件自动带入分页链接）

### 1.2 实现细节

#### 1.2.1 新增分页工具类
- **文件路径**: `src/main/java/cn/nankai/edu/cn/PageBean.java`
- **功能**: 封装分页相关的属性和计算逻辑
- **核心属性**:
  - `currentPage`: 当前页码
  - `pageSize`: 每页显示条数（默认10）
  - `totalCount`: 总记录数
  - `totalPage`: 总页数
  - `startIndex`: 数据库查询起始索引
- **核心方法**:
  - `isHasPrevious()`: 是否有上一页
  - `isHasNext()`: 是否有下一页
  - `getPreviousPage()`: 获取上一页页码
  - `getNextPage()`: 获取下一页页码

#### 1.2.2 数据库层扩展
- **修改文件**: `src/main/java/cn/nankai/edu/cn/JDBCemo.java`
- **新增方法**:
  - `selectCount(String tables, String condition)`: 查询满足条件的总记录数
  - `selectWithPaging(String arr, String tables, String condition, int startIndex, int pageSize)`: 分页查询数据（使用 MySQL 的 LIMIT 语法）

#### 1.2.3 Servlet 层修改

| Servlet | 路径 | 功能 |
|---------|------|------|
| `show_stuServlet` | `/show_stu` | 学生信息分页查询 |
| `Show_scoreServlet` | `/Show_score` | 学分绩分页查询 |
| `Show_claServlet` | `/Show_cla` | 班级信息分页查询 |

**主要改动**:
1. 接收 `page` 参数确定当前页码
2. 先调用 `selectCount()` 获取总记录数
3. 创建 `PageBean` 对象计算分页信息
4. 调用 `selectWithPaging()` 进行分页查询
5. 将 `pageBean` 存入 Session 供 JSP 使用

#### 1.2.4 JSP 层修改
- **修改文件**:
  - `src/main/webapp/show_stu.jsp`
  - `src/main/webapp/show_score.jsp`
  - `src/main/webapp/show_cla.jsp`
- **新增内容**:
  - 分页导航栏样式（`.pagination`）
  - 分页逻辑：使用 JSTL 标签判断是否有上一页/下一页，动态生成分页链接
  - 显示当前页码、总页数、总记录数

### 1.3 使用示例
```
http://localhost:8080/Student/show_stu?page=2
http://localhost:8080/Student/Show_score?page=1&stu_id=1001
http://localhost:8080/Student/Show_cla?page=3
```

---

## 二、Excel 导出功能

### 2.1 功能说明
- 支持三个页面的数据导出：
  1. 学生信息页面 → 导出全部学生信息
  2. 学分绩页面 → 导出全部学生学分绩
  3. 班级信息页面 → 导出全部班级信息
- 导出文件格式：`.xlsx`（Office Excel 2007+ 格式）
- 文件名格式：`页面名+导出日期`，例如：
  - 学生信息20260425.xlsx
  - 学分绩20260425.xlsx
  - 班级信息20260425.xlsx
- Excel 文件包含：
  - 表头行（灰色背景，加粗字体）
  - 数据行（居中对齐，带边框）
  - 自动列宽

### 2.2 实现细节

#### 2.2.1 依赖配置
- **修改文件**: `pom.xml`
- **新增依赖**:
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.3</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
```

#### 2.2.2 新增导出工具类
- **文件路径**: `src/main/java/cn/nankai/edu/cn/ExcelExportUtil.java`
- **功能**: 封装 Excel 导出的通用逻辑
- **核心方法**:
  - `generateFileName(String pageName)`: 生成带日期的文件名
  - `exportStudentExcel(List<Student>, HttpServletResponse, String fileName)`: 导出学生信息
  - `exportScoreExcel(List<Avg_score>, HttpServletResponse, String fileName)`: 导出学分绩
  - `exportClassExcel(List<Classinfo>, HttpServletResponse, String fileName)`: 导出班级信息

#### 2.2.3 新增导出 Servlet

| Servlet | 路径 | 功能 |
|---------|------|------|
| `ExportStudentServlet` | `/exportStudent` | 导出学生信息 Excel |
| `ExportScoreServlet` | `/exportScore` | 导出学分绩 Excel |
| `ExportClassServlet` | `/exportClass` | 导出班级信息 Excel |

**实现逻辑**:
1. 查询全部数据（不受分页限制）
2. 调用 `ExcelExportUtil` 生成 Excel 文件
3. 设置 HTTP 响应头，触发浏览器下载

#### 2.2.4 JSP 层修改
- **修改文件**:
  - `src/main/webapp/show_stu.jsp`
  - `src/main/webapp/show_score.jsp`
  - `src/main/webapp/show_cla.jsp`
- **新增内容**:
  - 导出按钮样式（`.export-btn`，绿色按钮）
  - 导出按钮点击事件：跳转到对应的导出 Servlet

### 2.3 使用方式
1. 进入对应的数据展示页面
2. 点击"导出Excel"按钮
3. 浏览器自动下载 Excel 文件

---

## 三、文件变更清单

### 3.1 新增文件
| 文件路径 | 说明 |
|---------|------|
| `src/main/java/cn/nankai/edu/cn/PageBean.java` | 分页工具类 |
| `src/main/java/cn/nankai/edu/cn/ExcelExportUtil.java` | Excel 导出工具类 |
| `src/main/java/cn/nankai/edu/cn/ExportStudentServlet.java` | 学生信息导出 Servlet |
| `src/main/java/cn/nankai/edu/cn/ExportScoreServlet.java` | 学分绩导出 Servlet |
| `src/main/java/cn/nankai/edu/cn/ExportClassServlet.java` | 班级信息导出 Servlet |

### 3.2 修改文件
| 文件路径 | 修改内容 |
|---------|---------|
| `pom.xml` | 添加 Apache POI 依赖 |
| `src/main/java/cn/nankai/edu/cn/JDBCemo.java` | 添加分页查询方法 |
| `src/main/java/cn/nankai/edu/cn/show_stuServlet.java` | 支持分页查询 |
| `src/main/java/cn/nankai/edu/cn/Show_scoreServlet.java` | 支持分页查询 |
| `src/main/java/cn/nankai/edu/cn/Show_claServlet.java` | 支持分页查询 |
| `src/main/webapp/show_stu.jsp` | 添加分页导航和导出按钮 |
| `src/main/webapp/show_score.jsp` | 添加分页导航和导出按钮 |
| `src/main/webapp/show_cla.jsp` | 添加分页导航和导出按钮 |

---

## 四、测试说明

### 4.1 编译命令
```bash
cd simple_student_manage_JDBC_tomcat/Student
mvn clean compile
```

### 4.2 运行命令
```bash
mvn jetty:run
```

### 4.3 访问地址
- 项目首页: `http://localhost:8080/Student/`
- 学生信息页面: `http://localhost:8080/Student/show_stu`
- 学分绩页面: `http://localhost:8080/Student/Show_score`
- 班级信息页面: `http://localhost:8080/Student/Show_cla`

### 4.4 测试要点
1. **分页功能**:
   - 确保数据库有超过10条数据
   - 验证分页导航栏显示
   - 验证页码跳转功能
   - 验证条件查询后的分页（搜索条件带入下一页）

2. **导出功能**:
   - 点击导出按钮，验证文件下载
   - 验证文件名格式（包含日期）
   - 打开 Excel 文件，验证数据完整性和格式

---

## 五、注意事项

1. **数据库依赖**: 分页功能使用 MySQL 的 `LIMIT` 语法，如果切换到其他数据库（如 Oracle、PostgreSQL），需要修改 `JDBCemo.selectWithPaging()` 方法中的 SQL 语法。

2. **Excel 格式**: 使用 `.xlsx` 格式（基于 Apache POI 的 XSSF），需要 Office 2007 或更高版本打开。

3. **导出数据范围**: 导出功能导出的是全部数据（不受当前分页限制），如果需要导出当前页数据，可根据需求调整导出逻辑。

4. **Session 存储**: 分页信息使用 Session 存储，注意并发访问时的状态管理。

---

## 六、后续优化建议

1. 添加页码输入框，支持直接跳转到指定页
2. 添加每页显示条数选择（10条/20条/50条）
3. 导出功能支持条件筛选后的数据导出
4. 导出前添加数据预览确认
5. 添加导出进度提示
6. 支持导出为 `.xls` 格式（兼容 Office 2003）

---

## 七、版本信息

- 迭代版本: v1.1
- 基础版本: v1.0
- 开发环境: JDK 17, Maven 3.9, Jetty 9.4
- 依赖库: Apache POI 5.2.3, JSTL 1.2, MySQL Connector/J 8.0.32
