package ua.training.controller.command.user;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Routes.REGISTER_USER_JSP_PATH;

public class NewUserPageCommand implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return REGISTER_USER_JSP_PATH;
    }
}
