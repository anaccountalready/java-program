package cn.nankai.edu.cn;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class RealDBTestBase {
    
    protected static Connection connection;
    protected static Statement statement;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/myc_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123";
    
    protected static String existingClassName = null;
    protected static int existingTeacherId = 1;
    protected static int existingMajorId = 1;
    protected static int existingCourseId = 1;
    
    @BeforeAll
    public static void initDatabaseConnection() throws Exception {
        log("========================================");
        log("开始初始化真实数据库连接...");
        log("数据库URL: " + DB_URL);
        log("数据库用户: " + DB_USER);
        log("数据库密码: " + DB_PASSWORD);
        log("========================================");
        
        try {
            log("正在加载MySQL驱动...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            log("MySQL驱动加载成功！");
            
            log("正在建立数据库连接...");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();
            log("数据库连接建立成功！");
            
            log("正在查询现有数据以确保外键约束...");
            queryExistingData();
            
            log("正在初始化JDBCemo静态变量...");
            JDBCemo.setConnection(connection);
            JDBCemo.setStatement(statement);
            log("JDBCemo静态变量初始化完成！");
            
            log("========================================");
            log("真实数据库连接初始化完成！");
            log("现有班级: " + existingClassName);
            log("现有教师ID: " + existingTeacherId);
            log("现有专业ID: " + existingMajorId);
            log("现有课程ID: " + existingCourseId);
            log("========================================");
            
        } catch (ClassNotFoundException e) {
            log("错误: 找不到MySQL驱动类");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            log("错误: 数据库连接失败");
            log("错误信息: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private static void queryExistingData() throws SQLException {
        log("查询class表中的现有数据...");
        ResultSet rs = statement.executeQuery("SELECT name FROM class LIMIT 1");
        if (rs.next()) {
            existingClassName = rs.getString("name");
            log("找到现有班级: " + existingClassName);
        } else {
            log("class表为空，尝试插入测试班级...");
            try {
                statement.executeUpdate("INSERT INTO class (name, num, teaid) VALUES ('测试班级A', 30, 1)");
                existingClassName = "测试班级A";
                log("插入测试班级成功: " + existingClassName);
            } catch (SQLException e) {
                log("插入测试班级失败: " + e.getMessage());
                existingClassName = "计算机一班";
            }
        }
        
        log("查询teacher表中的现有数据...");
        rs = statement.executeQuery("SELECT id FROM teacher LIMIT 1");
        if (rs.next()) {
            existingTeacherId = rs.getInt("id");
            log("找到现有教师ID: " + existingTeacherId);
        } else {
            log("teacher表为空，使用默认ID: 1");
            existingTeacherId = 1;
        }
        
        log("查询major表中的现有数据...");
        rs = statement.executeQuery("SELECT id FROM major LIMIT 1");
        if (rs.next()) {
            existingMajorId = rs.getInt("id");
            log("找到现有专业ID: " + existingMajorId);
        } else {
            log("major表为空，使用默认ID: 1");
            existingMajorId = 1;
        }
        
        log("查询course表中的现有数据...");
        rs = statement.executeQuery("SELECT id FROM course LIMIT 1");
        if (rs.next()) {
            existingCourseId = rs.getInt("id");
            log("找到现有课程ID: " + existingCourseId);
        } else {
            log("course表为空，使用默认ID: 1");
            existingCourseId = 1;
        }
    }
    
    @BeforeEach
    public void logTestStart() {
        log("----------------------------------------");
        log("开始执行测试方法...");
        log("----------------------------------------");
    }
    
    @AfterAll
    public static void closeDatabaseConnection() {
        log("========================================");
        log("开始关闭数据库连接...");
        log("========================================");
        
        try {
            if (statement != null && !statement.isClosed()) {
                log("正在关闭Statement...");
                statement.close();
                log("Statement关闭成功！");
            }
        } catch (SQLException e) {
            log("警告: 关闭Statement时出错: " + e.getMessage());
        }
        
        try {
            if (connection != null && !connection.isClosed()) {
                log("正在关闭Connection...");
                connection.close();
                log("Connection关闭成功！");
            }
        } catch (SQLException e) {
            log("警告: 关闭Connection时出错: " + e.getMessage());
        }
        
        log("正在重置JDBCemo静态变量...");
        JDBCemo.setConnection(null);
        JDBCemo.setStatement(null);
        log("JDBCemo静态变量重置完成！");
        
        log("========================================");
        log("数据库连接已全部关闭！");
        log("========================================");
    }
    
    protected static void log(String message) {
        String timestamp = dateFormat.format(new Date());
        System.out.println("[TEST-LOG][" + timestamp + "] " + message);
    }
    
    protected static void logSql(String operation, String sql) {
        log("========================================");
        log("执行数据库操作: " + operation);
        log("SQL语句: " + sql);
        log("========================================");
    }
    
    protected static void logResult(String operation, int rowsAffected) {
        log("========================================");
        log("数据库操作结果: " + operation);
        log("影响行数: " + rowsAffected);
        log("========================================");
    }
    
    protected static void logResult(String operation, boolean success) {
        log("========================================");
        log("数据库操作结果: " + operation);
        log("执行状态: " + (success ? "成功" : "失败"));
        log("========================================");
    }
    
    protected int executeUpdateWithLog(String operation, String sql) throws SQLException {
        logSql(operation, sql);
        try {
            int rows = statement.executeUpdate(sql);
            logResult(operation, rows);
            return rows;
        } catch (SQLException e) {
            log("错误: 执行 " + operation + " 失败");
            log("错误信息: " + e.getMessage());
            throw e;
        }
    }
    
    protected boolean executeWithLog(String operation, String sql) throws SQLException {
        logSql(operation, sql);
        try {
            boolean result = statement.execute(sql);
            logResult(operation, true);
            return result;
        } catch (SQLException e) {
            log("错误: 执行 " + operation + " 失败");
            log("错误信息: " + e.getMessage());
            throw e;
        }
    }
}
