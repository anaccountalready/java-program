package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Show_claServlet
 */
@WebServlet("/Show_cla")
public class Show_claServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Show_claServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Classinfo []cla=new Classinfo[100];
		int []teaid=new int[100];
		int count=0;
		JDBCemo.getInstence();
		JDBCemo.select("*", "class", null);
		int i=0;
		try {
			while(JDBCemo.resulSet.next()) {
				cla[i]=new Classinfo(JDBCemo.resulSet.getString("name"),JDBCemo.resulSet.getInt("num"),JDBCemo.resulSet.getInt("teaid"));
				teaid[i]=JDBCemo.resulSet.getInt("teaid");
				i++;
			}
			count=i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int j=0;j<count;j++) {
			cla[j].setTeaname(teaid[j]);
		}
		request.getSession().setAttribute("countcla", count);
		System.out.println("count"+count);
		request.getSession().setAttribute("clainfo", cla);
		JDBCemo.destroy();
		response.sendRedirect("show_cla.jsp");
		
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
