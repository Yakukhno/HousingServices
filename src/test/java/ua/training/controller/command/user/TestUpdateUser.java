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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestUpdateUser {

    private UserService userService = mock(UserService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    private String email = "Thishourses75@superrito.com";
    private String oldPassword = "2mYPg=NB;";
    private String newPassword = "VIT6Xh86P";
    private int userId = 3;

    private User user;

    private Command updateUser = new UpdateUser(userService);

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        user = new User.Builder()
                .setId(userId)
                .setName("Dave Kesler")
                .setEmail("Anythat53@jourrapide.com")
                .setPassword(oldPassword)
                .setRole(User.Role.valueOf("TENANT"))
                .build();
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getSession().getAttribute("user")).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
    }

    @Test
    public void testExecute() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(userService.getUserById(userId)).thenReturn(user);
        user.setEmail(email);
        user.setPassword(newPassword);

        updateUser.execute(request, response);

        verify(userService).updateUser(user, oldPassword);
    }

    @Test
    public void testExecuteEmptyEmail() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(userService.getUserById(userId)).thenReturn(user);
        user.setPassword(newPassword);

        updateUser.execute(request, response);

        verify(userService).updateUser(user, oldPassword);
    }

    @Test
    public void testExecuteEmptyNewPassword() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn("");
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(userService.getUserById(userId)).thenReturn(user);
        user.setEmail(email);

        updateUser.execute(request, response);

        verify(userService).updateUser(user, oldPassword);
    }

    @Test(expected = ApplicationException.class)
    public void testExecuteException()
            throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);
        doThrow(mock(ApplicationException.class))
                .when(userService).updateUser(any(), anyString());

        updateUser.execute(request, response);
    }

    @Test
    public void testExecuteExceptionWithUserMessage()
            throws ServletException, IOException {
        ApplicationException exception = mock(ApplicationException.class);
        String exceptionMessage = "Message";
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);
        when(exception.getUserMessage()).thenReturn(exceptionMessage);
        when(exception.isUserMessage()).thenReturn(true);
        doThrow(exception).when(userService).updateUser(any(), anyString());

        updateUser.execute(request, response);

        verify(request).setAttribute(anyString(), eq(exceptionMessage));
    }

    @Test
    public void testExecuteNullEmail() throws ServletException, IOException {
        when(request.getSession().getAttribute("user"))
                .thenReturn(mock(User.class));
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(newPassword);

        updateUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }

    @Test
    public void testExecuteNullOldPassword() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(null);
        when(request.getParameter("newPassword")).thenReturn(newPassword);

        updateUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }

    @Test
    public void testExecuteNullNewPassword() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("newPassword")).thenReturn(null);

        updateUser.execute(request, response);

        verify(userService, never()).createNewUser(any());
    }
}
