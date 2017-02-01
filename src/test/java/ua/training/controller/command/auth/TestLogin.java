package ua.training.controller.command.auth;

import org.junit.Before;
import org.junit.Test;
import ua.training.controller.command.Command;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestLogin {

    private UserService userService = mock(UserService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String email = "Thishourses75@superrito.com";
    private String password = "8Z8zdDAXq";

    private Command postLogin = new PostLogin(userService);

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testExecute() throws ServletException, IOException {
        User user = mock(User.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userService.loginEmail(email, password)).thenReturn(user);
        when(request.getSession()).thenReturn(session);

        postLogin.execute(request, response);

        verify(session).setAttribute(anyString(), eq(user));
    }

    @Test(expected = ApplicationException.class)
    public void testExecuteException()
            throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        doThrow(mock(ApplicationException.class))
                .when(userService).loginEmail(email, password);

        postLogin.execute(request, response);
        verify(session, never()).setAttribute(anyString(), any(User.class));
    }

    @Test
    public void testExecuteExceptionWithUserMessage()
            throws ServletException, IOException {
        ApplicationException exception = mock(ApplicationException.class);
        String exceptionMessage = "Message";
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(exception.getUserMessage()).thenReturn(exceptionMessage);
        when(exception.isUserMessage()).thenReturn(true);
        doThrow(exception).when(userService).loginEmail(email, password);

        postLogin.execute(request, response);

        verify(request).setAttribute(anyString(), eq(exceptionMessage));
    }

    @Test
    public void testExecuteNullEmail() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(password);

        postLogin.execute(request, response);

        verify(userService, never()).loginEmail(any(), any());
    }

    @Test
    public void testExecuteNullPassword() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(null);

        postLogin.execute(request, response);

        verify(userService, never()).loginEmail(any(), any());
    }

    @Test
    public void testExecuteNullEmailPassword()
            throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        postLogin.execute(request, response);

        verify(userService, never()).loginEmail(any(), any());
    }
}
