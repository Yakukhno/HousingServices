package ua.training.controller.command.auth;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginPage implements Command {

    private static final String LOGIN_JSP_PATH = "/WEB-INF/view/login.jsp";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return LOGIN_JSP_PATH;
    }
}
