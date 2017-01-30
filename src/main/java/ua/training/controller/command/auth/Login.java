package ua.training.controller.command.auth;

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
import static ua.training.controller.Attributes.USER;

public class Login implements Command {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";

    private static final String USER_PATH = "/rest/user/%d";
    private static final String LOGIN_PATH = "/rest/login";
    private static final String LOGIN_JSP = "/WEB-INF/view/login.jsp";

    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = LOGIN_PATH;
        String paramLogin = request.getParameter(PARAM_LOGIN);
        String paramPassword = request.getParameter(PARAM_PASSWORD);
        if ((paramLogin != null) && (paramPassword != null)
                && paramLogin.matches(EMAIL_REGEXP)) {
            try {
                User user = userService.loginEmail(paramLogin, paramPassword);
                request.getSession().setAttribute(USER, user);
                pageToGo = String.format(USER_PATH, user.getId());
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e);
            }
        }
        return pageToGo;
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e) {
        if (e.isUserMessage()) {
            request.setAttribute(MESSAGE, e.getUserMessage());
            return LOGIN_JSP;
        } else {
            throw e;
        }
    }
}


