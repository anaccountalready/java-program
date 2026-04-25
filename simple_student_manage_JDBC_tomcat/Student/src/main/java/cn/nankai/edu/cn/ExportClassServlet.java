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

@WebServlet("/exportClass")
public class ExportClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExportClassServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Classinfo> classList = new ArrayList<>();
		List<Integer> teaidList = new ArrayList<>();

		JDBCemo.getInstence();
		JDBCemo.select("*", "class", null);

		try {
			while (JDBCemo.resulSet.next()) {
				Classinfo classInfo = new Classinfo(
						JDBCemo.resulSet.getString("name"),
						JDBCemo.resulSet.getInt("num"),
						JDBCemo.resulSet.getInt("teaid")
				);
				classList.add(classInfo);
				teaidList.add(JDBCemo.resulSet.getInt("teaid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < classList.size(); i++) {
			Classinfo classInfo = classList.get(i);
			int teaid = teaidList.get(i);
			
			try {
				JDBCemo.select("name", "teacher", " id=" + teaid);
				if (JDBCemo.resulSet.next()) {
					classInfo.setTeanameFromResult(JDBCemo.resulSet.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		JDBCemo.destroy();

		String fileName = ExcelExportUtil.generateFileName("班级信息");
		ExcelExportUtil.exportClassExcel(classList, response, fileName);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
