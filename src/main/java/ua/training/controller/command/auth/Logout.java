package ua.training.controller.command.auth;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.USER;

public class Logout implements Command {

    private static final String LOGIN_PATH = "/rest/login";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().removeAttribute(USER);
        return LOGIN_PATH;
    }
}
