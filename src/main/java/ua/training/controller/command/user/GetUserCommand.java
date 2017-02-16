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
import static ua.training.controller.Routes.USER_JSP_PATH;

public class GetUserCommand implements Command {

    private UserService userService;

    public GetUserCommand() {
        userService = UserServiceImpl.getInstance();
    }

    GetUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = (User) request.getSession().getAttribute(USER);
        int userId = getUserIdFromRequest(request);
        User user;
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
