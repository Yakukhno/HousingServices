package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.USER;

public class GetUser implements Command {

    private static final String USER_JSP_PATH = "/WEB-INF/view/user.jsp";
    private static final String ERROR = "error";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(getUserIdFromRequest(request));
        return userService.getUserById(userId)
                .map(user -> {
                    request.setAttribute(USER, user);
                    return USER_JSP_PATH;
                })
                .orElse(ERROR);
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(uri.lastIndexOf('/') + 1);
        return uri;
    }
}
