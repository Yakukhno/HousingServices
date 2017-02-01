package ua.training.controller.servlet;

import org.junit.Before;
import org.junit.Test;
import ua.training.controller.command.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class TestFrontController {

    private FrontController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Command commandTwo;

    @Before
    public void before() {
        controller = new FrontController();
        controller = spy(controller);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        HashMap<String, Command> commands = new HashMap<>();
        commandTwo = mock(Command.class);
        commands.put("one", mock(Command.class));
        commands.put("two", commandTwo);
        commands.put("three", mock(Command.class));
        controller.setCommands(commands);
    }

    @Test
    public void testDoGetDoPost() throws ServletException, IOException {
        doNothing().when(controller).processRequest(request, response);

        controller.doGet(request, response);
        controller.doPost(request, response);

        verify(controller, times(2)).processRequest(request, response);
    }

    @Test
    public void testProcessRequestForward()
            throws ServletException, IOException {
        String path = "/test.jsp";
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        doReturn("two").when(controller).formCommand(request);
        when(commandTwo.execute(request, response)).thenReturn(path);

        controller.processRequest(request, response);

        verify(commandTwo).execute(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testProcessRequestRedirect()
            throws ServletException, IOException {
        String path = "/rest/test";
        doReturn("two").when(controller).formCommand(request);
        when(commandTwo.execute(request, response)).thenReturn(path);

        controller.processRequest(request, response);

        verify(commandTwo).execute(request, response);
        verify(response).sendRedirect(path);
    }
}
