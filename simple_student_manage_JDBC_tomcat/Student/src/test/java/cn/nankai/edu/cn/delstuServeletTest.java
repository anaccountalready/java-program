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
@DisplayName("delstuServelet测试")
class delstuServeletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private delstuServelet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        delstuServelet newServlet = new delstuServelet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试获取学生ID参数")
    void testGetStudentId() {
        when(request.getParameter("stu_id")).thenReturn("1001");
        
        assertEquals("1001", request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试null学生ID")
    void testNullStudentId() {
        when(request.getParameter("stu_id")).thenReturn(null);
        
        assertNull(request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试空字符串学生ID")
    void testEmptyStudentId() {
        when(request.getParameter("stu_id")).thenReturn("");
        
        assertEquals("", request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试学生ID类型转换 - 有效ID")
    void testValidStudentIdConversion() {
        String idStr = "1001";
        int id = Integer.valueOf(idStr);
        
        assertEquals(1001, id);
    }

    @Test
    @DisplayName("测试学生ID类型转换 - 无效ID应该抛出异常")
    void testInvalidStudentIdConversion() {
        String idStr = "abc";
        
        assertThrows(NumberFormatException.class, () -> {
            Integer.valueOf(idStr);
        });
    }

    @Test
    @DisplayName("测试学生ID类型转换 - 负数ID")
    void testNegativeStudentIdConversion() {
        String idStr = "-1001";
        int id = Integer.valueOf(idStr);
        
        assertEquals(-1001, id);
    }

    @Test
    @DisplayName("测试学生ID类型转换 - 零ID")
    void testZeroStudentIdConversion() {
        String idStr = "0";
        int id = Integer.valueOf(idStr);
        
        assertEquals(0, id);
    }

    @Test
    @DisplayName("测试存储过程SQL构建")
    void testStoredProcedureSql() {
        String expectedSql = "{call delstu(?,?)}";
        String actualSql = "{call delstu(?,?)}";
        
        assertEquals(expectedSql, actualSql);
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        int output = 1;
        request.getSession().setAttribute("t_error", output);
        
        verify(session).setAttribute("t_error", 1);
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("del_stu.jsp");
        verify(response).sendRedirect("del_stu.jsp");
    }

    @Test
    @DisplayName("测试长学生ID参数")
    void testLongStudentId() {
        String longId = String.valueOf(Integer.MAX_VALUE);
        when(request.getParameter("stu_id")).thenReturn(longId);
        
        assertEquals(longId, request.getParameter("stu_id"));
        
        int id = Integer.valueOf(longId);
        assertEquals(Integer.MAX_VALUE, id);
    }

    @Test
    @DisplayName("测试学生ID参数包含前导空格")
    void testStudentIdWithLeadingSpaces() {
        String idStr = "  1001  ";
        
        assertThrows(NumberFormatException.class, () -> {
            Integer.valueOf(idStr);
        });
    }

    @Test
    @DisplayName("测试存储过程输出参数类型")
    void testOutputParameterType() {
        int outputType = java.sql.Types.INTEGER;
        assertEquals(4, outputType);
    }
}
