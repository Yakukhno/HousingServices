package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.service.impl.UserServiceImpl;

import static ua.training.controller.Attributes.MESSAGE;

@Controller
@SessionAttributes("user")
@RequestMapping("/rest")
public class AuthController {

    private static final String USER_PATH = "redirect:/rest/user/%d";
    private static final String LOGIN_PATH = "auth/login";
    private static final String LOGOUT_PATH = "redirect:/rest/login";

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return LOGIN_PATH;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        String pageToGo;
        try {
            User user = userService.loginEmail(email, password);
            model.addAttribute(user);
            pageToGo = String.format(USER_PATH, user.getId());
        } catch (ApplicationException e) {
            pageToGo = getPageToGo(model, e);
        }
        return pageToGo;
    }

    private String getPageToGo(Model model, ApplicationException e) {
        if (e.isUserMessage()) {
            model.addAttribute(MESSAGE, e.getUserMessage());
            return LOGIN_PATH;
        } else {
            throw e;
        }
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute User user, SessionStatus status) {
        status.setComplete();
        return LOGOUT_PATH;
    }
}
