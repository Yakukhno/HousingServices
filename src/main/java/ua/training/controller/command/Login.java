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

    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";
    private static final String ACCOUNT_REGEXP = "[1-9]\\d{3}";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/";
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        if (login != null && password != null) {
            Optional<User> user = Optional.empty();
            if (login.matches(EMAIL_REGEXP)) {
                user = userService.loginEmail(login, password);
            } else if (login.matches(ACCOUNT_REGEXP)) {
                user = userService.loginAccount(Integer.parseInt(login),
                                                    password);
            }
            if (user.isPresent()) {
                User sessionUser = user.get();
                request.getSession().setAttribute("user", sessionUser);
                request.setAttribute("id", sessionUser.getId());
                if (sessionUser.getRole().equals(User.Role.TENANT)) {
                    pageToGo = "/rest/tenant/" + sessionUser.getId();
                } else if (sessionUser.getRole().equals(User.Role.DISPATCHER)) {
                    pageToGo = "/rest/dispatcher/" + sessionUser.getId();
                }
            }
        }
        return pageToGo;
    }
}


