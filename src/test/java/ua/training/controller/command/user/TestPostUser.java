package ua.training.controller.command.user;

import org.junit.Before;
import org.junit.Test;
import ua.training.controller.command.Command;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestPostUser {

    private UserService userService = mock(UserService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String name = "Dave Kesler";
    private String email = "Thishourses75@superrito.com";
    private String password = "2mYPg=NB;";
    private String role = "TENANT";

    private Command postUser = new PostUser(userService);

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testExecute() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(role);
        User user = new User.Builder()
                .setName(name)
                .setEmail(email)
                .setPassword(password)
                .setRole(User.Role.valueOf(role))
                .build();

        postUser.execute(request, response);

        verify(userService).createNewUser(user);
    }

    @Test(expected = ApplicationException.class)
    public void testExecuteException()
            throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(role);
        doThrow(mock(ApplicationException.class))
                .when(userService).createNewUser(any());

        postUser.execute(request, response);
    }

    @Test
    public void testExecuteExceptionWithUserMessage()
            throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(role);
        ApplicationException exception = mock(ApplicationException.class);
        String exceptionMessage = "Message";
        when(exception.getUserMessage()).thenReturn(exceptionMessage);
        when(exception.isUserMessage()).thenReturn(true);
        doThrow(exception).when(userService).createNewUser(any());

        postUser.execute(request, response);

        verify(request).setAttribute(anyString(), eq(exceptionMessage));
    }

    @Test
    public void testExecuteNullName() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(role);

        postUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }

    @Test
    public void testExecuteNullEmail() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(role);

        postUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }

    @Test
    public void testExecuteNullPassword() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("role")).thenReturn(role);

        postUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }

    @Test
    public void testExecuteNullRole() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("role")).thenReturn(null);

        postUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }
}
