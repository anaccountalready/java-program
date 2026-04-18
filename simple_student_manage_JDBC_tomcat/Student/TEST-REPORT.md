# Simple Student Management System - 测试文档

## 1. 项目概述

本项目是一个基于 JSP + Servlet + JDBC 的学生管理系统，已完成全面的单元测试、集成测试和真实数据库测试。

### 1.1 测试统计

| 项目 | 数值 |
|------|------|
| 总测试数 | 243 |
| 通过数 | 243 |
| 失败数 | 0 |
| 错误数 | 0 |
| 跳过数 | 0 |
| 成功率 | 100% |

### 1.2 测试框架和依赖

| 框架/库 | 版本 | 用途 |
|---------|------|------|
| JUnit Jupiter | 5.10.0 | 单元测试框架 |
| Mockito | 5.7.0 | Mock对象模拟 |
| H2 Database | 2.2.224 | 内存测试数据库 |
| MySQL Connector/J | 8.0.32 | 真实数据库连接 |
| Maven Surefire | 3.1.2 | 测试运行器 |

### 1.3 测试类型

| 测试类型 | 测试数量 | 测试目标 |
|---------|---------|---------|
| 内存数据库测试 (H2) | 228 | 单元测试、集成测试（隔离环境） |
| 真实数据库测试 (MySQL) | 15 | JDBC操作、集成测试（真实环境） |

---

## 2. 测试目录结构

```
src/test/
├── java/cn/nankai/edu/cn/
│   ├── BaseTest.java                    # 测试基类 (H2内存数据库初始化)
│   ├── ServletIntegrationTest.java       # Servlet集成测试基类
│   ├── IntegrationTest.java              # 集成测试 (H2)
│   ├── JDBCOperationTest.java            # JDBC操作测试 (H2)
│   ├── StudentTest.java                  # Student实体类测试
│   ├── StuscoreTest.java                 # Stuscore实体类测试
│   ├── ClassinfoTest.java                # Classinfo实体类测试
│   ├── Avg_scoreTest.java                # Avg_score实体类测试
│   ├── LoginAdminServletTest.java        # 登录Servlet测试
│   ├── AddstudentServletTest.java        # 添加学生Servlet测试
│   ├── delstuServeletTest.java           # 删除学生Servlet测试
│   ├── show_stuServletTest.java          # 显示学生Servlet测试
│   ├── Show_scoreServletTest.java        # 显示成绩Servlet测试
│   ├── DelscoreServletTest.java          # 删除成绩Servlet测试
│   ├── Show_claServletTest.java          # 显示班级Servlet测试
│   ├── RealDBTestBase.java               # 真实数据库测试基类 (MySQL)
│   ├── RealDBJDBCOperationTest.java      # JDBC操作测试 (MySQL真实库)
│   └── RealDBIntegrationTest.java        # 集成测试 (MySQL真实库)
└── resources/
    └── schema-h2.sql                     # H2数据库初始化脚本
```

---

## 3. 真实数据库测试

### 3.1 概述

真实数据库测试直接连接到 MySQL 数据库，测试代码与真实数据的交互过程。所有数据库操作都会输出详细的日志，便于调试和监控。

### 3.2 数据库配置

| 配置项 | 值 |
|--------|-----|
| 数据库类型 | MySQL |
| 数据库URL | `jdbc:mysql://localhost:3306/myc_test` |
| 用户名 | `root` |
| 密码 | `123` |
| 字符编码 | UTF-8 |
| SSL | 禁用 |

### 3.3 真实数据库测试基类 (`RealDBTestBase.java`)

**功能**: 提供 MySQL 真实数据库连接和日志输出

**关键特性**:
- 自动连接到真实 MySQL 数据库
- 查询数据库中已存在的班级、教师、专业、课程信息
- 自动初始化 JDBCemo 静态变量
- 输出详细的测试日志（带时间戳）
- 测试完成后自动关闭连接

**日志输出示例**:
```
[TEST-LOG][2026-04-18 16:44:22.682] ========================================
[TEST-LOG][2026-04-18 16:44:22.682] 执行数据库操作: 插入学生记录
[TEST-LOG][2026-04-18 16:44:22.682] SQL语句: INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) VALUES (99001, '测试学生_真实库', '男', '2023-09-01', '计算机科学与技术一班', 1, 1)
[TEST-LOG][2026-04-18 16:44:22.682] ========================================
[TEST-LOG][2026-04-18 16:44:22.683] ========================================
[TEST-LOG][2026-04-18 16:44:22.683] 数据库操作结果: 插入学生记录
[TEST-LOG][2026-04-18 16:44:22.683] 影响行数: 1
[TEST-LOG][2026-04-18 16:44:22.683] ========================================
```

