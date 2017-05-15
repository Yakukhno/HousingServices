package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import static ua.training.controller.Attributes.MESSAGE;

@Controller
@SessionAttributes("user")
@RequestMapping("/rest")
public class AuthController {

    private static final String LOGIN_VIEW = "auth/login";
    private static final String USER_REDIRECT = "redirect:/rest/user/{userId}";
    private static final String LOGIN_REDIRECT = "redirect:/rest/login";

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return LOGIN_VIEW;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        User user = userService.loginEmail(email, password);
        model.addAttribute(user);
        model.addAttribute("userId", user.getId());
        return USER_REDIRECT;
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute User user, SessionStatus status) {
        status.setComplete();
        return LOGIN_REDIRECT;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             RedirectAttributes model) {
        if (!e.isUserMessage()) {
            throw e;
        }
        model.addFlashAttribute(MESSAGE, e.getUserMessage());
        return LOGIN_REDIRECT;
    }
}
