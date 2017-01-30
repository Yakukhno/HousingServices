package ua.training.controller.command.user;

import org.junit.Test;
import org.mockito.InjectMocks;
import ua.training.controller.command.Command;
import ua.training.model.entities.person.User;
import ua.training.model.service.ServiceException;
import ua.training.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestGetUser {

    private UserService userService = mock(UserService.class);

    @InjectMocks
    private Command getUser = new GetUser(userService);

    @Test
    public void testExecute() throws ServletException, IOException {
        int userId = 3;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = mock(User.class);
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(userService.getUserById(userId)).thenReturn(user);

        getUser.execute(request, response);

        verify(request).setAttribute(anyString(), eq(user));
    }

    @Test(expected = ServiceException.class)
    public void testExecuteInvalidId() throws ServletException, IOException {
        int userId = 3;
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = mock(User.class);
        when(request.getRequestURI()).thenReturn("/rest/user/" + userId);
        when(userService.getUserById(userId)).thenThrow(mock(ServiceException.class));

        getUser.execute(request, response);
    }
}
