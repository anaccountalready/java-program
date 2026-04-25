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

@WebServlet("/exportStudent")
public class ExportStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExportStudentServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String condition = (String) request.getSession().getAttribute("export_stu_condition");
		
		List<Student> studentList = new ArrayList<>();
		List<Integer> teaidList = new ArrayList<>();
		List<Integer> majoridList = new ArrayList<>();

		JDBCemo.getInstence();
		JDBCemo.select("*", "student", condition);

		try {
			while (JDBCemo.resulSet.next()) {
				Student stu = new Student(
						JDBCemo.resulSet.getInt(1),
						JDBCemo.resulSet.getString(2),
						JDBCemo.resulSet.getString(3),
						JDBCemo.resulSet.getDate(4),
						JDBCemo.resulSet.getString(5),
						JDBCemo.resulSet.getInt(6),
						JDBCemo.resulSet.getInt(7)
				);
				studentList.add(stu);
				teaidList.add(JDBCemo.resulSet.getInt(7));
				majoridList.add(JDBCemo.resulSet.getInt(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < studentList.size(); i++) {
			Student stu = studentList.get(i);
			int teaid = teaidList.get(i);
			int majorid = majoridList.get(i);

			try {
				JDBCemo.select("name", "teacher", " id=" + teaid);
				if (JDBCemo.resulSet.next()) {
					stu.setTeacher(JDBCemo.resulSet.getString("name"));
				}

				JDBCemo.select("name", "major", " id=" + majorid);
				if (JDBCemo.resulSet.next()) {
					stu.setMajor(JDBCemo.resulSet.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		JDBCemo.destroy();

		String fileName = ExcelExportUtil.generateFileName("学生信息");
		ExcelExportUtil.exportStudentExcel(studentList, response, fileName);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
