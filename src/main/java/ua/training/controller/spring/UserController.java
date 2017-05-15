package ua.training.controller.spring;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.controller.validator.EmailValidator;
import ua.training.controller.validator.NameValidator;
import ua.training.controller.validator.PasswordValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ua.training.controller.Attributes.*;

@Controller
@RequestMapping("/rest")
public class UserController {

    private static final String USER_VIEW = "user/user";
    private static final String NEW_USER_VIEW = "user/new_user";
    private static final String NEW_USER_REDIRECT = "redirect:/rest/new_user";
    private static final String LOGIN_REDIRECT = "redirect:/rest/login";
    private static final String USER_REDIRECT = "redirect:/rest/user/{userId}";
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
    public String getUser(@SessionAttribute("user") User sessionUser,
                          @PathVariable int userId,
                          Model model) {
        checkUserAccess(userId, sessionUser);
        User user = userService.getUserById(userId);
        model.addAttribute(USER, user);
        return USER_VIEW;
    }

    @PostMapping("/user")
    public String addUser(User user) {
        validateUserFieldsForCreate(user);
        userService.createNewUser(user);
        return LOGIN_REDIRECT;
    }

    private void validateUserFieldsForCreate(User user) {
        nameValidator.validate(user.getName());
        emailValidator.validate(user.getEmail());
        passwordValidator.validate(user.getPassword());
    }

    @PostMapping("/user/{userId}")
    public String updateUser(@PathVariable int userId,
                             @RequestParam String email,
                             @RequestParam String oldPassword,
                             @RequestParam String newPassword,
                             @SessionAttribute("user") User sessionUser,
                             HttpServletRequest request, Model model) {
        request.setAttribute(USER_ID, userId);
        model.addAttribute(USER_ID, userId);
        checkUserAccess(userId, sessionUser);
        User user = validateUserFieldsForUpdate(sessionUser, email, newPassword);
        userService.updateUser(user, oldPassword);
        return USER_REDIRECT;
    }

    private void checkUserAccess(int userId, User sessionUser) {
        if (userId != sessionUser.getId()) {
            throw new AccessForbiddenException();
        }
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

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             RedirectAttributes model,
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
            return USER_REDIRECT;
        } else {
            return NEW_USER_REDIRECT;
        }
    }

    @GetMapping("/new_user")
    public String getNewUserPage() {
        return NEW_USER_VIEW;
    }
}
