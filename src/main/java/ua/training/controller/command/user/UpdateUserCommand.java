package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.controller.validator.EmailValidator;
import ua.training.controller.validator.PasswordValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.training.controller.Attributes.*;
import static ua.training.controller.Routes.USER_JSP_PATH;

public class UpdateUserCommand implements Command {

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "newPassword";

    private static final String USER_PATH = "/rest/user/%s";

    private Validator emailValidator = new EmailValidator();
    private Validator passwordValidator = new PasswordValidator();

    private UserService userService;

    public UpdateUserCommand() {
        userService = UserServiceImpl.getInstance();
    }

    UpdateUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = (User) request.getSession().getAttribute(USER);
        String pageToGo = String.format(USER_PATH, sessionUser.getId());
        String newEmail = request.getParameter(PARAM_EMAIL);
        String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
        int userId = getUserIdFromRequest(request);
        if (sessionUser.getId() != userId) {
            throw new AccessForbiddenException();
        }
        if ((newEmail != null) && (newPassword != null)
                && (oldPassword != null)) {
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
            emailValidator.validate(email);
            user.setEmail(email);
        }
        if (!password.isEmpty()) {
            passwordValidator.validate(password);
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
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                request.setAttribute(PARAMS, parameters);
            }
            return USER_JSP_PATH;
        } else {
            throw e;
        }
    }
}
