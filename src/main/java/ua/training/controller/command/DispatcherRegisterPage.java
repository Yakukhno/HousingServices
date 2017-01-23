package ua.training.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherRegisterPage implements Command {

    private static final String REGISTER_DISPATCHER_JSP_PATH
            = "/WEB-INF/view/register_dispatcher.jsp";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return REGISTER_DISPATCHER_JSP_PATH;
    }
}