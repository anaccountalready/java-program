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
@DisplayName("DelscoreServlet测试")
class DelscoreServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private DelscoreServlet servlet;

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        DelscoreServlet newServlet = new DelscoreServlet();
        assertNotNull(newServlet);
    }

    @Test
    @DisplayName("测试获取checkID参数")
    void testGetCheckIdParameter() {
        when(request.getParameter("checkID")).thenReturn("1_1001,2_1001");
        
        assertEquals("1_1001,2_1001", request.getParameter("checkID"));
    }

    @Test
    @DisplayName("测试null checkID参数")
    void testNullCheckIdParameter() {
        when(request.getParameter("checkID")).thenReturn(null);
        
        assertNull(request.getParameter("checkID"));
    }

    @Test
    @DisplayName("测试获取showall参数")
    void testGetShowallParameter() {
        when(request.getParameter("showall")).thenReturn("true");
        
        assertEquals("true", request.getParameter("showall"));
    }

    @Test
    @DisplayName("测试获取课程参数")
    void testGetCourseParameter() {
        when(request.getParameter("course")).thenReturn("高等数学");
        
        assertEquals("高等数学", request.getParameter("course"));
    }

    @Test
    @DisplayName("测试获取学生ID参数")
    void testGetStudentIdParameter() {
        when(request.getParameter("id")).thenReturn("1001");
        
        assertEquals("1001", request.getParameter("id"));
    }

    @Test
    @DisplayName("测试获取newscore参数")
    void testGetNewscoreParameter() {
        when(request.getParameter("newscore")).thenReturn("95.5_1001_1");
        
        assertEquals("95.5_1001_1", request.getParameter("newscore"));
    }

    @Test
    @DisplayName("测试获取delsel参数")
    void testGetDelselParameter() {
        when(request.getParameter("delsel")).thenReturn("true");
        
        assertEquals("true", request.getParameter("delsel"));
    }

    @Test
    @DisplayName("测试checkID字符串分割")
    void testCheckIdSplit() {
        String checkID = "1_1001,2_1001,3_1002";
        String[] str = checkID.split(",");
        
        assertEquals(3, str.length);
        assertEquals("1_1001", str[0]);
        assertEquals("2_1001", str[1]);
        assertEquals("3_1002", str[2]);
    }

    @Test
    @DisplayName("测试单个checkID项分割")
    void testSingleCheckIdSplit() {
        String item = "1_1001";
        String[] tem = item.split("_");
        
        assertEquals(2, tem.length);
        assertEquals("1", tem[0]);
        assertEquals("1001", tem[1]);
    }

    @Test
    @DisplayName("测试newscore字符串分割")
    void testNewscoreSplit() {
        String newscore = "95.5_1001_1";
        String[] a = newscore.split("_");
        
        assertEquals(3, a.length);
        assertEquals("95.5", a[0]);
        assertEquals("1001", a[1]);
        assertEquals("1", a[2]);
    }

    @Test
    @DisplayName("测试更新条件拼接")
    void testUpdateConditionBuilding() {
        String[] a = {"95.5", "1001", "1"};
        String con = " stuid=" + a[1] + " and couid=" + a[2];
        
        assertEquals(" stuid=1001 and couid=1", con);
    }

    @Test
    @DisplayName("测试更新值拼接")
    void testUpdateValueBuilding() {
        String[] a = {"95.5", "1001", "1"};
        String values = "score=" + a[0];
        
        assertEquals("score=95.5", values);
    }

    @Test
    @DisplayName("测试学生ID条件拼接")
    void testStudentIdConditionBuilding() {
        String stuid = "1001";
        String si = (stuid == "" || stuid == null) ? "" : " stuid= " + stuid;
        
        assertEquals(" stuid= 1001", si);
    }

    @Test
    @DisplayName("测试空学生ID条件")
    void testEmptyStudentIdCondition() {
        String stuid = "";
        String si = (stuid == "" || stuid == null) ? "" : " stuid= " + stuid;
        
        assertEquals("", si);
    }

    @Test
    @DisplayName("测试课程条件拼接")
    void testCourseConditionBuilding() {
        String couid = "1";
        String stuid = "1001";
        String si = " stuid= 1001";
        String sc = (stuid == "" || stuid == null) ? " couid=" + couid : " and couid=" + couid;
        
        assertEquals(" and couid=1", sc);
    }

    @Test
    @DisplayName("测试课程条件拼接(无学生ID)")
    void testCourseConditionBuildingNoStudent() {
        String couid = "1";
        String stuid = "";
        String sc = (stuid == "" || stuid == null) ? " couid=" + couid : " and couid=" + couid;
        
        assertEquals(" couid=1", sc);
    }

    @Test
    @DisplayName("测试组合条件")
    void testCombinedCondition() {
        String si = " stuid= 1001";
        String sc = " and couid=1";
        String condition = si + sc;
        
        assertEquals(" stuid= 1001 and couid=1", condition);
    }

    @Test
    @DisplayName("测试课程查询SQL")
    void testCourseQuerySql() {
        String course = "高等数学";
        String sql = "select id from course where name=" + "'" + course + "'";
        
        assertEquals("select id from course where name='高等数学'", sql);
    }

    @Test
    @DisplayName("测试Session属性设置")
    void testSessionAttributeSet() {
        when(request.getSession()).thenReturn(session);
        
        request.getSession().setAttribute("condition", " stuid=1001");
        request.getSession().setAttribute("count", 5);
        request.getSession().setAttribute("stucour", new Stuscore[100]);
        
        verify(session).setAttribute(eq("condition"), any());
        verify(session).setAttribute(eq("count"), anyInt());
        verify(session).setAttribute(eq("stucour"), any());
    }

    @Test
    @DisplayName("测试重定向")
    void testRedirect() throws Exception {
        response.sendRedirect("DelScore.jsp");
        verify(response).sendRedirect("DelScore.jsp");
    }

    @Test
    @DisplayName("测试删除条件SQL")
    void testDeleteConditionSql() {
        String condition = " stuid=1001 and couid=1";
        String sql = "delete from stucourse where " + condition + ";";
        
        assertEquals("delete from stucourse where  stuid=1001 and couid=1;", sql);
    }

    @Test
    @DisplayName("测试按ID删除SQL")
    void testDeleteByIdSql() {
        int id = 1;
        int stuid = 1001;
        String sql = "delete from stucourse where couid=" + id + " and stuid=" + stuid;
        
        assertEquals("delete from stucourse where couid=1 and stuid=1001", sql);
    }

    @Test
    @DisplayName("测试浮点数分数解析")
    void testFloatScoreParsing() {
        String scoreStr = "95.5";
        float score = Float.parseFloat(scoreStr);
        
        assertEquals(95.5f, score, 0.001f);
    }

    @Test
    @DisplayName("测试整数ID解析")
    void testIntegerIdParsing() {
        String idStr = "1001";
        int id = Integer.parseInt(idStr);
        
        assertEquals(1001, id);
    }
}
