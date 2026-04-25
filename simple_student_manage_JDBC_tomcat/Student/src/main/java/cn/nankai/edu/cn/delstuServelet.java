package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class delstuServelet
 */
@WebServlet("/delstu")
public class delstuServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public delstuServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //存储过程控制下的更新操作
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id=request.getParameter("stu_id");
		JDBCemo.getInstence();
		String sql="{call delstu(?,?)}";
		CallableStatement callstate;
		try {
			if(id == null || id.equals("")) {
				request.getSession().setAttribute("delResult", "fail");
				response.sendRedirect("del_stu.jsp");
				return;
			}
			callstate = JDBCemo.connection.prepareCall(sql);
			callstate.setInt(1, Integer.valueOf(id));
			callstate.registerOutParameter(2, java.sql.Types.INTEGER);
			callstate.execute();
			int output=callstate.getInt(2);
			System.out.println(output);
			if(output == 1) {
				request.getSession().setAttribute("delResult", "fail");
			} else {
				request.getSession().setAttribute("delResult", "success");
			}
		} catch (SQLException e) {
			request.getSession().setAttribute("delResult", "fail");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("delResult", "fail");
			e.printStackTrace();
		}
		JDBCemo.destroy();
		 response.sendRedirect("del_stu.jsp");
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
