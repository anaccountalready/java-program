package cn.nankai.edu.cn;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exportScore")
public class ExportScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExportScoreServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Avg_score> scoreList = new ArrayList<>();

		JDBCemo.getInstence();
		JDBCemo.select("*", "v_stu_avgscore", null);

		try {
			while (JDBCemo.resulSet.next()) {
				Avg_score score = new Avg_score(
						JDBCemo.resulSet.getInt(1),
						JDBCemo.resulSet.getFloat(2)
				);
				scoreList.add(score);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JDBCemo.destroy();

		String fileName = ExcelExportUtil.generateFileName("学分绩");
		ExcelExportUtil.exportScoreExcel(scoreList, response, fileName);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
