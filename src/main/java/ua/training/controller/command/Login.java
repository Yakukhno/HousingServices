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
    private static final String TENANT_PATH = "/rest/tenant/%s";
    private static final String DISPATCHER_PATH = "/rest/dispatcher/%s";
    private static final String LOGIN_JSP = "/WEB-INF/view/login.jsp";

    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";
    private static final String ACCOUNT_REGEXP = "[1-9]\\d{3}";

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
            } else if (paramLogin.matches(ACCOUNT_REGEXP)) {
                user = userService.loginAccount(Integer.parseInt(paramLogin),
                                                    paramPassword);
            }
            if (user.isPresent()) {
                User sessionUser = user.get();
                request.getSession().setAttribute("user", sessionUser);
                request.setAttribute("id", sessionUser.getId());
                if (sessionUser.getRole().equals(User.Role.TENANT)) {
                    pageToGo = String.format(TENANT_PATH,
                            sessionUser.getId());
                } else if (sessionUser.getRole().equals(User.Role.DISPATCHER)) {
                    pageToGo = String.format(DISPATCHER_PATH,
                            sessionUser.getId());
                }
            } else {
                request.setAttribute("message", "Incorrect email or password");
                pageToGo = LOGIN_JSP;
            }
        }
        return pageToGo;
    }
}


