package ua.training.controller.command.exception;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Routes.EXCEPTION_JSP_PATH;

public class ExceptionPageCommand implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return EXCEPTION_JSP_PATH;
    }
}
