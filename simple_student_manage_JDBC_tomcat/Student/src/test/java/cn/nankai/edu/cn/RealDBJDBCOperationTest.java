package cn.nankai.edu.cn;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("真实数据库测试 - JDBC操作测试")
class RealDBJDBCOperationTest extends RealDBTestBase {
    
    private static final int TEST_STUDENT_ID = 99001;
    private static final int TEST_STUDENT_ID_2 = 99002;
    private static final int TEST_STUDENT_ID_3 = 99003;
    
    @BeforeEach
    public void setupTestData() throws SQLException {
        log("准备测试数据...");
        
        try {
            log("清理可能存在的旧测试数据...");
            executeUpdateWithLog("清理测试成绩数据", 
                "DELETE FROM stucourse WHERE stuid IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            executeUpdateWithLog("清理测试学生数据", 
                "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            log("测试数据清理完成");
        } catch (SQLException e) {
            log("清理测试数据时出错（可能数据不存在）: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("测试1: 数据库连接验证")
    void testDatabaseConnection() throws SQLException {
        log("========== 测试1: 数据库连接验证 ==========");
        
        assertNotNull(connection, "数据库连接不应为null");
        assertFalse(connection.isClosed(), "数据库连接应该是打开的");
        log("✓ 数据库连接验证通过");
        
        log("执行简单的SELECT 1测试...");
        ResultSet rs = statement.executeQuery("SELECT 1 as result");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("result"));
        log("✓ SELECT 1测试通过");
        
        log("========== 测试1完成 ==========");
    }
    
    @Test
    @DisplayName("测试2: 插入学生记录 - 完整流程")
    void testInsertStudent() throws SQLException {
        log("========== 测试2: 插入学生记录 - 完整流程 ==========");
        
        String studentName = "测试学生_真实库";
        String studentSex = "男";
        String studentIndate = "2023-09-01";
        
        log("准备插入学生数据:");
        log("  学号: " + TEST_STUDENT_ID);
        log("  姓名: " + studentName);
        log("  性别: " + studentSex);
        log("  入学日期: " + studentIndate);
        log("  班级: " + existingClassName);
        log("  专业ID: " + existingMajorId);
        log("  教师ID: " + existingTeacherId);
        
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '" + studentName + "', '" + studentSex + "', " +
                "'" + studentIndate + "', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        int rowsInserted = executeUpdateWithLog("插入学生记录", insertSql);
        assertEquals(1, rowsInserted, "应该插入1条记录");
        log("✓ 学生记录插入成功");
        
        log("验证插入的数据...");
        String selectSql = "SELECT * FROM student WHERE id = " + TEST_STUDENT_ID;
        logSql("查询插入的学生", selectSql);
        
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next(), "应该能查询到插入的学生");
        
        log("查询结果:");
        log("  学号: " + rs.getInt("id"));
        log("  姓名: " + rs.getString("name"));
        log("  性别: " + rs.getString("sex"));
        log("  班级: " + rs.getString("claname"));
        
        assertEquals(TEST_STUDENT_ID, rs.getInt("id"));
        assertEquals(studentName, rs.getString("name"));
        assertEquals(studentSex, rs.getString("sex"));
        log("✓ 插入数据验证通过");
        
        log("========== 测试2完成 ==========");
    }
    
    @Test
    @DisplayName("测试3: 更新学生记录 - 完整流程")
    void testUpdateStudent() throws SQLException {
        log("========== 测试3: 更新学生记录 - 完整流程 ==========");
        
        log("步骤1: 先插入测试数据");
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '更新前姓名', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("插入测试学生", insertSql);
        
        log("步骤2: 验证插入的数据");
        ResultSet rs = statement.executeQuery("SELECT name, claname FROM student WHERE id = " + TEST_STUDENT_ID);
        assertTrue(rs.next());
        assertEquals("更新前姓名", rs.getString("name"));
        log("✓ 插入数据验证通过");
        
        log("步骤3: 执行更新操作");
        String newName = "更新后姓名_真实库";
        String updateSql = "UPDATE student SET name = '" + newName + "' WHERE id = " + TEST_STUDENT_ID;
        
        int rowsUpdated = executeUpdateWithLog("更新学生记录", updateSql);
        assertEquals(1, rowsUpdated, "应该更新1条记录");
        log("✓ 更新操作执行成功");
        
        log("步骤4: 验证更新结果");
        rs = statement.executeQuery("SELECT name FROM student WHERE id = " + TEST_STUDENT_ID);
        assertTrue(rs.next());
        
        log("更新后的数据:");
        log("  姓名: " + rs.getString("name"));
        
        assertEquals(newName, rs.getString("name"));
        log("✓ 更新结果验证通过");
        
        log("========== 测试3完成 ==========");
    }
    
    @Test
    @DisplayName("测试4: 删除学生记录 - 完整流程")
    void testDeleteStudent() throws SQLException {
        log("========== 测试4: 删除学生记录 - 完整流程 ==========");
        
        log("步骤1: 先插入测试数据");
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '待删除学生', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("插入待删除学生", insertSql);
        
        log("步骤2: 验证数据存在");
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as count FROM student WHERE id = " + TEST_STUDENT_ID);
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("count"));
        log("✓ 数据存在验证通过");
        
        log("步骤3: 执行删除操作");
        String deleteSql = "DELETE FROM student WHERE id = " + TEST_STUDENT_ID;
        int rowsDeleted = executeUpdateWithLog("删除学生记录", deleteSql);
        assertEquals(1, rowsDeleted, "应该删除1条记录");
        log("✓ 删除操作执行成功");
        
        log("步骤4: 验证数据已删除");
        rs = statement.executeQuery("SELECT COUNT(*) as count FROM student WHERE id = " + TEST_STUDENT_ID);
        assertTrue(rs.next());
        assertEquals(0, rs.getInt("count"));
        log("✓ 数据已删除验证通过");
        
        log("========== 测试4完成 ==========");
    }
    
    @Test
    @DisplayName("测试5: 查询学生记录 - 多种查询方式")
    void testSelectStudent() throws SQLException {
        log("========== 测试5: 查询学生记录 - 多种查询方式 ==========");
        
        log("步骤1: 插入多个测试数据");
        String insertSql1 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '查询测试1', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        String insertSql2 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID_2 + ", '查询测试2', '女', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        executeUpdateWithLog("插入测试学生1", insertSql1);
        executeUpdateWithLog("插入测试学生2", insertSql2);
        
        log("步骤2: 按ID查询");
        String selectByIdSql = "SELECT * FROM student WHERE id = " + TEST_STUDENT_ID;
        logSql("按ID查询学生", selectByIdSql);
        ResultSet rs = statement.executeQuery(selectByIdSql);
        assertTrue(rs.next());
        assertEquals("查询测试1", rs.getString("name"));
        log("✓ 按ID查询通过");
        
        log("步骤3: 按条件查询（性别=男）");
        String selectBySexSql = "SELECT * FROM student WHERE sex = '男' AND id >= " + TEST_STUDENT_ID;
        logSql("按性别查询", selectBySexSql);
        rs = statement.executeQuery(selectBySexSql);
        int maleCount = 0;
        while (rs.next()) {
            maleCount++;
            log("  查询到男生: " + rs.getString("name"));
        }
        assertTrue(maleCount >= 1);
        log("✓ 按性别查询通过");
        
        log("步骤4: 模糊查询（姓名以'查询'开头）");
        String likeSql = "SELECT * FROM student WHERE name LIKE '查询%'";
        logSql("模糊查询", likeSql);
        rs = statement.executeQuery(likeSql);
        int likeCount = 0;
        while (rs.next()) {
            likeCount++;
            log("  模糊查询结果: " + rs.getString("name"));
        }
        assertEquals(2, likeCount);
        log("✓ 模糊查询通过");
        
        log("步骤5: IN查询");
        String inSql = "SELECT * FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ")";
        logSql("IN查询", inSql);
        rs = statement.executeQuery(inSql);
        int inCount = 0;
        while (rs.next()) {
            inCount++;
            log("  IN查询结果: " + rs.getString("name"));
        }
        assertEquals(2, inCount);
        log("✓ IN查询通过");
        
        log("========== 测试5完成 ==========");
    }
    
