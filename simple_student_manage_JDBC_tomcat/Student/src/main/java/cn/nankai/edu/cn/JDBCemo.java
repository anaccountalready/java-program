package cn.nankai.edu.cn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.ResultSupport;

import java.sql.*;
import javax.servlet.jsp.jstl.sql.Result;
public class JDBCemo{
	protected static java.sql.Statement statement=null;
	protected static Connection connection=null;
	protected static ResultSet resulSet=null;
	protected static JDBCemo instance=null;

	public static JDBCemo getInstence() {
		if(instance==null) {instance=new JDBCemo();}
		return instance;
		
	}
	private JDBCemo() {
		
		String url = "jdbc:mysql://localhost:3306/myc_test?useUnicode=true&characterEncoding=utf8&useSSL=true";
		String username="root";
		String password="MsQ3Ly%sy";
		try {
			 try {
				 //注册数据库驱动
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("注册失败");
			}
			 //获得视图建立到指定数据库的url连接
			connection=DriverManager.getConnection(url,username,password);
			//由当前数据库连接生成一个数据库操作对象，获取数据库连接对象statement
			statement=  connection.createStatement();
			System.out.println("数据库连接成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("连接数据库失败");
			e.printStackTrace();
			
		}
		
		
		
	}
	public static void destroy() {
		//关闭数据库连接
		if(statement!=null)
			try {
				statement.close();
				if(connection!=null) {connection.close();}
				System.out.println("关闭数据库连接成功");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("关闭数据库连接失败");
				e.printStackTrace();
			}
		instance=null;
	}
	public static java.sql.Statement getStatement() {
		return statement;
	}
	public static Connection getConnection() {
		return connection;
	}
	public static ResultSet getResulSet() {
		return resulSet;
	}

	public static void setStatement(java.sql.Statement statement) {
		JDBCemo.statement = statement;
	}
	public static void setConnection(Connection connection) {
		JDBCemo.connection = connection;
	}
	public static void setResulSet(ResultSet resulSet) {
		JDBCemo.resulSet = resulSet;
	}
	//触发器控制下的添加操作
	protected static int insert(String tables,String columns,String values) {

		String sql = new String("insert into " + tables +"("+columns+")values("+values+");");
		
		System.out.println(sql);
		int count = 0;
		try {
			
			count = statement.executeUpdate(sql);
			System.out.println(count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("插入失败");
			e.printStackTrace();
		}
		return count;
		
	}
	protected static int update(String tables, String values, String condition) {
	
		String sql = new String("UPDATE " + tables + " SET " + values + " WHERE " + condition + ";");
		System.out.println(sql);
		int count = 0;
		try {
			count = statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public static int select(String arr, String tables, String condition) {
		String sql=null;
		if(condition==null||condition=="") {sql = new String("SELECT " + arr + " FROM " + tables+";");
	}
		else sql = new String("SELECT " + arr + " FROM " + tables + " WHERE " + condition + ";");
		System.out.println(sql);
	
		int count = 0;
		try {
			getInstence();
			resulSet = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	//批量删除自选条件的元组
	public static void delete_stucourse(String condition) {
		
		String sql="delete from stucourse where "+condition +";";
		
		
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	//批量删除自选复选框的元组
	public static void delete_stucoursebyid(int id,int stuid) throws SQLException {
		
		String sql="delete from stucourse where couid="+id+" and stuid="+stuid;
		System.out.println(sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args)  {	
	}
	
}

