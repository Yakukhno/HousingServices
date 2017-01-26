package ua.training.controller.command;

import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class Login implements Command {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";

    private static final String HOME_PATH = "/";
    private static final String USER_PATH = "/rest/user/%s";
    private static final String LOGIN_JSP = "/WEB-INF/view/login.jsp";

    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = HOME_PATH;
        String paramLogin = request.getParameter(PARAM_LOGIN);
        String paramPassword = request.getParameter(PARAM_PASSWORD);
        if ((paramLogin != null) && (paramPassword != null)) {
            Optional<User> user = Optional.empty();
            if (paramLogin.matches(EMAIL_REGEXP)) {
                user = userService.loginEmail(paramLogin, paramPassword);
            }
            pageToGo = user.map(sessionUser -> {
                request.getSession().setAttribute("user", sessionUser);
                return String.format(USER_PATH, sessionUser.getId());
            }).orElseGet(() -> {
                request.setAttribute("message", "Incorrect email or password");
                return LOGIN_JSP;
            });
        }
        return pageToGo;
    }
}


