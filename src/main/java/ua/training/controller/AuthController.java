package ua.training.controller;

import static ua.training.controller.util.ViewConstants.LOGIN_VIEW;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage() {
        return LOGIN_VIEW;
    }
}
