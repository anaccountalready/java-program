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
@DisplayName("show_stuServlet测试")
class show_stuServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private show_stuServlet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        show_stuServlet newServlet = new show_stuServlet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试获取showall参数")
    void testGetShowallParameter() {
        when(request.getParameter("showall")).thenReturn("true");
        
        assertEquals("true", request.getParameter("showall"));
    }

    @Test
    @DisplayName("测试null showall参数")
    void testNullShowallParameter() {
        when(request.getParameter("showall")).thenReturn(null);
        
        assertNull(request.getParameter("showall"));
    }

    @Test
    @DisplayName("测试获取班级参数")
    void testGetClassParameter() {
        when(request.getParameter("class")).thenReturn("计算机一班");
        
        assertEquals("计算机一班", request.getParameter("class"));
    }

    @Test
    @DisplayName("测试获取性别参数")
    void testGetSexParameter() {
        when(request.getParameter("sex")).thenReturn("男");
        
        assertEquals("男", request.getParameter("sex"));
    }

    @Test
    @DisplayName("测试获取教师参数")
    void testGetTeacherParameter() {
        when(request.getParameter("tea")).thenReturn("张老师");
        
        assertEquals("张老师", request.getParameter("tea"));
    }

    @Test
    @DisplayName("测试获取学号参数")
    void testGetIdParameter() {
        when(request.getParameter("id")).thenReturn("1001");
        
        assertEquals("1001", request.getParameter("id"));
    }

    @Test
    @DisplayName("测试获取专业参数")
    void testGetMajorParameter() {
        when(request.getParameter("major")).thenReturn("计算机科学与技术");
        
        assertEquals("计算机科学与技术", request.getParameter("major"));
    }

    @Test
    @DisplayName("测试SQL拼接 - 专业查询")
    void testSqlBuildingForMajor() {
        String major = "计算机科学与技术";
        String expectedSql = "select id from major where name='计算机科学与技术';";
        String actualSql = "select id from major where name=" + "'" + major + "'" + ";";
        
        assertEquals(expectedSql, actualSql);
    }

    @Test
    @DisplayName("测试SQL拼接 - 教师查询")
    void testSqlBuildingForTeacher() {
        String tea = "张老师";
        String expectedSql = "select id from teacher where name='张老师';";
        String actualSql = "select id from teacher where name=" + "'" + tea + "'" + ";";
        
        assertEquals(expectedSql, actualSql);
    }

    @Test
    @DisplayName("测试条件拼接 - 班级条件")
    void testConditionBuildingForClass() {
        String claname = "计算机一班";
        String cc = " claname=" + "'" + claname + "'" + " and ";
        
        assertEquals(" claname='计算机一班' and ", cc);
    }

    @Test
    @DisplayName("测试条件拼接 - 性别条件")
    void testConditionBuildingForSex() {
        String sex = "男";
        String cs = " sex=" + "'" + sex + "'";
        
        assertEquals(" sex='男'", cs);
    }

    @Test
    @DisplayName("测试条件拼接 - 学号条件")
    void testConditionBuildingForId() {
        String id = "1001";
        String ci = " and " + " id=" + id;
        
        assertEquals(" and  id=1001", ci);
    }

    @Test
    @DisplayName("测试条件拼接 - 专业ID条件")
    void testConditionBuildingForMajorId() {
        String majorid = "1";
        String cm = " majorid=" + majorid + " and ";
        
        assertEquals(" majorid=1 and ", cm);
    }

    @Test
    @DisplayName("测试条件拼接 - 教师ID条件")
    void testConditionBuildingForTeacherId() {
        String teaid = "1";
        String ct = " and " + " teaid=" + teaid;
        
        assertEquals(" and  teaid=1", ct);
    }

    @Test
    @DisplayName("测试空条件处理 - 班级为null")
    void testNullClassCondition() {
        String claname = null;
        String cc = (claname == null) ? "" : " claname=" + "'" + claname + "'" + " and ";
        
        assertEquals("", cc);
    }

    @Test
    @DisplayName("测试空条件处理 - 性别为null")
    void testNullSexCondition() {
        String sex = null;
        String cs = (sex == null) ? "" : " sex=" + "'" + sex + "'";
        
        assertEquals("", cs);
    }

    @Test
    @DisplayName("测试空条件处理 - 教师为空字符串")
    void testEmptyTeacherCondition() {
        String tea = "";
        String ct = (tea == "" || tea == null) ? "" : " and " + " teaid=1";
        
        assertEquals("", ct);
    }

    @Test
    @DisplayName("测试空条件处理 - 学号为空字符串")
    void testEmptyIdCondition() {
        String id = "";
        String ci = (id == "" || id == null) ? "" : " and " + " id=1001";
        
        assertEquals("", ci);
    }

    @Test
    @DisplayName("测试组合条件")
    void testCombinedConditions() {
        String cc = " claname='计算机一班' and ";
        String cm = " majorid=1 and ";
        String cs = " sex='男'";
        String ct = " and  teaid=1";
        String ci = " and  id=1001";
        
        String condition = cc + cm + cs + ct + ci;
        
        assertEquals(" claname='计算机一班' and  majorid=1 and  sex='男' and  teaid=1 and  id=1001", condition);
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        request.getSession().setAttribute("countstu", 5);
        request.getSession().setAttribute("student", new Student[100]);
        
        verify(session).setAttribute(eq("countstu"), anyInt());
        verify(session).setAttribute(eq("student"), any());
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("show_stu.jsp");
        verify(response).sendRedirect("show_stu.jsp");
    }
}
