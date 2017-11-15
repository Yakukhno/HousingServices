package ua.training.controller;

import static ua.training.controller.util.ViewConstants.HOME_VIEW;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/web/home"})
    public String home() {
        return HOME_VIEW;
    }
}
