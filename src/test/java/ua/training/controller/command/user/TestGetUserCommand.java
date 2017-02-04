package ua.training.controller.command.user;

import org.junit.Test;
import ua.training.controller.command.Command;
import ua.training.exception.AccessForbiddenException;
import ua.training.model.entities.person.User;
import ua.training.exception.ServiceException;
import ua.training.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestGetUserCommand {

    private UserService userService = mock(UserService.class);

    private Command getUserCommand = new GetUserCommand(userService);

    @Test
    public void testExecute() throws ServletException, IOException {
        int userId = 3;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = mock(User.class);
        User sessionUser = mock(User.class);
        when(sessionUser.getId()).thenReturn(userId);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(request.getSession().getAttribute(any())).thenReturn(sessionUser);
        when(userService.getUserById(userId)).thenReturn(user);

        getUserCommand.execute(request, response);

        verify(request).setAttribute(anyString(), eq(user));
    }

    @Test(expected = ServiceException.class)
    public void testExecuteInvalidId() throws ServletException, IOException {
        int userId = 3;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User sessionUser = mock(User.class);
        when(sessionUser.getId()).thenReturn(userId);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(request.getSession().getAttribute(any())).thenReturn(sessionUser);
        when(userService.getUserById(userId)).thenThrow(mock(ServiceException.class));

        getUserCommand.execute(request, response);
    }

    @Test(expected = AccessForbiddenException.class)
    public void testExecuteInvalidAccess() throws ServletException, IOException {
        int userId = 3;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User sessionUser = mock(User.class);
        when(sessionUser.getId()).thenReturn(4);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(request.getSession().getAttribute(any())).thenReturn(sessionUser);
        when(userService.getUserById(userId)).thenThrow(mock(ServiceException.class));

        getUserCommand.execute(request, response);
    }
}
