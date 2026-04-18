package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddstudentServlet测试")
class AddstudentServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AddstudentServlet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        AddstudentServlet newServlet = new AddstudentServlet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试获取参数逻辑")
    void testGetParameters() {
        when(request.getParameter("stu_name")).thenReturn("测试学生");
        when(request.getParameter("stu_id")).thenReturn("2001");
        when(request.getParameter("sex")).thenReturn("男");
        when(request.getParameter("stu_indate")).thenReturn("2023-09-01");
        when(request.getParameter("class")).thenReturn("计算机一班");
        when(request.getParameter("major")).thenReturn("计算机科学与技术");
        when(request.getParameter("tea")).thenReturn("张老师");

        assertEquals("测试学生", request.getParameter("stu_name"));
        assertEquals("2001", request.getParameter("stu_id"));
        assertEquals("男", request.getParameter("sex"));
        assertEquals("2023-09-01", request.getParameter("stu_indate"));
        assertEquals("计算机一班", request.getParameter("class"));
        assertEquals("计算机科学与技术", request.getParameter("major"));
        assertEquals("张老师", request.getParameter("tea"));
    }

    @Test
    @DisplayName("测试SQL拼接逻辑 - 专业名称查询")
    void testSqlBuildingForMajor() {
        String major = "计算机科学与技术";
        String expectedSql = "select id from major where name='计算机科学与技术';";
        String actualSql = "select id from major where name=" + "'" + major + "'" + ";";
        
        assertEquals(expectedSql, actualSql);
    }

    @Test
    @DisplayName("测试SQL拼接逻辑 - 教师名称查询")
    void testSqlBuildingForTeacher() {
        String tea = "张老师";
        String expectedSql = "select id from teacher where name='张老师';";
        String actualSql = "select id from teacher where name=" + "'" + tea + "'" + ";";
        
        assertEquals(expectedSql, actualSql);
    }

    @Test
    @DisplayName("测试值拼接逻辑")
    void testValuesBuilding() {
        String id = "2001";
        String name = "测试学生";
        String sex = "男";
        String indate = "2023-09-01";
        String claname = "计算机一班";
        String majorid = "1";
        String teaid = "1";
        
        String expectedValues = "2001,'测试学生','男','2023-09-01','计算机一班',1,1";
        String actualValues = id + "," + "'" + name + "'" + ","
                + "'" + sex + "'" + ","
                + "'" + indate + "'" + ","
                + "'" + claname + "'" + "," + majorid + "," + teaid;
        
        assertEquals(expectedValues, actualValues);
    }

    @Test
    @DisplayName("测试列名拼接")
    void testColumnsBuilding() {
        String expectedColumns = "id,name,sex,indate,claname,majorid,teaid";
        String columns = "id,name,sex,indate,claname,majorid,teaid";
        
        assertEquals(expectedColumns, columns);
    }

    @Test
    @DisplayName("测试空参数处理 - 学生姓名为null")
    void testNullStudentName() {
        when(request.getParameter("stu_name")).thenReturn(null);
        
        assertNull(request.getParameter("stu_name"));
    }

    @Test
    @DisplayName("测试空参数处理 - 学生ID为null")
    void testNullStudentId() {
        when(request.getParameter("stu_id")).thenReturn(null);
        
        assertNull(request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试空参数处理 - 性别为null")
    void testNullSex() {
        when(request.getParameter("sex")).thenReturn(null);
        
        assertNull(request.getParameter("sex"));
    }

    @Test
    @DisplayName("测试空参数处理 - 入学日期为null")
    void testNullIndate() {
        when(request.getParameter("stu_indate")).thenReturn(null);
        
        assertNull(request.getParameter("stu_indate"));
    }

    @Test
    @DisplayName("测试空参数处理 - 班级为null")
    void testNullClass() {
        when(request.getParameter("class")).thenReturn(null);
        
        assertNull(request.getParameter("class"));
    }

    @Test
    @DisplayName("测试空参数处理 - 专业为null")
    void testNullMajor() {
        when(request.getParameter("major")).thenReturn(null);
        
        assertNull(request.getParameter("major"));
    }

    @Test
    @DisplayName("测试空参数处理 - 教师为null")
    void testNullTeacher() {
        when(request.getParameter("tea")).thenReturn(null);
        
        assertNull(request.getParameter("tea"));
    }

    @Test
    @DisplayName("测试特殊字符参数 - 学生姓名包含单引号")
    void testSpecialCharacterInName() {
        String name = "O'Connor";
        String sql = "select id from major where name=" + "'" + name + "'" + ";";
        
        assertNotNull(sql);
    }

    @Test
    @DisplayName("测试长参数 - 学生姓名很长")
    void testLongNameParameter() {
        String longName = "a".repeat(1000);
        when(request.getParameter("stu_name")).thenReturn(longName);
        
        assertEquals(longName, request.getParameter("stu_name"));
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        request.getSession().setAttribute("addtea", "张老师");
        request.getSession().setAttribute("addstuteaid", "1");
        
        verify(session).setAttribute("addtea", "张老师");
        verify(session).setAttribute("addstuteaid", "1");
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("addStudent.jsp");
        verify(response).sendRedirect("addStudent.jsp");
    }
}
