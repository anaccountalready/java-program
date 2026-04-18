package cn.nankai.edu.cn;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("集成测试 - 完整CRUD流程测试")
class IntegrationTest extends BaseTest {

    @Test
    @DisplayName("测试完整的学生CRUD流程")
    void testStudentCRUD() throws SQLException {
        int newStudentId = 5001;
        String newStudentName = "集成测试学生";
        String newStudentSex = "男";
        String newStudentIndate = "2023-09-01";
        String newStudentClaname = "计算机一班";
        int newStudentMajorid = 1;
        int newStudentTeaid = 1;

        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (" + newStudentId + ", '" + newStudentName + "', '" + newStudentSex + "', " +
                "'" + newStudentIndate + "', '" + newStudentClaname + "', " + newStudentMajorid + ", " + newStudentTeaid + ")";
        int rowsInserted = statement.executeUpdate(insertSql);
        assertEquals(1, rowsInserted);

        String selectSql = "SELECT * FROM student WHERE id = " + newStudentId;
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(newStudentId, rs.getInt("id"));
        assertEquals(newStudentName, rs.getString("name"));
        assertEquals(newStudentSex, rs.getString("sex"));

        String updatedName = "集成测试学生(已更新)";
        String updateSql = "UPDATE student SET name = '" + updatedName + "' WHERE id = " + newStudentId;
        int rowsUpdated = statement.executeUpdate(updateSql);
        assertEquals(1, rowsUpdated);

        rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(updatedName, rs.getString("name"));

        String deleteSql = "DELETE FROM student WHERE id = " + newStudentId;
        int rowsDeleted = statement.executeUpdate(deleteSql);
        assertEquals(1, rowsDeleted);

        rs = statement.executeQuery(selectSql);
        assertFalse(rs.next());
    }

    @Test
    @DisplayName("测试完整的成绩CRUD流程")
    void testScoreCRUD() throws SQLException {
        int stuId = 1001;
        int couId = 4;
        float initialScore = 80.0f;

        String insertSql = "INSERT INTO stucourse (stuid, couid, score) VALUES (" + stuId + ", " + couId + ", " + initialScore + ")";
        int rowsInserted = statement.executeUpdate(insertSql);
        assertEquals(1, rowsInserted);

        String selectSql = "SELECT * FROM stucourse WHERE stuid = " + stuId + " AND couid = " + couId;
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(initialScore, rs.getFloat("score"), 0.001f);

        float updatedScore = 95.5f;
        String updateSql = "UPDATE stucourse SET score = " + updatedScore + " WHERE stuid = " + stuId + " AND couid = " + couId;
        int rowsUpdated = statement.executeUpdate(updateSql);
        assertEquals(1, rowsUpdated);

        rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(updatedScore, rs.getFloat("score"), 0.001f);

        String deleteSql = "DELETE FROM stucourse WHERE stuid = " + stuId + " AND couid = " + couId;
        int rowsDeleted = statement.executeUpdate(deleteSql);
        assertEquals(1, rowsDeleted);

        rs = statement.executeQuery(selectSql);
        assertFalse(rs.next());
    }

    @Test
    @DisplayName("测试多表关联查询 - 学生信息与专业")
    void testMultiTableJoinQuery() throws SQLException {
        String sql = "SELECT s.id, s.name, m.name as major_name " +
                "FROM student s " +
                "JOIN major m ON s.majorid = m.id";
        
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String majorName = rs.getString("major_name");
            
            assertTrue(id > 0);
            assertNotNull(name);
            assertFalse(name.isEmpty());
            assertNotNull(majorName);
            assertFalse(majorName.isEmpty());
        }
        
        assertTrue(count >= 3);
    }

    @Test
    @DisplayName("测试多表关联查询 - 学生信息与教师")
    void testStudentTeacherJoinQuery() throws SQLException {
        String sql = "SELECT s.id, s.name, t.name as teacher_name " +
                "FROM student s " +
                "JOIN teacher t ON s.teaid = t.id";
        
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            String teacherName = rs.getString("teacher_name");
            assertNotNull(teacherName);
            assertFalse(teacherName.isEmpty());
        }
        
