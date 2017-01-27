package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.model.dao.DaoException;
import ua.training.model.entities.person.User;
import ua.training.model.service.ServiceException;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.training.controller.Attributes.MESSAGE;
import static ua.training.controller.Attributes.USER;

public class UpdateUser implements Command {

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "newPassword";

    private static final String USER_PATH = "/rest/user/%s";

    private static final String USER_URI_REGEXP = "(?<=/user/)[\\d]+";
    private static final String USER_JSP_PATH = "/WEB-INF/view/user.jsp";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(USER_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());

        String pageToGo;
        String newEmail = request.getParameter(PARAM_EMAIL);
        String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
        if (matcher.find() && (newEmail != null)
                && (newPassword != null) && (oldPassword != null)) {
            int userId = Integer.parseInt(matcher.group());
            User user = getUserWithSetFields(userId, newEmail, newPassword);

            try {
                userService.updateUser(user, oldPassword);
                pageToGo = String.format(USER_PATH, user.getId());
            } catch (DaoException e) {
                user = userService.getUserById(userId)
                        .orElseThrow(() -> e);
                request.setAttribute(USER, user);
                request.setAttribute(MESSAGE, e.getMessage());
                pageToGo = USER_JSP_PATH;
            }
        } else {
            throw new RuntimeException("Invalid URL");
        }
        return pageToGo;
    }

    private User getUserWithSetFields(int userId,
                                      String email,
                                      String password) {
        User user = userService.getUserById(userId)
                .orElseThrow(
                        () -> new ServiceException("Invalid user id")
                );
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        return user;
    }
}
