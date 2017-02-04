package ua.training.controller.command.user;

import ua.training.controller.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserPageCommand implements Command {

    private static final String REGISTER_USER_JSP_PATH
            = "/WEB-INF/view/user/new_user.jsp";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        return REGISTER_USER_JSP_PATH;
    }
}
