package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Show_scoreServlet
 */
@WebServlet("/Show_score")
public class Show_scoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Show_scoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String stuid=request.getParameter("stu_id");
		String high=request.getParameter("high");
		String low=request.getParameter("low");
		JDBCemo.getInstence();
		String ci;
		String ch;
		String cw;
		String condition;
		if(stuid==""||stuid==null)ci="";
		else ci=" stu_id="+stuid;
		if(high==""||high==null)ch="";
		else {
			if(ci=="")ch=" avg_score<="+high;
			else ch=" and avg_score<=" +high;
			
		}
		if(low==""||low==null)cw="";
		else {
			if(ch=="") {
				if(ci=="")cw=" avg_score>="+low;
				else cw=" and avg_score>="+low;
			}
			else cw=" and avg_score>="+low;
		}
		condition=ci+ch+cw;
		JDBCemo.getInstence();
		JDBCemo.select("*", "v_stu_avgscore", condition);
		//System.out.println(condition);
		Avg_score[] stu = new Avg_score[100];
		int i=0;
		try {
			while(JDBCemo.resulSet.next()) {
				stu[i]=new Avg_score(JDBCemo.resulSet.getInt(1),JDBCemo.resulSet.getFloat(2));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JDBCemo.destroy();
		request.getSession().setAttribute("countavg", i);
		request.getSession().setAttribute("avg_score", stu);
		 response.sendRedirect("show_score.jsp");
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