    @Test
    @DisplayName("测试6: 成绩CRUD - 完整流程")
    void testScoreCRUD() throws SQLException {
        log("========== 测试6: 成绩CRUD - 完整流程 ==========");
        
        log("========================================");
        log("前置条件: 创建学生记录（成绩表依赖学生表）");
        log("========================================");
        
        String insertStudentSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '成绩测试学生', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("创建成绩测试学生", insertStudentSql);
        
        log("========================================");
        log("阶段1: 创建成绩记录 (Create)");
        log("========================================");
        
        float initialScore = 78.5f;
        String insertScoreSql = "INSERT INTO stucourse (stuid, couid, score) VALUES (" +
                TEST_STUDENT_ID + ", " + existingCourseId + ", " + initialScore + ")";
        
        int rowsInserted = executeUpdateWithLog("插入成绩记录", insertScoreSql);
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
        
        log("========== 测试6完成 ==========");
    }
    
    @Test
    @DisplayName("测试7: 多表关联查询 - 学生、教师、专业关联")
    void testMultiTableJoin() throws SQLException {
        log("========== 测试7: 多表关联查询 - 学生、教师、专业关联 ==========");
        
        log("========================================");
        log("步骤1: 插入测试数据");
        log("========================================");
        
        String insertStudentSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '关联查询测试', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("插入关联测试学生", insertStudentSql);
        
        log("========================================");
        log("步骤2: 执行多表关联查询");
        log("========================================");
        
        String joinSql = "SELECT " +
                "s.id as student_id, " +
                "s.name as student_name, " +
                "s.sex, " +
                "t.name as teacher_name, " +
                "m.name as major_name " +
                "FROM student s " +
                "LEFT JOIN teacher t ON s.teaid = t.id " +
                "LEFT JOIN major m ON s.majorid = m.id " +
                "WHERE s.id = " + TEST_STUDENT_ID;
        
        logSql("多表关联查询", joinSql);
        ResultSet rs = statement.executeQuery(joinSql);
        
        assertTrue(rs.next());
        log("关联查询结果:");
        log("  学生ID: " + rs.getInt("student_id"));
        log("  学生姓名: " + rs.getString("student_name"));
        log("  性别: " + rs.getString("sex"));
        log("  教师姓名: " + rs.getString("teacher_name"));
        log("  专业名称: " + rs.getString("major_name"));
        
        assertEquals(TEST_STUDENT_ID, rs.getInt("student_id"));
        assertEquals("关联查询测试", rs.getString("student_name"));
        log("✓ 多表关联查询通过");
        
        log("========== 测试7完成 ==========");
    }
    
