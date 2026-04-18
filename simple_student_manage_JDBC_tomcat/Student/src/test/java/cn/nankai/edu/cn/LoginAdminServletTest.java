package cn.nankai.edu.cn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginAdminServlet测试")
class LoginAdminServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginAdminServlet servlet;

    @Test
    @DisplayName("测试空用户名 - 应该重定向到登录页面并设置错误消息")
    void testEmptyUsername() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("msg", "请输入用户名");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    @DisplayName("测试null用户名 - 应该重定向到登录页面并设置错误消息")
    void testNullUsername() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("msg", "请输入用户名");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    @DisplayName("测试空密码 - 应该重定向到登录页面并设置错误消息")
    void testEmptyPassword() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("pwd", "请输入密码");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    @DisplayName("测试null密码 - 应该重定向到登录页面并设置错误消息")
    void testNullPassword() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn(null);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("pwd", "请输入密码");
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    @DisplayName("测试有效用户名和密码 - 应该存储到session并重定向到首页")
    void testValidCredentials() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("username", "admin");
        verify(session).setAttribute("password", "123456");
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("测试用户名和密码都为空 - 应该只提示用户名错误")
    void testBothEmptyCredentials() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("msg", "请输入用户名");
        verify(session, never()).setAttribute(eq("pwd"), any());
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    @DisplayName("测试用户名包含特殊字符")
    void testUsernameWithSpecialCharacters() throws ServletException, IOException {
        String specialUsername = "admin' OR '1'='1";
        when(request.getParameter("username")).thenReturn(specialUsername);
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("username", specialUsername);
        verify(session).setAttribute("password", "123456");
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("测试长用户名和密码")
    void testLongCredentials() throws ServletException, IOException {
        String longUsername = "a".repeat(1000);
        String longPassword = "b".repeat(1000);
        when(request.getParameter("username")).thenReturn(longUsername);
        when(request.getParameter("password")).thenReturn(longPassword);
        when(request.getSession()).thenReturn(session);

        servlet.doGet(request, response);

        verify(session).setAttribute("username", longUsername);
        verify(session).setAttribute("password", longPassword);
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("测试doPost调用doGet")
    void testDoPostCallsDoGet() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("123456");
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute("username", "admin");
        verify(session).setAttribute("password", "123456");
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    @DisplayName("测试构造函数")
    void testConstructor() {
        LoginAdminServlet newServlet = new LoginAdminServlet();
        assertNotNull(newServlet);
    }
}
