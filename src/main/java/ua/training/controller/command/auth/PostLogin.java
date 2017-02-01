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

public class PostLogin implements Command {

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";

    private static final String USER_PATH = "/rest/user/%d";
    private static final String LOGIN_PATH = "/rest/login";
    private static final String LOGIN_JSP = "/WEB-INF/view/login.jsp";

    private UserService userService;

    public PostLogin() {
        userService = UserServiceImpl.getInstance();
    }

    PostLogin(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = LOGIN_PATH;
        String paramLogin = request.getParameter(PARAM_EMAIL);
        String paramPassword = request.getParameter(PARAM_PASSWORD);
        if ((paramLogin != null) && (paramPassword != null)) {
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


