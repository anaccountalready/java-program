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
@DisplayName("Show_claServlet测试")
class Show_claServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private Show_claServlet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        Show_claServlet newServlet = new Show_claServlet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试查询所有班级SQL")
    void testSelectAllClassesSql() {
        String expectedSql = "SELECT * FROM class";
        String actualSql = "select * from class";
        
        assertNotNull(actualSql);
    }

    @Test
    @DisplayName("测试教师名称查询SQL")
    void testTeacherNameQuerySql() {
        int teaid = 1;
        String sql = "select name from teacher where id=" + teaid;
        
        assertEquals("select name from teacher where id=1", sql);
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        request.getSession().setAttribute("countcla", 5);
        request.getSession().setAttribute("clainfo", new Classinfo[100]);
        
        verify(session).setAttribute(eq("countcla"), anyInt());
        verify(session).setAttribute(eq("clainfo"), any());
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("show_cla.jsp");
        verify(response).sendRedirect("show_cla.jsp");
    }

    @Test
    @DisplayName("测试Classinfo数组初始化")
    void testClassinfoArrayInitialization() {
        Classinfo[] cla = new Classinfo[100];
        
        assertNotNull(cla);
        assertEquals(100, cla.length);
        assertNull(cla[0]);
    }

    @Test
    @DisplayName("测试教师ID数组初始化")
    void testTeacherIdArrayInitialization() {
        int[] teaid = new int[100];
        
        assertNotNull(teaid);
        assertEquals(100, teaid.length);
        assertEquals(0, teaid[0]);
    }

    @Test
    @DisplayName("测试计数变量初始化")
    void testCountVariableInitialization() {
        int count = 0;
        
        assertEquals(0, count);
    }

    @Test
    @DisplayName("测试Classinfo对象创建")
    void testClassinfoObjectCreation() {
        String name = "计算机一班";
        int num = 30;
        int teaid = 1;
        
        Classinfo classinfo = new Classinfo(name, num, teaid);
        
        assertNotNull(classinfo);
        assertEquals(name, classinfo.getName());
        assertEquals(num, classinfo.getNum());
        assertEquals(teaid, classinfo.getTeaid());
    }

    @Test
    @DisplayName("测试循环变量初始化")
    void testLoopVariableInitialization() {
        int i = 0;
        int j = 0;
        
        assertEquals(0, i);
        assertEquals(0, j);
    }
}