    @Test
    @DisplayName("测试8: 聚合函数查询 - 统计分析")
    void testAggregateQuery() throws SQLException {
        log("========== 测试8: 聚合函数查询 - 统计分析 ==========");
        
        log("========================================");
        log("步骤1: 插入多个测试学生");
        log("========================================");
        
        String insertSql1 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", '聚合测试1', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        String insertSql2 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID_2 + ", '聚合测试2', '女', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        
        executeUpdateWithLog("插入聚合测试学生1", insertSql1);
        executeUpdateWithLog("插入聚合测试学生2", insertSql2);
        
        log("========================================");
        log("步骤2: 统计班级学生数量");
        log("========================================");
        
        String countSql = "SELECT COUNT(*) as total, claname " +
                "FROM student " +
                "WHERE claname = '" + existingClassName + "' " +
                "GROUP BY claname";
        logSql("统计班级学生数", countSql);
        ResultSet rs = statement.executeQuery(countSql);
        assertTrue(rs.next());
        log("班级学生数量: " + rs.getInt("total"));
        assertTrue(rs.getInt("total") >= 2);
        log("✓ COUNT统计通过");
        
        log("========================================");
        log("步骤3: 查询最大/最小学生ID");
        log("========================================");
        
        String minMaxSql = "SELECT MIN(id) as min_id, MAX(id) as max_id " +
                "FROM student " +
                "WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ")";
        logSql("查询最值", minMaxSql);
        rs = statement.executeQuery(minMaxSql);
        assertTrue(rs.next());
        log("最小ID: " + rs.getInt("min_id"));
        log("最大ID: " + rs.getInt("max_id"));
        assertEquals(TEST_STUDENT_ID, rs.getInt("min_id"));
        assertEquals(TEST_STUDENT_ID_2, rs.getInt("max_id"));
        log("✓ MIN/MAX查询通过");
        
