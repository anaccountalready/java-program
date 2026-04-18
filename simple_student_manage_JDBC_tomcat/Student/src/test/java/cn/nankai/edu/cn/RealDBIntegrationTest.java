package cn.nankai.edu.cn;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("真实数据库测试 - 完整集成测试")
class RealDBIntegrationTest extends RealDBTestBase {
    
    private static final int TEST_STUDENT_ID = 88001;
    private static final int TEST_STUDENT_ID_2 = 88002;
    private static final int TEST_STUDENT_ID_3 = 88003;
    
    @BeforeEach
    public void setupTestEnvironment() throws SQLException {
        log("========================================");
        log("准备集成测试环境...");
        log("========================================");
        
        try {
            log("清理可能存在的旧测试数据...");
            executeUpdateWithLog("清理成绩数据", 
                "DELETE FROM stucourse WHERE stuid IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            executeUpdateWithLog("清理学生数据", 
                "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            log("✓ 测试数据清理完成");
        } catch (SQLException e) {
            log("清理数据时可能数据不存在: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("集成测试1: 完整学生生命周期 - 增删改查全流程")
    void testCompleteStudentLifecycle() throws SQLException {
        log("========================================");
        log("集成测试1: 完整学生生命周期 - 增删改查全流程");
        log("========================================");
        
        String studentName = "集成测试学生_完整生命周期";
        String studentSex = "男";
        String studentIndate = "2023-09-01";
        
        log("========================================");
        log("阶段1: 创建学生记录 (Create)");
        log("========================================");
        
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '" + studentName + "', '" + studentSex + "', " +
                "'" + studentIndate + "', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        int rowsInserted = executeUpdateWithLog("插入学生", insertSql);
        assertEquals(1, rowsInserted, "插入应该成功，影响1行");
        log("✓ 阶段1完成: 学生创建成功");
        
        log("========================================");
        log("阶段2: 读取学生记录 (Read)");
        log("========================================");
        
        String selectSql = "SELECT * FROM student WHERE id = " + TEST_STUDENT_ID;
        logSql("查询学生", selectSql);
        ResultSet rs = statement.executeQuery(selectSql);
        
        assertTrue(rs.next(), "应该能查到学生");
        log("读取到的学生信息:");
        log("  ID: " + rs.getInt("id"));
        log("  姓名: " + rs.getString("name"));
        log("  性别: " + rs.getString("sex"));
        log("  班级: " + rs.getString("claname"));
        
        assertEquals(TEST_STUDENT_ID, rs.getInt("id"));
        assertEquals(studentName, rs.getString("name"));
        log("✓ 阶段2完成: 学生读取验证通过");
        
        log("========================================");
        log("阶段3: 更新学生记录 (Update)");
        log("========================================");
        
        String updatedName = "集成测试学生_已更新";
        String updatedSex = "女";
        
        String updateSql = "UPDATE student SET " +
                "name = '" + updatedName + "', " +
                "sex = '" + updatedSex + "' " +
                "WHERE id = " + TEST_STUDENT_ID;
        
        int rowsUpdated = executeUpdateWithLog("更新学生", updateSql);
        assertEquals(1, rowsUpdated, "更新应该成功，影响1行");
        
        log("验证更新结果...");
        rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        log("更新后的学生信息:");
        log("  姓名: " + rs.getString("name"));
        log("  性别: " + rs.getString("sex"));
        
        assertEquals(updatedName, rs.getString("name"));
        assertEquals(updatedSex, rs.getString("sex"));
        log("✓ 阶段3完成: 学生更新验证通过");
        
        log("========================================");
        log("阶段4: 删除学生记录 (Delete)");
        log("========================================");
        
        String deleteSql = "DELETE FROM student WHERE id = " + TEST_STUDENT_ID;
        int rowsDeleted = executeUpdateWithLog("删除学生", deleteSql);
        assertEquals(1, rowsDeleted, "删除应该成功，影响1行");
        
        log("验证删除结果...");
        rs = statement.executeQuery(selectSql);
        assertFalse(rs.next(), "删除后应该查不到学生");
        log("✓ 阶段4完成: 学生删除验证通过");
        
        log("========================================");
        log("集成测试1完成: 完整学生生命周期测试通过");
        log("========================================");
    }
    
    @Test
    @DisplayName("集成测试2: 完整成绩生命周期 - 增删改查全流程")
    void testCompleteScoreLifecycle() throws SQLException {
        log("========================================");
        log("集成测试2: 完整成绩生命周期 - 增删改查全流程");
        log("========================================");
        
        log("========================================");
        log("前置条件: 创建学生记录（成绩表依赖学生表）");
        log("========================================");
        
        String insertStudentSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '成绩生命周期测试', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("创建测试学生", insertStudentSql);
        
        log("========================================");
        log("阶段1: 创建成绩记录 (Create)");
        log("========================================");
        
        float initialScore = 78.5f;
        String insertScoreSql = "INSERT INTO stucourse (stuid, couid, score) VALUES (" +
                TEST_STUDENT_ID + ", " + existingCourseId + ", " + initialScore + ")";
        
        int rowsInserted = executeUpdateWithLog("插入成绩", insertScoreSql);
        assertEquals(1, rowsInserted);
        log("✓ 成绩创建成功");
        
        log("========================================");
        log("阶段2: 读取成绩记录 (Read)");
        log("========================================");
        
        String selectScoreSql = "SELECT * FROM stucourse WHERE stuid = " + TEST_STUDENT_ID + " AND couid = " + existingCourseId;
        logSql("查询成绩", selectScoreSql);
        ResultSet rs = statement.executeQuery(selectScoreSql);
        
        assertTrue(rs.next());
        log("读取到的成绩信息:");
        log("  学生ID: " + rs.getInt("stuid"));
        log("  课程ID: " + rs.getInt("couid"));
        log("  分数: " + rs.getFloat("score"));
        
        assertEquals(TEST_STUDENT_ID, rs.getInt("stuid"));
        assertEquals(existingCourseId, rs.getInt("couid"));
        assertEquals(initialScore, rs.getFloat("score"), 0.001f);
        log("✓ 成绩读取验证通过");
        
        log("========================================");
        log("阶段3: 更新成绩记录 (Update)");
        log("========================================");
        
        float updatedScore = 92.5f;
        String updateScoreSql = "UPDATE stucourse SET score = " + updatedScore + 
                " WHERE stuid = " + TEST_STUDENT_ID + " AND couid = " + existingCourseId;
        
        int rowsUpdated = executeUpdateWithLog("更新成绩", updateScoreSql);
        assertEquals(1, rowsUpdated);
        
        rs = statement.executeQuery(selectScoreSql);
        assertTrue(rs.next());
        assertEquals(updatedScore, rs.getFloat("score"), 0.001f);
        log("✓ 成绩更新验证通过");
        
        log("========================================");
        log("阶段4: 删除成绩记录 (Delete)");
        log("========================================");
        
        String deleteScoreSql = "DELETE FROM stucourse WHERE stuid = " + TEST_STUDENT_ID + " AND couid = " + existingCourseId;
        int rowsDeleted = executeUpdateWithLog("删除成绩", deleteScoreSql);
        assertEquals(1, rowsDeleted);
        
        rs = statement.executeQuery(selectScoreSql);
        assertFalse(rs.next());
        log("✓ 成绩删除验证通过");
        
        log("========================================");
        log("清理: 删除测试学生");
        log("========================================");
        executeUpdateWithLog("删除测试学生", "DELETE FROM student WHERE id = " + TEST_STUDENT_ID);
        
        log("========================================");
        log("集成测试2完成: 完整成绩生命周期测试通过");
        log("========================================");
    }
    
    @Test
    @DisplayName("集成测试3: 多学生多成绩关联测试 - 模拟真实业务场景")
    void testMultiStudentScoreRelationship() throws SQLException {
        log("========================================");
        log("集成测试3: 多学生多成绩关联测试 - 模拟真实业务场景");
        log("========================================");
        
        log("========================================");
        log("步骤1: 创建多个学生");
        log("========================================");
        
        String[] studentNames = {"关联测试学生A", "关联测试学生B", "关联测试学生C"};
        int[] studentIds = {TEST_STUDENT_ID, TEST_STUDENT_ID_2, TEST_STUDENT_ID_3};
        
        for (int i = 0; i < 3; i++) {
            String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (" + studentIds[i] + ", '" + studentNames[i] + "', '男', " +
                    "'2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
            executeUpdateWithLog("创建学生" + (i+1), insertSql);
        }
        log("✓ 3个学生创建完成");
        
        log("========================================");
        log("步骤2: 为每个学生添加课程成绩");
        log("========================================");
        
        float[] scores = {85.5f, 90.0f, 88.0f};
        
        for (int i = 0; i < 3; i++) {
            String insertScoreSql = "INSERT INTO stucourse (stuid, couid, score) VALUES (" +
                    studentIds[i] + ", " + existingCourseId + ", " + scores[i] + ")";
            executeUpdateWithLog("添加学生" + (i+1) + "的课程成绩", insertScoreSql);
        }
        log("✓ 3条成绩记录添加完成");
        
        log("========================================");
        log("步骤3: 验证学生-成绩关联查询");
        log("========================================");
        
        String joinQuerySql = "SELECT " +
                "s.id as student_id, " +
                "s.name as student_name, " +
                "sc.score " +
                "FROM student s " +
                "JOIN stucourse sc ON s.id = sc.stuid " +
                "WHERE s.id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ") " +
                "ORDER BY s.id";
        
        logSql("关联查询学生成绩", joinQuerySql);
        ResultSet rs = statement.executeQuery(joinQuerySql);
        
        int rowCount = 0;
        log("关联查询结果:");
        while (rs.next()) {
            rowCount++;
            log("  记录" + rowCount + ": 学生=" + rs.getString("student_name") + 
                    ", 成绩=" + rs.getFloat("score"));
        }
        
        assertEquals(3, rowCount, "应该查询到3条成绩记录");
        log("✓ 关联查询验证通过");
        
        log("========================================");
        log("步骤4: 统计分析 - 平均成绩");
        log("========================================");
        
        String avgScoreSql = "SELECT " +
                "AVG(sc.score) as avg_score, " +
                "COUNT(sc.score) as course_count " +
                "FROM student s " +
                "JOIN stucourse sc ON s.id = sc.stuid " +
                "WHERE s.id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")";
        
        logSql("统计平均成绩", avgScoreSql);
        rs = statement.executeQuery(avgScoreSql);
        
        assertTrue(rs.next());
        log("统计结果:");
        log("  平均成绩: " + String.format("%.2f", rs.getFloat("avg_score")));
        log("  课程数: " + rs.getInt("course_count"));
        assertEquals(3, rs.getInt("course_count"), "应该有3门课程");
        log("✓ 统计分析验证通过");
        
        log("========================================");
        log("步骤5: 清理测试数据");
        log("========================================");
        
        executeUpdateWithLog("删除成绩数据", 
            "DELETE FROM stucourse WHERE stuid IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
        executeUpdateWithLog("删除学生数据", 
            "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
        log("✓ 测试数据清理完成");
        
        log("========================================");
        log("集成测试3完成: 多学生多成绩关联测试通过");
        log("========================================");
    }
    
    @Test
    @DisplayName("集成测试4: 复杂查询场景 - 模拟前端多条件查询")
    void testComplexQueryScenarios() throws SQLException {
        log("========================================");
        log("集成测试4: 复杂查询场景 - 模拟前端多条件查询");
        log("========================================");
        
        log("========================================");
        log("准备测试数据");
        log("========================================");
        
        String insertSql1 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '复杂查询张小明', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        String insertSql2 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID_2 + ", '复杂查询李小红', '女', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        executeUpdateWithLog("创建测试学生1", insertSql1);
        executeUpdateWithLog("创建测试学生2", insertSql2);
        
        log("========================================");
        log("场景1: 按性别过滤查询");
        log("========================================");
        
        String sexQuery = "SELECT * FROM student WHERE sex = '男' AND id >= " + TEST_STUDENT_ID;
        logSql("按性别查询男生", sexQuery);
        ResultSet rs = statement.executeQuery(sexQuery);
        
        int maleCount = 0;
        while (rs.next()) {
            maleCount++;
            log("  查询到男生: " + rs.getString("name"));
            assertEquals("男", rs.getString("sex"));
        }
        assertTrue(maleCount >= 1);
        log("✓ 场景1通过");
        
        log("========================================");
        log("场景2: 模糊查询（姓名包含'小'）");
        log("========================================");
        
        String likeSql = "SELECT * FROM student WHERE name LIKE '%小%' AND id >= " + TEST_STUDENT_ID;
        logSql("模糊查询", likeSql);
        rs = statement.executeQuery(likeSql);
        
        int likeCount = 0;
        while (rs.next()) {
            likeCount++;
            String name = rs.getString("name");
            log("  模糊查询结果: " + name);
            assertTrue(name.contains("小"));
        }
        assertEquals(2, likeCount);
        log("✓ 场景2通过");
        
        log("========================================");
        log("场景3: IN查询");
        log("========================================");
        
        String inSql = "SELECT * FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ")";
        logSql("IN查询", inSql);
        rs = statement.executeQuery(inSql);
        
        int inCount = 0;
        while (rs.next()) {
            inCount++;
            log("  IN查询结果: " + rs.getString("name"));
        }
        assertEquals(2, inCount);
        log("✓ 场景3通过");
        
        log("========================================");
        log("场景4: 排序查询");
        log("========================================");
        
        String orderQuery = "SELECT * FROM student WHERE id >= " + TEST_STUDENT_ID + " ORDER BY name ASC";
        logSql("按姓名排序", orderQuery);
        rs = statement.executeQuery(orderQuery);
        
        String prevName = "";
        log("查询结果（按姓名排序）:");
        while (rs.next()) {
            String currentName = rs.getString("name");
            log("  " + currentName);
            if (!prevName.isEmpty()) {
                assertTrue(currentName.compareTo(prevName) >= 0, "应该按升序排列");
            }
            prevName = currentName;
        }
        log("✓ 场景4通过");
        
        log("========================================");
        log("清理测试数据");
        log("========================================");
        executeUpdateWithLog("删除测试学生", 
            "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ")");
        log("✓ 测试数据清理完成");
        
        log("========================================");
        log("集成测试4完成: 复杂查询场景测试通过");
        log("========================================");
    }
    
    @Test
    @DisplayName("集成测试5: 使用JDBCemo模拟Servlet操作流程")
    void testJDBCemoServletFlow() throws SQLException {
        log("========================================");
        log("集成测试5: 使用JDBCemo模拟Servlet操作流程");
        log("========================================");
        
        log("========================================");
        log("模拟AddstudentServlet添加学生");
        log("========================================");
        
        int newStudentId = 77001;
        String newStudentName = "JDBCemo模拟添加_张三";
        String newStudentSex = "男";
        String newStudentIndate = "2023-09-01";
        
        log("模拟构建INSERT SQL...");
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + newStudentId + ", '" + newStudentName + "', '" + newStudentSex + "', " +
                "'" + newStudentIndate + "', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        logSql("模拟AddstudentServlet插入", insertSql);
        int rowsInserted = statement.executeUpdate(insertSql);
        assertEquals(1, rowsInserted);
        log("✓ 模拟添加学生成功");
        
        log("========================================");
        log("模拟show_stuServlet查询学生");
        log("========================================");
        
        log("调用JDBCemo.select查询学生...");
        int selectResult = JDBCemo.select("*", "student", "id = " + newStudentId);
        log("JDBCemo.select返回: " + selectResult);
        assertEquals(1, selectResult);
        log("✓ JDBCemo.select执行成功");
        
        log("========================================");
        log("验证查询结果集...");
        log("========================================");
        ResultSet rs = JDBCemo.getResulSet();
        assertNotNull(rs);
        assertTrue(rs.next());
        log("查询结果:");
        log("  学号: " + rs.getInt("id"));
        log("  姓名: " + rs.getString("name"));
        assertEquals(newStudentId, rs.getInt("id"));
        assertEquals(newStudentName, rs.getString("name"));
        log("✓ JDBCemo查询结果验证通过");
        
        log("========================================");
        log("模拟delstuServelet删除学生");
        log("========================================");
        
        log("模拟构建DELETE SQL...");
        String deleteSql = "DELETE FROM student WHERE id = " + newStudentId;
        logSql("模拟delstuServelet删除", deleteSql);
        int rowsDeleted = statement.executeUpdate(deleteSql);
        assertEquals(1, rowsDeleted);
        log("✓ 模拟删除学生成功");
        
        log("验证删除结果...");
        selectResult = JDBCemo.select("*", "student", "id = " + newStudentId);
        rs = JDBCemo.getResulSet();
        assertFalse(rs.next());
        log("✓ 删除验证通过");
        
        log("========================================");
        log("集成测试5完成: JDBCemo模拟Servlet操作流程测试通过");
        log("========================================");
    }
    
    @AfterEach
    public void cleanupAllTestData() throws SQLException {
        log("========================================");
        log("清理所有集成测试数据...");
        log("========================================");
        
        try {
            executeUpdateWithLog("清理成绩数据", 
                "DELETE FROM stucourse WHERE stuid IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ", 77001)");
            executeUpdateWithLog("清理学生数据", 
                "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ", 77001)");
            log("✓ 所有测试数据清理完成");
        } catch (SQLException e) {
            log("清理数据时出错: " + e.getMessage());
        }
    }
}
