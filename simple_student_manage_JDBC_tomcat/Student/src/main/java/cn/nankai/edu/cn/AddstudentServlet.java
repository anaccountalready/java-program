package cn.nankai.edu.cn;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JButton;

import cn.nankai.edu.cn.JDBCemo;

/**
 * Servlet implementation class AddstudentServlet
 */
@WebServlet("/add")
public class AddstudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddstudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//触发器下添加学生操作
		String name=request.getParameter("stu_name");
		 String id = request.getParameter("stu_id");
	      String sex = request.getParameter("sex");
	      String indate=request.getParameter("stu_indate");
	      String claname=request.getParameter("class");
	      String major=request.getParameter("major");
	      String tea=request.getParameter("tea");
	      String columns= "id,name,sex,indate,claname,majorid,teaid";
	      String sql1="select id from major where name="+"'"+major+"'"+";";
	      String sql2="select id from teacher where name="+"'"+tea+"'"+";";
		
	      
	     try {
	    	 JDBCemo.getInstence();
			JDBCemo.resulSet = JDBCemo.statement.executeQuery(sql1);
			String majorid=null;
			String teaid=null;
			while(JDBCemo.resulSet.next()) {majorid = JDBCemo.resulSet.getString("id");}
			//System.out.println(majorid);
			JDBCemo.resulSet = JDBCemo.statement.executeQuery(sql2);
			while(JDBCemo.resulSet.next())
			 teaid = JDBCemo.resulSet.getString("id");
			
			
			request.getSession().setAttribute("addtea", tea);
			request.getSession().setAttribute("addstuteaid", teaid);
			String values=id+","+"'"+name+"'"+","
	                +"'"+sex+"'"+","
					+"'"+indate+"'"+","
	                +"'"+claname+"'"+","+majorid+","+teaid;
			JDBCemo.insert("student", columns, values);
			JDBCemo.destroy();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      response.sendRedirect("addStudent.jsp");
			return;
	 	
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
