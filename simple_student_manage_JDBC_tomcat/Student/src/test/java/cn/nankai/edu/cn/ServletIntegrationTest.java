package cn.nankai.edu.cn;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public abstract class ServletIntegrationTest {
    
    protected static Connection connection;
    protected static Statement statement;
    protected static String dbName;
    
    @BeforeAll
    public static void setUpClass() throws Exception {
        dbName = "testdb_servlet_" + UUID.randomUUID().toString().replace("-", "");
        String h2Url = "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1;MODE=MySQL";
        String h2User = "sa";
        String h2Password = "";
        
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(h2Url, h2User, h2Password);
        statement = connection.createStatement();
        initDatabase();
        
        JDBCemo.setConnection(connection);
        JDBCemo.setStatement(statement);
    }
    
    @AfterAll
    public static void tearDownClass() throws Exception {
        JDBCemo.setConnection(null);
        JDBCemo.setStatement(null);
        JDBCemo.setResulSet(null);
        
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    
    @BeforeEach
    public void setUp() throws Exception {
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        JDBCemo.setResulSet(null);
    }
    
    private static void initDatabase() throws SQLException {
        executeSql("CREATE TABLE IF NOT EXISTS teacher (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL" +
                ")");
        
        executeSql("CREATE TABLE IF NOT EXISTS major (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL" +
                ")");
        
        executeSql("CREATE TABLE IF NOT EXISTS student (" +
                "id INT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "sex VARCHAR(10)," +
                "indate DATE," +
                "claname VARCHAR(100)," +
                "majorid INT," +
                "teaid INT" +
                ")");
        
        executeSql("CREATE TABLE IF NOT EXISTS course (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100) NOT NULL" +
                ")");
        
        executeSql("CREATE TABLE IF NOT EXISTS stucourse (" +
                "stuid INT," +
                "couid INT," +
                "score FLOAT," +
                "PRIMARY KEY (stuid, couid)" +
                ")");
        
        executeSql("CREATE TABLE IF NOT EXISTS class (" +
                "name VARCHAR(100) PRIMARY KEY," +
                "num INT," +
                "teaid INT" +
                ")");
        
        executeSql("CREATE VIEW IF NOT EXISTS v_stu_avgscore AS " +
                "SELECT sc.stuid as stu_id, AVG(sc.score) as avg_score " +
                "FROM stucourse sc " +
                "GROUP BY sc.stuid");
        
        executeSql("INSERT INTO teacher (id, name) VALUES (1, '张老师')");
        executeSql("INSERT INTO teacher (id, name) VALUES (2, '李老师')");
        executeSql("INSERT INTO teacher (id, name) VALUES (3, '王老师')");
        
        executeSql("INSERT INTO major (id, name) VALUES (1, '计算机科学与技术')");
        executeSql("INSERT INTO major (id, name) VALUES (2, '软件工程')");
        executeSql("INSERT INTO major (id, name) VALUES (3, '信息工程')");
        
        executeSql("INSERT INTO course (id, name) VALUES (1, '高等数学')");
        executeSql("INSERT INTO course (id, name) VALUES (2, '大学英语')");
        executeSql("INSERT INTO course (id, name) VALUES (3, '程序设计')");
        executeSql("INSERT INTO course (id, name) VALUES (4, '数据结构')");
        
        executeSql("INSERT INTO class (name, num, teaid) VALUES ('计算机一班', 30, 1)");
        executeSql("INSERT INTO class (name, num, teaid) VALUES ('计算机二班', 28, 2)");
        
        executeSql("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (1001, '张三', '男', '2023-09-01', '计算机一班', 1, 1)");
        executeSql("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (1002, '李四', '女', '2023-09-01', '计算机一班', 1, 1)");
        executeSql("INSERT INTO student (id, name, sex, indate, claname, majorid, teaid) " +
                "VALUES (1003, '王五', '男', '2023-09-01', '计算机二班', 2, 2)");
        
        executeSql("INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 1, 85.5)");
        executeSql("INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 2, 90.0)");
        executeSql("INSERT INTO stucourse (stuid, couid, score) VALUES (1001, 3, 88.0)");
        executeSql("INSERT INTO stucourse (stuid, couid, score) VALUES (1002, 1, 78.0)");
        executeSql("INSERT INTO stucourse (stuid, couid, score) VALUES (1002, 2, 82.5)");
    }
    
    protected static void executeSql(String sql) throws SQLException {
        statement.execute(sql);
    }
    
    protected int executeUpdate(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }
    
    protected ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }
}
