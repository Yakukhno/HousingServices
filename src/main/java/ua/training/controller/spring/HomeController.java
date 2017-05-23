package ua.training.controller.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ua.training.controller.Views.HOME_VIEW;

@Controller
@RequestMapping("/rest")
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return HOME_VIEW;
    }
}
