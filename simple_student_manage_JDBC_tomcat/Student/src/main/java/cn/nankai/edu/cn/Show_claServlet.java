package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Show_cla")
public class Show_claServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Show_claServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageStr = request.getParameter("page");
		int currentPage = 1;
		if (pageStr != null && !pageStr.isEmpty()) {
			try {
				currentPage = Integer.parseInt(pageStr);
			} catch (NumberFormatException e) {
				currentPage = 1;
			}
		}

		int totalCount = JDBCemo.selectCount("class", null);
		PageBean pageBean = new PageBean(currentPage, totalCount);

		Classinfo[] cla = new Classinfo[pageBean.getPageSize()];
		int[] teaid = new int[pageBean.getPageSize()];
		int count = 0;
		JDBCemo.getInstence();
		JDBCemo.selectWithPaging("*", "class", null, pageBean.getStartIndex(), pageBean.getPageSize());
		int i = 0;
		try {
			while (JDBCemo.resulSet.next()) {
				cla[i] = new Classinfo(JDBCemo.resulSet.getString("name"), JDBCemo.resulSet.getInt("num"),
						JDBCemo.resulSet.getInt("teaid"));
				teaid[i] = JDBCemo.resulSet.getInt("teaid");
				i++;
			}
			count = i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int j = 0; j < count; j++) {
			cla[j].setTeaname(teaid[j]);
		}
		request.getSession().setAttribute("countcla", count);
		System.out.println("count" + count);
		request.getSession().setAttribute("clainfo", cla);
		request.getSession().setAttribute("pageBean", pageBean);
		JDBCemo.destroy();
		response.sendRedirect("show_cla.jsp");

		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
