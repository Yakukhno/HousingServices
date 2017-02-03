package ua.training.controller.command.exception;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorPage implements Command {

    private static final String ERROR_JSP_PATH = "/WEB-INF/view/error/error.jsp";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return ERROR_JSP_PATH;
    }
}
