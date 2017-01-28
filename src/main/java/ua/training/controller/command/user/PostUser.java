package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.MESSAGE;

public class PostUser implements Command {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_ROLE = "role";

    private static final String REGISTER_USER_JSP = "/WEB-INF/view/register_user.jsp";
    private static final String REGISTER_USER_PATH = "/rest/register_user";
    private static final String LOGIN_PATH = "/rest/login";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = REGISTER_USER_PATH;
        String name = request.getParameter(PARAM_NAME);
        String email = request.getParameter(PARAM_EMAIL);
        String password = request.getParameter(PARAM_PASSWORD);
        String role = request.getParameter(PARAM_ROLE);
        if ((name != null) && (email != null)
                && (password != null) && (role != null)) {
            try {
                userService.createNewUser(new User.Builder()
                        .setName(name)
                        .setEmail(email)
                        .setPassword(password)
                        .setRole(User.Role.valueOf(role))
                        .build()
                );
                pageToGo = LOGIN_PATH;
            } catch (ApplicationException e) {
                if (e.isUserMessage()) {
                    request.setAttribute(MESSAGE, e.getUserMessage());
                    pageToGo = REGISTER_USER_JSP;
                } else {
                    throw e;
                }
            }
        }
        return pageToGo;
    }
}