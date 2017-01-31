package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.controller.validator.Validator;
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

public class UpdateUser implements Command {

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "newPassword";

    private static final String USER_PATH = "/rest/user/%s";

    private static final String USER_JSP_PATH = "/WEB-INF/view/user.jsp";

    private Validator validator = new Validator();

    private UserService userService;

    public UpdateUser() {
        userService = UserServiceImpl.getInstance();
    }

    UpdateUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String pageToGo = String.format(USER_PATH, sessionUser.getId());
        String newEmail = request.getParameter(PARAM_EMAIL);
        String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
        if ((newEmail != null) && (newPassword != null)
                && (oldPassword != null)) {
            int userId = getUserIdFromRequest(request);
            try {
                User user = setAndValidateFields(sessionUser,
                        newEmail, newPassword);
                userService.updateUser(user, oldPassword);
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e, userId);
            }
        }
        return pageToGo;
    }

    private User setAndValidateFields(User user,
                                      String email,
                                      String password) {
        if (!email.isEmpty()) {
            validator.validateEmail(email);
            user.setEmail(email);
        }
        if (!password.isEmpty()) {
            validator.validatePassword(password);
            user.setPassword(password);
        }
        return user;
    }

    private int getUserIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String userId = uri.substring(uri.lastIndexOf('/') + 1);
        return Integer.parseInt(userId);
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e,
                               int userId) {
        if (e.isUserMessage()) {
            User user = userService.getUserById(userId);
            request.setAttribute(USER, user);
            request.setAttribute(MESSAGE, e.getUserMessage());
            return USER_JSP_PATH;
        } else {
            throw e;
        }
    }
}
