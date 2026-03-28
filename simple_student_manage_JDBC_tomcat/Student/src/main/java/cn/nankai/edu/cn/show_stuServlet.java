package cn.nankai.edu.cn;

import java.awt.Window;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class show_stuServlet
 */
@WebServlet("/show_stu")
public class show_stuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public show_stuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String condition=null;
    String showall=(String)request.getParameter("showall");
    if(showall!=null) {
    	condition=null;
    	
    }
    else {
		String claname=request.getParameter("class");
		String sex=request.getParameter("sex");
		String tea=request.getParameter("tea");
		String id=request.getParameter("id");
		String major=request.getParameter("major");
		 String sql1="select id from major where name="+"'"+major+"'"+";";
	      String sql2="select id from teacher where name="+"'"+tea+"'"+";";
	      String majorid=null;
			 String teaid=null;
			 String ci=null;
			 String cm=null;
			 String cc=null;
			 String cs=null;
			 String ct=null;
		try {
	    	 JDBCemo.getInstence();
	    	 
	    	if(major==""){}
	    	else {JDBCemo.resulSet = JDBCemo.statement.executeQuery(sql1);
             while(JDBCemo.resulSet.next()) {majorid = JDBCemo.resulSet.getString("id");}}
	    	if(tea==""){}
	    	else {
			JDBCemo.resulSet = JDBCemo.statement.executeQuery(sql2);
			while(JDBCemo.resulSet.next())teaid = JDBCemo.resulSet.getString("id");}
	    	 
	    	 if(claname==null)cc="";
	    	 else cc=" claname="+"'"+claname+"'"+" and ";
	    	 if(major==null)cm="";
	    	 else cm=" majorid="+majorid+" and ";
	    	 
	    	 if(sex==null)cs="";
	    	 else cs=" sex="+"'"+sex+"'";
	    	 
	    	 if(tea==""||tea==null)ct="";
	    	 else {ct=" and "+" teaid="+teaid;}
	    	 if(id==""||id==null)ci="";
	    	 else ci=" and "+" id="+id;
	    	// System.out.println("cc="+cc+" "+"cs="+cs+" ct="+ct+" ci="+ci);
	    	 condition=cc+cm+cs+ct+ci;
	    	 //System.out.println("condition:"+condition);
	    	 } catch (SQLException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}}
	 	     
			JDBCemo.select("*", "student", condition);
			Student[] stu = new Student[100];
			int []teai=new int[100];
			int []majori=new int[100];
			int i=0;
			try {
				while(JDBCemo.resulSet.next()) {
					
					stu[i]=new Student(JDBCemo.resulSet.getInt(1),JDBCemo.resulSet.getString(2),JDBCemo.resulSet.getString(3),JDBCemo.resulSet.getDate(4),JDBCemo.resulSet.getString(5),JDBCemo.resulSet.getInt(6),JDBCemo.resulSet.getInt(7));
					
					majori[i]=JDBCemo.resulSet.getInt(6);
					teai[i]=JDBCemo.resulSet.getInt(7);
					i++;
				}
				for(int m=0;m<i;m++) {
					JDBCemo.select("name", "teacher", " id="+teai[m]);
					if(JDBCemo.resulSet.next()) {stu[m].setTeacher(JDBCemo.resulSet.getString("name"));}
				}
				for(int m=0;m<i;m++) {
					JDBCemo.select("name", "major", " id="+majori[m]);
					if(JDBCemo.resulSet.next()) {stu[m].setMajor(JDBCemo.resulSet.getString("name"));}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JDBCemo.destroy();
			request.getSession().setAttribute("countstu", i);
			request.getSession().setAttribute("student",stu );
			 response.sendRedirect("show_stu.jsp");
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
