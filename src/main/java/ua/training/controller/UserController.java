package ua.training.controller;

import static ua.training.util.AttributeConstants.MESSAGE;
import static ua.training.util.AttributeConstants.PARAMS;
import static ua.training.util.AttributeConstants.USER;
import static ua.training.util.RouteConstants.LOGIN_ROUTE;
import static ua.training.util.RouteConstants.NEW_USER_ROUTE;
import static ua.training.util.RouteConstants.REDIRECT;
import static ua.training.util.RouteConstants.WEB;
import static ua.training.util.ViewConstants.NEW_USER_VIEW;
import static ua.training.util.ViewConstants.USER_VIEW;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.training.controller.validator.EmailValidator;
import ua.training.controller.validator.NameValidator;
import ua.training.controller.validator.PasswordValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

@Controller
@RequestMapping("/web")
public class UserController {

    private static final String USER_WITH_ID_REDIRECT = REDIRECT + WEB + "/user/{userId}";
    private static final String USER_ID = "userId";

    private Validator nameValidator = new NameValidator();
    private Validator emailValidator = new EmailValidator();
    private Validator passwordValidator = new PasswordValidator();

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute(USER, user);
        return USER_VIEW;
    }

    @PostMapping("/user")
    public String addUser(User user) {
        validateUserFieldsForCreate(user);
        userService.createNewUser(user);
        return REDIRECT + LOGIN_ROUTE;
    }

    private void validateUserFieldsForCreate(User user) {
        nameValidator.validate(user.getName());
        emailValidator.validate(user.getEmail());
        passwordValidator.validate(user.getPassword());
    }

    @PostMapping("/user/{userId}")
    public String updateUser(@PathVariable int userId, @RequestParam String email, @RequestParam String oldPassword,
                             @RequestParam String newPassword, HttpServletRequest request, Model model) {
        request.setAttribute(USER_ID, userId);
        model.addAttribute(USER_ID, userId);
        User user = validateUserFieldsForUpdate(userService.getUserById(userId), email, newPassword);
        userService.updateUser(user, oldPassword);
        return USER_WITH_ID_REDIRECT;
    }

    private User validateUserFieldsForUpdate(User user, String email, String password) {
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

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e, RedirectAttributes model,
                                             HttpServletRequest request) {
        if (!e.isUserMessage()) {
            throw e;
        }
        model.addFlashAttribute(MESSAGE, e.getUserMessage());
        List<String> parameters = e.getParameters();
        if (parameters.size() != 0) {
            model.addFlashAttribute(PARAMS, parameters);
        }

        Integer userId = (Integer) request.getAttribute(USER_ID);
        if (userId != null) {
            model.addFlashAttribute(USER, userService.getUserById(userId));
            return USER_WITH_ID_REDIRECT;
        } else {
            return REDIRECT + NEW_USER_ROUTE;
        }
    }

    @GetMapping("/new_user")
    public String getNewUserPage() {
        return NEW_USER_VIEW;
    }
}
