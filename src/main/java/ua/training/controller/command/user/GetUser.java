package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.exception.AccessForbiddenException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.USER;

public class GetUser implements Command {

    private static final String USER_JSP_PATH = "/WEB-INF/view/user/user.jsp";

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
        User user;
        User sessionUser = (User) request.getSession().getAttribute(USER);
        int userId = getUserIdFromRequest(request);
        if (userId == sessionUser.getId()) {
            user = userService.getUserById(userId);
        } else {
            throw new AccessForbiddenException();
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