        log("========== 测试8完成 ==========");
    }
    
    @Test
    @DisplayName("测试9: 事务测试 - 提交与回滚")
    void testTransaction() throws SQLException {
        log("========== 测试9: 事务测试 - 提交与回滚 ==========");
        
        try {
            log("========================================");
            log("步骤1: 关闭自动提交，开始事务");
            log("========================================");
            connection.setAutoCommit(false);
            log("✓ 自动提交已关闭，事务开始");
            
            log("========================================");
            log("步骤2: 在事务中插入数据");
            log("========================================");
            String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (" + TEST_STUDENT_ID + ", '事务测试', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
            statement.executeUpdate(insertSql);
            log("✓ 事务中数据已插入（尚未提交）");
            
            log("========================================");
            log("步骤3: 验证事务中可以查询到数据（当前连接可见）");
            log("========================================");
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) as count FROM student WHERE id = " + TEST_STUDENT_ID);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("count"));
            log("✓ 事务中数据可见");
            
            log("========================================");
            log("步骤4: 回滚事务");
            log("========================================");
            connection.rollback();
            log("✓ 事务已回滚");
            
            log("========================================");
            log("步骤5: 验证数据已消失");
            log("========================================");
            rs = statement.executeQuery("SELECT COUNT(*) as count FROM student WHERE id = " + TEST_STUDENT_ID);
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"));
            log("✓ 回滚后数据已删除");
            
            log("========================================");
            log("步骤6: 测试事务提交");
            log("========================================");
            String insertSql2 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (" + TEST_STUDENT_ID_2 + ", '事务提交测试', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
            statement.executeUpdate(insertSql2);
            log("✓ 数据已插入，准备提交");
            
            connection.commit();
            log("✓ 事务已提交");
            
            log("========================================");
            log("步骤7: 验证提交后的数据");
            log("========================================");
            rs = statement.executeQuery("SELECT COUNT(*) as count FROM student WHERE id = " + TEST_STUDENT_ID_2);
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("count"));
            log("✓ 提交后数据已持久化");
            
        } finally {
            log("========================================");
            log("清理测试数据");
            log("========================================");
            executeUpdateWithLog("清理事务测试学生", "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ")");
            connection.setAutoCommit(true);
            log("✓ 自动提交已恢复");
        }
        
        log("========== 测试9完成 ==========");
    }
    
    @Test
    @DisplayName("测试10: 使用JDBCemo进行操作测试")
    void testJDBCemoOperations() throws SQLException {
        log("========== 测试10: 使用JDBCemo进行操作测试 ==========");
        
        log("========================================");
        log("步骤1: 插入测试学生数据");
        log("========================================");
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + TEST_STUDENT_ID + ", 'JDBCemo测试', '男', '2023-09-01', '" + existingClassName + "', " + existingMajorId + ", " + existingTeacherId + ")";
        executeUpdateWithLog("插入JDBCemo测试学生", insertSql);
        
        log("========================================");
        log("步骤2: 使用JDBCemo进行查询");
        log("========================================");
        log("调用JDBCemo.select方法...");
        int selectResult = JDBCemo.select("*", "student", "id = " + TEST_STUDENT_ID);
        log("JDBCemo.select返回值: " + selectResult);
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
        assertEquals(TEST_STUDENT_ID, rs.getInt("id"));
        assertEquals("JDBCemo测试", rs.getString("name"));
        log("✓ JDBCemo查询结果验证通过");
        
        log("========================================");
        log("步骤3: 清理测试数据");
        log("========================================");
        executeUpdateWithLog("删除JDBCemo测试学生", "DELETE FROM student WHERE id = " + TEST_STUDENT_ID);
        
        log("========== 测试10完成 ==========");
    }
    
    @AfterEach
    public void cleanupTestData() throws SQLException {
        log("========================================");
        log("清理本次测试的数据...");
        log("========================================");
        try {
            executeUpdateWithLog("清理成绩数据", 
                "DELETE FROM stucourse WHERE stuid IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            executeUpdateWithLog("清理学生数据", 
                "DELETE FROM student WHERE id IN (" + TEST_STUDENT_ID + ", " + TEST_STUDENT_ID_2 + ", " + TEST_STUDENT_ID_3 + ")");
            log("✓ 测试数据清理完成");
        } catch (SQLException e) {
            log("清理数据时出错: " + e.getMessage());
        }
    }
}
