package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Show_score")
public class Show_scoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Show_scoreServlet() {
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

		String stuid = request.getParameter("stu_id");
		String high = request.getParameter("high");
		String low = request.getParameter("low");
		JDBCemo.getInstence();
		String ci;
		String ch;
		String cw;
		String condition;
		if (stuid == "" || stuid == null)
			ci = "";
		else
			ci = " stu_id=" + stuid;
		if (high == "" || high == null)
			ch = "";
		else {
			if (ci == "")
				ch = " avg_score<=" + high;
			else
				ch = " and avg_score<=" + high;
		}
		if (low == "" || low == null)
			cw = "";
		else {
			if (ch == "") {
				if (ci == "")
					cw = " avg_score>=" + low;
				else
					cw = " and avg_score>=" + low;
			} else
				cw = " and avg_score>=" + low;
		}
		condition = ci + ch + cw;

		int totalCount = JDBCemo.selectCount("v_stu_avgscore", condition);
		PageBean pageBean = new PageBean(currentPage, totalCount);

		JDBCemo.getInstence();
		JDBCemo.selectWithPaging("*", "v_stu_avgscore", condition, pageBean.getStartIndex(), pageBean.getPageSize());
		Avg_score[] stu = new Avg_score[pageBean.getPageSize()];
		int i = 0;
		try {
			while (JDBCemo.resulSet.next()) {
				stu[i] = new Avg_score(JDBCemo.resulSet.getInt(1), JDBCemo.resulSet.getFloat(2));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JDBCemo.destroy();
		request.getSession().setAttribute("countavg", i);
		request.getSession().setAttribute("avg_score", stu);
		request.getSession().setAttribute("pageBean", pageBean);
		request.getSession().setAttribute("searchParams", buildSearchParams(stuid, high, low));
		String exportCondition = (condition != null && condition.isEmpty()) ? null : condition;
		request.getSession().setAttribute("export_score_condition", exportCondition);
		response.sendRedirect("show_score.jsp");
		return;
	}

	private String buildSearchParams(String stuid, String high, String low) {
		StringBuilder sb = new StringBuilder();
		if (stuid != null && !stuid.isEmpty()) {
			sb.append("&stu_id=").append(stuid);
		}
		if (high != null && !high.isEmpty()) {
			sb.append("&high=").append(high);
		}
		if (low != null && !low.isEmpty()) {
			sb.append("&low=").append(low);
		}
		return sb.toString();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
