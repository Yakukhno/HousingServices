package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.model.entities.person.User;
import ua.training.model.service.ServiceException;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.USER;

public class GetUser implements Command {

    private static final String USER_JSP_PATH = "/WEB-INF/view/user.jsp";

    private UserService userService;

    public GetUser() {
        userService = UserServiceImpl.getInstance();
    }

    GetUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int userId = getUserIdFromRequest(request);
        User user;
        if (userId == ((User) request.getSession().getAttribute(USER)).getId()) {
            user = userService.getUserById(userId);
        } else {
            throw new ServiceException("Resource not found!");
        }
        request.setAttribute(USER, user);
        return USER_JSP_PATH;
    }

    private int getUserIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String userId = uri.substring(uri.lastIndexOf('/') + 1);
        return Integer.parseInt(userId);
    }
}
