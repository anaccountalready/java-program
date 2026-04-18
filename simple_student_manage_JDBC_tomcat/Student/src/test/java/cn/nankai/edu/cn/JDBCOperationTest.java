package cn.nankai.edu.cn;

import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JDBC数据库操作测试")
class JDBCOperationTest extends BaseTest {

    @Test
    @DisplayName("测试查询操作 - 查询所有教师")
    void testSelectAllTeachers() throws SQLException {
        String sql = "SELECT * FROM teacher";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            int id = rs.getInt("id");
            String name = rs.getString("name");
            assertTrue(id > 0);
            assertNotNull(name);
            assertFalse(name.isEmpty());
        }
        assertTrue(count >= 3, "应该至少有3个教师");
    }

    @Test
    @DisplayName("测试查询操作 - 根据条件查询")
    void testSelectWithCondition() throws SQLException {
        String sql = "SELECT * FROM teacher WHERE id = 1";
        ResultSet rs = statement.executeQuery(sql);
        
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("id"));
        assertEquals("张老师", rs.getString("name"));
        assertFalse(rs.next());
    }

    @Test
    @DisplayName("测试插入操作 - 插入新教师")
    void testInsertTeacher() throws SQLException {
        String insertSql = "INSERT INTO teacher (name) VALUES ('赵老师')";
        int rowsAffected = statement.executeUpdate(insertSql);
        assertEquals(1, rowsAffected);
        
        String selectSql = "SELECT * FROM teacher WHERE name = '赵老师'";
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals("赵老师", rs.getString("name"));
    }

    @Test
    @DisplayName("测试插入操作 - 插入新学生")
    void testInsertStudent() throws SQLException {
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (2001, '赵六', '男', '2023-09-01', '计算机一班', 1, 1)";
        int rowsAffected = statement.executeUpdate(insertSql);
        assertEquals(1, rowsAffected);
        
        String selectSql = "SELECT * FROM student WHERE id = 2001";
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(2001, rs.getInt("id"));
        assertEquals("赵六", rs.getString("name"));
        assertEquals("男", rs.getString("sex"));
    }

    @Test
    @DisplayName("测试更新操作 - 更新学生信息")
    void testUpdateStudent() throws SQLException {
        String updateSql = "UPDATE student SET name = '张三更新' WHERE id = 1001";
        int rowsAffected = statement.executeUpdate(updateSql);
        assertEquals(1, rowsAffected);
        
        String selectSql = "SELECT name FROM student WHERE id = 1001";
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals("张三更新", rs.getString("name"));
    }

    @Test
    @DisplayName("测试更新操作 - 更新成绩")
    void testUpdateScore() throws SQLException {
        String updateSql = "UPDATE stucourse SET score = 95.0 WHERE stuid = 1001 AND couid = 1";
        int rowsAffected = statement.executeUpdate(updateSql);
        assertEquals(1, rowsAffected);
        
        String selectSql = "SELECT score FROM stucourse WHERE stuid = 1001 AND couid = 1";
        ResultSet rs = statement.executeQuery(selectSql);
        assertTrue(rs.next());
        assertEquals(95.0f, rs.getFloat("score"), 0.001f);
    }

    @Test
    @DisplayName("测试删除操作 - 删除学生成绩")
    void testDeleteScore() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM stucourse WHERE stuid = 1001 AND couid = 1";
        ResultSet rs = statement.executeQuery(checkSql);
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        String deleteSql = "DELETE FROM stucourse WHERE stuid = 1001 AND couid = 1";
        int rowsAffected = statement.executeUpdate(deleteSql);
        assertEquals(1, rowsAffected);
        
        rs = statement.executeQuery(checkSql);
        assertTrue(rs.next());
        assertEquals(0, rs.getInt(1));
    }

    @Test
    @DisplayName("测试删除操作 - 删除学生")
    void testDeleteStudent() throws SQLException {
        String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (9999, '删除测试', '男', '2023-09-01', '计算机一班', 1, 1)";
        statement.executeUpdate(insertSql);
        
        String checkSql = "SELECT COUNT(*) FROM student WHERE id = 9999";
        ResultSet rs = statement.executeQuery(checkSql);
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        
        String deleteSql = "DELETE FROM student WHERE id = 9999";
        int rowsAffected = statement.executeUpdate(deleteSql);
        assertEquals(1, rowsAffected);
        
        rs = statement.executeQuery(checkSql);
        assertTrue(rs.next());
        assertEquals(0, rs.getInt(1));
    }

    @Test
    @DisplayName("测试多表连接查询 - 查询学生及其专业")
    void testJoinQuery() throws SQLException {
        String sql = "SELECT s.id, s.name, m.name as major_name " +
                "FROM student s " +
                "LEFT JOIN major m ON s.majorid = m.id " +
                "WHERE s.id = 1001";
        
        ResultSet rs = statement.executeQuery(sql);
        assertTrue(rs.next());
        assertEquals(1001, rs.getInt("id"));
        assertEquals("张三", rs.getString("name"));
        assertEquals("计算机科学与技术", rs.getString("major_name"));
    }

    @Test
    @DisplayName("测试聚合查询 - 查询平均成绩")
    void testAggregateQuery() throws SQLException {
        String sql = "SELECT AVG(score) as avg_score FROM stucourse WHERE stuid = 1001";
        ResultSet rs = statement.executeQuery(sql);
        
        assertTrue(rs.next());
        float avgScore = rs.getFloat("avg_score");
        assertEquals((85.5f + 90.0f + 88.0f) / 3, avgScore, 0.001f);
    }

    @Test
    @DisplayName("测试条件查询 - 查询男生学生")
    void testConditionQueryMale() throws SQLException {
        String sql = "SELECT * FROM student WHERE sex = '男'";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
            assertEquals("男", rs.getString("sex"));
        }
        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("测试条件查询 - 查询成绩大于80分")
    void testConditionQueryScoreAbove80() throws SQLException {
        String sql = "SELECT * FROM stucourse WHERE score > 80.0";
        ResultSet rs = statement.executeQuery(sql);
        
        while (rs.next()) {
            float score = rs.getFloat("score");
            assertTrue(score > 80.0f);
        }
    }

    @Test
    @DisplayName("测试批量操作 - 插入多个学生")
    void testBatchInsert() throws SQLException {
        connection.setAutoCommit(false);
        try {
            Statement stmt = connection.createStatement();
            stmt.addBatch("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (3001, '批量1', '男', '2023-09-01', '计算机一班', 1, 1)");
            stmt.addBatch("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (3002, '批量2', '女', '2023-09-01', '计算机一班', 1, 1)");
            stmt.addBatch("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (3003, '批量3', '男', '2023-09-01', '计算机一班', 1, 1)");
            
            int[] results = stmt.executeBatch();
            assertEquals(3, results.length);
            for (int result : results) {
                assertEquals(1, result);
            }
            
            connection.commit();
            
            String countSql = "SELECT COUNT(*) FROM student WHERE id >= 3001 AND id <= 3003";
            ResultSet rs = statement.executeQuery(countSql);
            assertTrue(rs.next());
            assertEquals(3, rs.getInt(1));
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Test
    @DisplayName("测试事务回滚")
    void testTransactionRollback() throws SQLException {
        connection.setAutoCommit(false);
        try {
            String insertSql = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (4001, '回滚测试', '男', '2023-09-01', '计算机一班', 1, 1)";
            statement.executeUpdate(insertSql);
            
            connection.rollback();
            
            String checkSql = "SELECT COUNT(*) FROM student WHERE id = 4001";
            ResultSet rs = statement.executeQuery(checkSql);
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1));
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Test
    @DisplayName("测试空结果集")
    void testEmptyResultSet() throws SQLException {
        String sql = "SELECT * FROM student WHERE id = 999999";
        ResultSet rs = statement.executeQuery(sql);
        
        assertFalse(rs.next());
    }

    @Test
    @DisplayName("测试排序查询")
    void testOrderByQuery() throws SQLException {
        String sql = "SELECT * FROM student ORDER BY id DESC";
        ResultSet rs = statement.executeQuery(sql);
        
        int prevId = Integer.MAX_VALUE;
        while (rs.next()) {
            int currentId = rs.getInt("id");
            assertTrue(currentId <= prevId);
            prevId = currentId;
        }
    }

    @Test
    @DisplayName("测试分页查询")
    void testPaginationQuery() throws SQLException {
        String sql = "SELECT * FROM student ORDER BY id LIMIT 2 OFFSET 0";
        ResultSet rs = statement.executeQuery(sql);
        
        int count = 0;
        while (rs.next()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    @DisplayName("测试插入重复主键 - 应该抛出异常")
    void testInsertDuplicatePrimaryKey() {
        Assertions.assertThrows(SQLException.class, () -> {
            String insertSql1 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (5001, '重复', '男', '2023-09-01', '计算机一班', 1, 1)";
            statement.executeUpdate(insertSql1);
            
            String insertSql2 = "INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                    "VALUES (5001, '重复2', '女', '2023-09-01', '计算机二班', 2, 2)";
            statement.executeUpdate(insertSql2);
        });
    }

    @Test
    @DisplayName("测试更新不存在的记录 - 返回0行")
    void testUpdateNonExistentRecord() throws SQLException {
        String updateSql = "UPDATE student SET name = '不存在' WHERE id = 999999";
        int rowsAffected = statement.executeUpdate(updateSql);
        assertEquals(0, rowsAffected);
    }

    @Test
    @DisplayName("测试删除不存在的记录 - 返回0行")
    void testDeleteNonExistentRecord() throws SQLException {
        String deleteSql = "DELETE FROM student WHERE id = 999999";
        int rowsAffected = statement.executeUpdate(deleteSql);
        assertEquals(0, rowsAffected);
    }
}
