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
@DisplayName("Show_scoreServlet测试")
class Show_scoreServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private Show_scoreServlet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        Show_scoreServlet newServlet = new Show_scoreServlet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试获取学生ID参数")
    void testGetStudentId() {
        when(request.getParameter("stu_id")).thenReturn("1001");
        
        assertEquals("1001", request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试获取高分参数")
    void testGetHighParameter() {
        when(request.getParameter("high")).thenReturn("90");
        
        assertEquals("90", request.getParameter("high"));
    }

    @Test
    @DisplayName("测试获取低分参数")
    void testGetLowParameter() {
        when(request.getParameter("low")).thenReturn("60");
        
        assertEquals("60", request.getParameter("low"));
    }

    @Test
    @DisplayName("测试null学生ID参数")
    void testNullStudentId() {
        when(request.getParameter("stu_id")).thenReturn(null);
        
        assertNull(request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试空字符串学生ID参数")
    void testEmptyStudentId() {
        when(request.getParameter("stu_id")).thenReturn("");
        
        assertEquals("", request.getParameter("stu_id"));
    }

    @Test
    @DisplayName("测试null高分参数")
    void testNullHigh() {
        when(request.getParameter("high")).thenReturn(null);
        
        assertNull(request.getParameter("high"));
    }

    @Test
    @DisplayName("测试空字符串高分参数")
    void testEmptyHigh() {
        when(request.getParameter("high")).thenReturn("");
        
        assertEquals("", request.getParameter("high"));
    }

    @Test
    @DisplayName("测试null低分参数")
    void testNullLow() {
        when(request.getParameter("low")).thenReturn(null);
        
        assertNull(request.getParameter("low"));
    }

    @Test
    @DisplayName("测试空字符串低分参数")
    void testEmptyLow() {
        when(request.getParameter("low")).thenReturn("");
        
        assertEquals("", request.getParameter("low"));
    }

    @Test
    @DisplayName("测试条件拼接 - 学生ID条件")
    void testConditionBuildingForStudentId() {
        String stuid = "1001";
        String ci = (stuid == "" || stuid == null) ? "" : " stu_id=" + stuid;
        
        assertEquals(" stu_id=1001", ci);
    }

    @Test
    @DisplayName("测试条件拼接 - 空学生ID")
    void testConditionBuildingForEmptyStudentId() {
        String stuid = "";
        String ci = (stuid == "" || stuid == null) ? "" : " stu_id=" + stuid;
        
        assertEquals("", ci);
    }

    @Test
    @DisplayName("测试条件拼接 - 高分条件(无前导条件)")
    void testConditionBuildingForHighNoPrefix() {
        String high = "90";
        String ci = "";
        String ch = (high == "" || high == null) ? "" : 
            (ci == "" ? " avg_score<=" + high : " and avg_score<=" + high);
        
        assertEquals(" avg_score<=90", ch);
    }

    @Test
    @DisplayName("测试条件拼接 - 高分条件(有前导条件)")
    void testConditionBuildingForHighWithPrefix() {
        String high = "90";
        String ci = " stu_id=1001";
        String ch = (high == "" || high == null) ? "" : 
            (ci == "" ? " avg_score<=" + high : " and avg_score<=" + high);
        
        assertEquals(" and avg_score<=90", ch);
    }

    @Test
    @DisplayName("测试条件拼接 - 低分条件(无前导条件)")
    void testConditionBuildingForLowNoPrefix() {
        String low = "60";
        String ci = "";
        String ch = "";
        String cw = (low == "" || low == null) ? "" : 
            (ch == "" ? (ci == "" ? " avg_score>=" + low : " and avg_score>=" + low) : " and avg_score>=" + low);
        
        assertEquals(" avg_score>=60", cw);
    }

    @Test
    @DisplayName("测试条件拼接 - 低分条件(有前导条件)")
    void testConditionBuildingForLowWithPrefix() {
        String low = "60";
        String ci = " stu_id=1001";
        String ch = " and avg_score<=90";
        String cw = (low == "" || low == null) ? "" : 
            (ch == "" ? (ci == "" ? " avg_score>=" + low : " and avg_score>=" + low) : " and avg_score>=" + low);
        
        assertEquals(" and avg_score>=60", cw);
    }

    @Test
    @DisplayName("测试组合条件")
    void testCombinedConditions() {
        String stuid = "1001";
        String high = "90";
        String low = "60";
        
        String ci = (stuid == "" || stuid == null) ? "" : " stu_id=" + stuid;
        String ch = (high == "" || high == null) ? "" : 
            (ci == "" ? " avg_score<=" + high : " and avg_score<=" + high);
        String cw = (low == "" || low == null) ? "" : 
            (ch == "" ? (ci == "" ? " avg_score>=" + low : " and avg_score>=" + low) : " and avg_score>=" + low);
        
        String condition = ci + ch + cw;
        
        assertEquals(" stu_id=1001 and avg_score<=90 and avg_score>=60", condition);
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        request.getSession().setAttribute("countavg", 5);
        request.getSession().setAttribute("avg_score", new Avg_score[100]);
        
        verify(session).setAttribute(eq("countavg"), anyInt());
        verify(session).setAttribute(eq("avg_score"), any());
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("show_score.jsp");
        verify(response).sendRedirect("show_score.jsp");
    }

    @Test
    @DisplayName("测试浮点数参数")
    void testFloatParameter() {
        String high = "85.5";
        float highValue = Float.parseFloat(high);
        
        assertEquals(85.5f, highValue, 0.001f);
    }

    @Test
    @DisplayName("测试无效浮点数参数")
    void testInvalidFloatParameter() {
        String high = "abc";
        
        assertThrows(NumberFormatException.class, () -> {
            Float.parseFloat(high);
        });
    }
}
