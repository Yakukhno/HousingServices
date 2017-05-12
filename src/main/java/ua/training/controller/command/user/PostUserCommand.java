package ua.training.controller.command.user;

import ua.training.controller.command.Command;
import ua.training.controller.validator.EmailValidator;
import ua.training.controller.validator.NameValidator;
import ua.training.controller.validator.PasswordValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.training.controller.Attributes.MESSAGE;
import static ua.training.controller.Attributes.PARAMS;
import static ua.training.controller.Routes.REGISTER_USER_JSP_PATH;

public class PostUserCommand implements Command {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_ROLE = "role";

    private static final String REGISTER_USER_PATH = "/rest/new_user";
    private static final String LOGIN_PATH = "/rest/login";

    private Validator nameValidator = new NameValidator();
    private Validator emailValidator = new EmailValidator();
    private Validator passwordValidator = new PasswordValidator();

    private UserService userService;

//    public PostUserCommand() {
//        userService = UserServiceImpl.getInstance();
//    }

    PostUserCommand(UserService userService) {
        this.userService = userService;
    }

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
                validateUserFields(name, email, password);
                userService.createNewUser(new User.Builder()
                        .setName(name)
                        .setEmail(email)
                        .setPassword(password)
                        .setRole(User.Role.valueOf(role))
                        .build()
                );
                pageToGo = LOGIN_PATH;
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e);
            }
        }
        return pageToGo;
    }

    private void validateUserFields(String name, String email, String password) {
        nameValidator.validate(name);
        emailValidator.validate(email);
        passwordValidator.validate(password);
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e) {
        if (e.isUserMessage()) {
            request.setAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                request.setAttribute(PARAMS, parameters);
            }
            return REGISTER_USER_JSP_PATH;
        } else {
            throw e;
        }
    }
}