### 3.4 真实数据库 JDBC 操作测试 (`RealDBJDBCOperationTest.java`)

**测试方法**: 10 个

| 测试方法 | 描述 | 状态 |
|---------|------|------|
| testDatabaseConnection | 数据库连接验证 | ✅ |
| testInsertStudent | 插入学生记录 | ✅ |
| testUpdateStudent | 更新学生记录 | ✅ |
| testDeleteStudent | 删除学生记录 | ✅ |
| testSelectStudent | 查询学生记录（多种方式） | ✅ |
| testScoreCRUD | 成绩CRUD完整流程 | ✅ |
| testMultiTableJoin | 多表关联查询 | ✅ |
| testAggregateQuery | 聚合函数查询 | ✅ |
| testTransaction | 事务测试（提交与回滚） | ✅ |
| testJDBCemoOperations | 使用JDBCemo操作测试 | ✅ |

**测试覆盖**:
- **连接验证**: 数据库连接是否正常
- **CRUD操作**: 学生和成绩的完整增删改查
- **高级查询**: 多表关联查询、聚合查询
- **事务处理**: 事务提交与回滚
- **JDBCemo集成**: 测试 JDBCemo 工具类与真实数据库的交互

### 3.5 真实数据库集成测试 (`RealDBIntegrationTest.java`)

**测试方法**: 5 个

| 测试方法 | 描述 | 状态 |
|---------|------|------|
| testCompleteStudentLifecycle | 完整学生生命周期（CRUD全流程） | ✅ |
| testCompleteScoreLifecycle | 完整成绩生命周期（CRUD全流程） | ✅ |
| testMultiStudentScoreRelationship | 多学生多成绩关联测试 | ✅ |
| testComplexQueryScenarios | 复杂查询场景（模拟前端多条件查询） | ✅ |
| testJDBCemoServletFlow | 使用JDBCemo模拟Servlet操作流程 | ✅ |

**测试覆盖**:
- **完整生命周期**: 学生和成绩的端到端CRUD测试
- **关联关系**: 学生-成绩一对多关系验证
- **复杂查询**: 条件查询、模糊查询、IN查询、排序查询
- **Servlet模拟**: 模拟 AddstudentServlet、show_stuServlet、delstuServelet 的操作流程

### 3.6 日志输出内容

真实数据库测试会输出以下交互过程:

1. **数据库连接信息**
   - 数据库URL
   - 用户名
   - 密码 (123)
   - 找到的现有班级、教师、专业、课程ID

2. **每个SQL操作**
   - 操作类型（插入、更新、删除、查询）
   - 完整的SQL语句
   - 影响行数（对于DML操作）
   - 查询结果（对于SELECT操作）

3. **测试流程信息**
   - 测试开始/结束标记
   - 步骤说明
   - 验证结果

4. **清理操作**
   - 测试数据清理
   - 连接关闭

### 3.7 运行真实数据库测试

```bash
# 运行所有真实数据库测试
mvn test -Dtest=RealDB*

# 运行指定的真实数据库测试类
mvn test -Dtest=RealDBJDBCOperationTest
mvn test -Dtest=RealDBIntegrationTest

# 运行指定的测试方法
mvn test -Dtest=RealDBJDBCOperationTest#testInsertStudent
```

### 3.8 测试数据管理

**测试数据范围**:
- 使用大ID值（88001-99003）避免与现有数据冲突
- 每个测试方法使用 `@BeforeEach` 和 `@AfterEach` 管理测试数据
- 测试完成后自动清理测试数据

**现有数据处理**:
- 测试基类自动查询数据库中已存在的班级、教师、专业
- 使用已存在的班级名避免外键约束错误
- 查询条件使用精确的ID范围（IN子句）而非范围查询

---

## 4. 内存数据库测试 (H2)

### 4.1 单元测试

#### 4.1.1 实体类测试

**测试方法总数**: 76 个

| 测试类 | 测试方法数 | 描述 |
|--------|-----------|------|
| StudentTest | 11 | Student实体类完整测试 |
| StuscoreTest | 20 | Stuscore实体类完整测试 |
| ClassinfoTest | 25 | Classinfo实体类完整测试 |
| Avg_scoreTest | 20 | Avg_score实体类完整测试 |

#### 4.1.2 Servlet 单元测试

**测试方法总数**: 119 个

