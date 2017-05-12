package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.validator.EmailValidator;
import ua.training.controller.validator.NameValidator;
import ua.training.controller.validator.PasswordValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import java.util.List;

import static ua.training.controller.Attributes.*;

@Controller
@RequestMapping("/rest")
public class UserController {

    private static final String LOGIN_PATH = "redirect:/rest/login";

    private static final String USER_PATH = "redirect:/rest/user/%s";

    private Validator nameValidator = new NameValidator();
    private Validator emailValidator = new EmailValidator();
    private Validator passwordValidator = new PasswordValidator();

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public String getUser(@SessionAttribute("user") User sessionUser,
                          @PathVariable int userId,
                          Model model) {
        if (userId == sessionUser.getId()) {
            User user = userService.getUserById(userId);
            model.addAttribute(USER, user);
            return "user/user";
        } else {
            throw new AccessForbiddenException();
        }
    }

    @PostMapping("/user")
    public String addUser(User user, Model model) {
        String pageToGo;
        try {
            validateUserFieldsForCreate(user);
            userService.createNewUser(user);
            pageToGo = LOGIN_PATH;
        } catch (ApplicationException e) {
            pageToGo = getPageToGo(model, e);
        }
        return pageToGo;
    }

    private void validateUserFieldsForCreate(User user) {
        nameValidator.validate(user.getName());
        emailValidator.validate(user.getEmail());
        passwordValidator.validate(user.getPassword());
    }

    private String getPageToGo(Model model, ApplicationException e) {
        if (e.isUserMessage()) {
            model.addAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                model.addAttribute(PARAMS, parameters);
            }
            return "user/new_user";
        } else {
            throw e;
        }
    }

    @PostMapping("/user/{userId}")
    public String updateUser(@PathVariable int userId,
                             @RequestParam String email,
                             @RequestParam String oldPassword,
                             @RequestParam String newPassword,
                             @SessionAttribute("user") User sessionUser,
                             Model model) {
        String pageToGo = String.format(USER_PATH, sessionUser.getId());
        if (sessionUser.getId() != userId) {
            throw new AccessForbiddenException();
        }
        try {
            User user = validateUserFieldsForUpdate(sessionUser, email, newPassword);
            userService.updateUser(user, oldPassword);
        } catch (ApplicationException e) {
            pageToGo = getPageToGo(model, e, userId);
        }
        return pageToGo;
    }

    private User validateUserFieldsForUpdate(User user,
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

    private String getPageToGo(Model model,
                               ApplicationException e,
                               int userId) {
        if (e.isUserMessage()) {
            User user = userService.getUserById(userId);
            model.addAttribute(USER, user);
            model.addAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                model.addAttribute(PARAMS, parameters);
            }
            return "user/user";
        } else {
            throw e;
        }
    }

    @GetMapping("/new_user")
    public String getNewUserPage() {
        return "user/new_user";
    }
}
