package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.CDATASection;

/**
 * Servlet implementation class DelscoreServlet
 */
@WebServlet("/Del")
public class DelscoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DelscoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String checkID=request.getParameter("checkID");
		
		//System.out.println(checkID);
		Stuscore []stucour=new Stuscore[100];
		String showall=(String)request.getParameter("showall");
		String course=request.getParameter("course");
		String stuid=(request.getParameter("id"));
		String condition=null;
		//System.out.println("course=:"+course);
		//System.out.println("stuid=:"+stuid);
		 String sql1="select name from course where id in(select id from stucourse);";
	  int count=0;
		if((stuid==""||stuid==null)&&(course==""||course==null)) {
			 JDBCemo.getInstence();
			 JDBCemo.select("*", "stucourse", null);
			 ResultSet re=JDBCemo.resulSet;
			 int []cid=new int[100];
			 int i=0;
			 try {
				while(re.next()) {
					cid[i]=re.getInt("couid");
					stucour[i]=new Stuscore(re.getInt("stuid"),re.getInt("couid"),re.getFloat("score"));
					 i++;
				 }
				count=i;
				for(int j=0;j<count;j++) {
					  stucour[j].setCouname(cid[j]);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
		else {
			JDBCemo.getInstence();
			String si=null;
			String sc=null;
			int couid;
			String siglecoursename=null;
           
			if(stuid==""||stuid==null)si="";
			else si=" stuid= "+stuid;
			if(course==null||course=="")sc="";
			else {
				siglecoursename=course;
				JDBCemo.select("id", "course", " name="+"'"+course+"'");
				try {
					if(JDBCemo.resulSet.next()) {
					couid=JDBCemo.resulSet.getInt("id");
					if(stuid==""||stuid==null) {
						sc=" couid="+couid;
							
							
						} 
					else {sc=" and couid="+couid;}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			condition=si+sc;
		
			 JDBCemo.select("*", "stucourse", condition);
			 request.getSession().setAttribute("condition", condition);
			 ResultSet re=JDBCemo.resulSet;
			 int []cid=new int[100];
			 int i=0;
			 try {
				while(re.next()) {
					cid[i]=re.getInt("couid");
					stucour[i]=new Stuscore(re.getInt("stuid"),re.getInt("couid"),re.getFloat("score"),course);
					 i++;
				 }
				count=i;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//批量删除自选复选框的元组
		if(checkID==null) {}
		else {
			try {
				JDBCemo.getInstence();
				JDBCemo.connection.setAutoCommit(false);
				//System.out.println(checkID);
				String []str=checkID.split(",");
				int []ids=new int[str.length];
				int []stuids=new int[str.length];
				if(str.length>0) {
					for(int i=0;i<str.length;i++) {
						String []tem=str[i].split("_");
						ids[i]=Integer.parseInt(tem[0]);
						stuids[i]=Integer.parseInt(tem[1]);
						
							JDBCemo.delete_stucoursebyid(ids[i],stuids[i]);
						
					}
					JDBCemo.connection.commit();
					JDBCemo.connection.setAutoCommit(true);
					System.out.println("复选框删除事务成功提交");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				try {
					JDBCemo.connection.rollback();
					JDBCemo.connection.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.err.println("复选框删除事务回滚失败");
				}
				
				e.printStackTrace();
			}
	
		}
		
		 String chascore=(String)request.getParameter("newscore");
		 if(chascore!=null) {
			// System.out.println("chascore:"+chascore);
			 String []a=chascore.split("_");
			 String con=" stuid="+a[1]+" and couid="+a[2];
			 JDBCemo.getInstence();
			 JDBCemo.update("stucourse","score="+a[0],con);
		 }
		request.getSession().setAttribute("count", count);
		request.getSession().setAttribute("stucour", stucour);
		String delsel=(String)request.getParameter("delsel");
		//批量删除自选上方条件的元组
		 if(delsel!=null) {
			 try {
				 JDBCemo.getInstence();
				JDBCemo.connection.setAutoCommit(false);
				 condition=(String)( request.getSession().getAttribute("condition"));
				 JDBCemo.delete_stucourse(condition);
				 JDBCemo.connection.commit();
				 JDBCemo.connection.setAutoCommit(true);
				 System.out.println("一门课程删除事务成功提交");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 try {
					JDBCemo.connection.rollback();
					JDBCemo.connection.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.err.println("一门删除事务回滚失败");
				}
				 
			}
			
		 }
		 JDBCemo.destroy();
		response.sendRedirect("DelScore.jsp");
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
