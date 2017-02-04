package ua.training.controller.command.auth;

import org.junit.Test;
import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class TestLogoutCommand {

    private Command postLogoutCommand = new PostLogoutCommand();

    @Test
    public void testExecute() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        postLogoutCommand.execute(request, response);

        verify(session).removeAttribute(anyString());
    }
}