        assertTrue(count >= 3);
    }

    @Test
    @DisplayName("测试聚合查询 - 学生平均成绩")
    void testAggregateQuery() throws SQLException {
        String sql = "SELECT s.id, s.name, AVG(sc.score) as avg_score " +
                "FROM student s " +
                "JOIN stucourse sc ON s.id = sc.stuid " +
                "WHERE s.id = 1001 " +
                "GROUP BY s.id, s.name";
        
        ResultSet rs = statement.executeQuery(sql);
        
        assertTrue(rs.next());
        assertEquals(1001, rs.getInt("id"));
        assertEquals("张三", rs.getString("name"));
        
        float avgScore = rs.getFloat("avg_score");
        assertEquals((85.5f + 90.0f + 88.0f) / 3, avgScore, 0.001f);
    }

    @Test
    @DisplayName("测试条件组合查询")
    void testCombinedConditionQuery() throws SQLException {
        String sql = "SELECT * FROM student WHERE sex = '男' AND claname = '计算机一班'";
        
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            assertEquals("男", rs.getString("sex"));
            assertEquals("计算机一班", rs.getString("claname"));
        }
        
        assertTrue(count >= 1);
    }

    @Test
    @DisplayName("测试批量插入与批量删除")
    void testBatchOperations() throws SQLException {
        connection.setAutoCommit(false);
        try {
            int baseId = 6000;
            for (int i = 0; i < 3; i++) {
                String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                        "VALUES (" + (baseId + i) + ", '批量学生" + i + "', '男', " +
                        "'2023-09-01', '计算机一班', 1, 1)";
                statement.executeUpdate(insertSql);
            }
            connection.commit();
            
            String countSql = "SELECT COUNT(*) FROM student WHERE id >= " + baseId + " AND id < " + (baseId + 3);
            ResultSet rs = statement.executeQuery(countSql);
            assertTrue(rs.next());
            assertEquals(3, rs.getInt(1));
            
            connection.setAutoCommit(false);
            String deleteSql = "DELETE FROM student WHERE id >= " + baseId + " AND id < " + (baseId + 3);
            int rowsDeleted = statement.executeUpdate(deleteSql);
            connection.commit();
            
            assertEquals(3, rowsDeleted);
            
            rs = statement.executeQuery(countSql);
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Test
    @DisplayName("测试分页查询")
    void testPagination() throws SQLException {
        String page1Sql = "SELECT * FROM student ORDER BY id LIMIT 2 OFFSET 0";
        ResultSet rs = statement.executeQuery(page1Sql);
        
        int page1Count = 0;
        while (rs.next()) {
            page1Count++;
        }
        assertEquals(2, page1Count);
        
        String page2Sql = "SELECT * FROM student ORDER BY id LIMIT 2 OFFSET 2";
        rs = statement.executeQuery(page2Sql);
        
        int page2Count = 0;
        while (rs.next()) {
            page2Count++;
        }
        assertTrue(page2Count >= 1);
    }

    @Test
    @DisplayName("测试排序查询")
    void testOrderBy() throws SQLException {
        String ascSql = "SELECT * FROM student ORDER BY id ASC";
        ResultSet rs = statement.executeQuery(ascSql);
        
        int prevId = Integer.MIN_VALUE;
        while (rs.next()) {
            int currentId = rs.getInt("id");
            assertTrue(currentId >= prevId);
            prevId = currentId;
        }
        
        String descSql = "SELECT * FROM student ORDER BY id DESC";
        rs = statement.executeQuery(descSql);
        
        prevId = Integer.MAX_VALUE;
        while (rs.next()) {
            int currentId = rs.getInt("id");
            assertTrue(currentId <= prevId);
            prevId = currentId;
        }
    }

    @Test
    @DisplayName("测试事务回滚")
    void testTransactionRollback() throws SQLException {
        int testId = 7001;
        String checkSql = "SELECT COUNT(*) FROM student WHERE id = " + testId;
        
        ResultSet rs = statement.executeQuery(checkSql);
        assertTrue(rs.next());
        int initialCount = rs.getInt(1);
        
        connection.setAutoCommit(false);
        try {
            String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (" + testId + ", '回滚测试', '男', '2023-09-01', '计算机一班', 1, 1)";
            statement.executeUpdate(insertSql);
            
            connection.rollback();
            
            rs = statement.executeQuery(checkSql);
            assertTrue(rs.next());
            assertEquals(initialCount, rs.getInt(1));
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Test
    @DisplayName("测试IN查询")
    void testInQuery() throws SQLException {
        String sql = "SELECT * FROM student WHERE id IN (1001, 1002)";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            int id = rs.getInt("id");
            assertTrue(id == 1001 || id == 1002);
        }
        
        assertEquals(2, count);
    }

    @Test
    @DisplayName("测试LIKE模糊查询")
    void testLikeQuery() throws SQLException {
        String sql = "SELECT * FROM student WHERE name LIKE '张%'";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            String name = rs.getString("name");
            assertTrue(name.startsWith("张"));
        }
        
        assertTrue(count >= 1);
    }

    @Test
    @DisplayName("测试IS NULL查询")
    void testIsNullQuery() throws SQLException {
        String sql = "SELECT * FROM student WHERE sex IS NOT NULL";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            String sex = rs.getString("sex");
            assertNotNull(sex);
        }
        
        assertTrue(count >= 3);
    }
}
