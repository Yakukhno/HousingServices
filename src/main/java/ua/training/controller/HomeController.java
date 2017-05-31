package ua.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static ua.training.controller.util.Views.HOME_VIEW;

@Controller
public class HomeController {

    @GetMapping({"/", "/web/home"})
    public String home() {
        return HOME_VIEW;
    }
}
