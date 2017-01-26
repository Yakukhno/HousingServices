package ua.training.controller.command;

import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetUser implements Command {

    private static final String USER_JSP_PATH = "/WEB-INF/view/user.jsp";
    private static final String ERROR = "error";

    private static final String USER_URI_REGEXP = "(?<=/user/)[\\d]+";

    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(USER_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int userId = Integer.parseInt(matcher.group());
            return userService.getUserById(userId)
                    .map(user -> {
                        request.setAttribute("user", user);
                        return USER_JSP_PATH;
                    })
                    .orElse(ERROR);
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
