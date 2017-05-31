package ua.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ua.training.controller.util.Views.LOGIN_VIEW;

@Controller
@RequestMapping("/web")
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage() {
        return LOGIN_VIEW;
    }
}