| 测试类 | 测试方法数 | 描述 |
|--------|-----------|------|
| LoginAdminServletTest | 10 | 登录Servlet参数获取和流程测试 |
| AddstudentServletTest | 17 | 添加学生Servlet测试 |
| delstuServeletTest | 14 | 删除学生Servlet测试 |
| show_stuServletTest | 22 | 显示学生Servlet测试 |
| Show_scoreServletTest | 21 | 显示成绩Servlet测试 |
| DelscoreServletTest | 25 | 删除成绩Servlet测试 |
| Show_claServletTest | 10 | 显示班级Servlet测试 |

### 4.2 集成测试

**测试方法总数**: 33 个

| 测试类 | 测试方法数 | 描述 |
|--------|-----------|------|
| JDBCOperationTest | 20 | JDBC操作测试（H2内存库） |
| IntegrationTest | 13 | 完整集成测试（H2内存库） |

---

## 5. 测试运行说明

### 5.1 运行所有测试

```bash
mvn clean test
```

### 5.2 按测试类型运行

```bash
# 运行内存数据库测试（H2）
mvn test -Dtest="*Test" -Dtest="!RealDB*"

# 运行真实数据库测试（MySQL）
mvn test -Dtest=RealDB*
```

### 5.3 运行指定测试类

```bash
# 内存数据库测试
mvn test -Dtest=JDBCOperationTest
mvn test -Dtest=IntegrationTest

# 真实数据库测试
mvn test -Dtest=RealDBJDBCOperationTest
mvn test -Dtest=RealDBIntegrationTest
```

### 5.4 生成测试报告

```bash
mvn surefire-report:report
```
报告位置: `target/site/surefire-report.html`

---

## 6. 测试结果统计

### 6.1 按测试类型统计

| 测试类型 | 测试数量 | 通过 | 失败 | 成功率 |
|---------|---------|------|------|--------|
| 实体类单元测试 | 76 | 76 | 0 | 100% |
| Servlet 单元测试 | 119 | 119 | 0 | 100% |
| JDBC 操作测试 | 30 | 30 | 0 | 100% |
| 集成测试 | 18 | 18 | 0 | 100% |
| **总计** | **243** | **243** | **0** | **100%** |

### 6.2 按数据库类型统计

| 数据库类型 | 测试数量 | 通过 | 失败 | 说明 |
|-----------|---------|------|------|------|
| H2内存数据库 | 228 | 228 | 0 | 隔离环境测试 |
| MySQL真实数据库 | 15 | 15 | 0 | 真实环境测试 |
| **总计** | **243** | **243** | **0** | |

---

## 7. 新增文件清单

### 7.1 真实数据库测试相关文件

| 文件路径 | 描述 |
|---------|------|
| `src/test/java/cn/nankai/edu/cn/RealDBTestBase.java` | 真实数据库测试基类（MySQL连接、日志输出） |
| `src/test/java/cn/nankai/edu/cn/RealDBJDBCOperationTest.java` | JDBC操作测试（真实数据库） |
| `src/test/java/cn/nankai/edu/cn/RealDBIntegrationTest.java` | 集成测试（真实数据库） |

### 7.2 数据库配置修改

**RealDBTestBase.java 中的数据库配置**:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/myc_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "123";  // 密码已改为123
```

---

## 8. 结论

本测试套件成功实现了以下目标：

### 8.1 内存数据库测试 (H2)
- ✅ **228 个测试全部通过**
- ✅ 完整的单元测试覆盖
- ✅ 集成测试验证
- ✅ 测试隔离（每个测试方法独立数据库）

### 8.2 真实数据库测试 (MySQL)
- ✅ **15 个测试全部通过**
- ✅ 直接连接真实 MySQL 数据库
- ✅ **密码改为 `123`**
- ✅ **输出详细的交互过程日志**
- ✅ 自动查询现有数据避免外键约束
- ✅ 测试数据自动清理
- ✅ JDBCemo 工具类集成测试
- ✅ Servlet 操作流程模拟

### 8.3 测试覆盖范围

| 功能 | 内存库测试 | 真实库测试 |
|------|-----------|-----------|
| 学生CRUD | ✅ | ✅ |
| 成绩CRUD | ✅ | ✅ |
| 多表关联查询 | ✅ | ✅ |
| 聚合查询 | ✅ | ✅ |
| 事务处理 | ✅ | ✅ |
| Servlet参数处理 | ✅ | - |
| JDBCemo集成 | ✅ | ✅ |
| 前后端交互模拟 | - | ✅ |
| **日志输出** | - | ✅ |

---

**文档生成时间**: 2026-04-18  
**测试执行结果**: 全部通过 (243/243)  
**真实数据库连接**: 成功（密码: 123）  
**交互日志输出**: 已启用
