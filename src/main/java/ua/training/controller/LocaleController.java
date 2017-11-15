package ua.training.controller;

import static ua.training.controller.util.AttributeConstants.LOCALE;
import static ua.training.controller.util.RouteConstants.REDIRECT;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class LocaleController {

    @PostMapping("/locale")
    public String setLocale(@RequestParam String uri, @RequestParam String locale, HttpServletRequest request) {
        request.getSession().setAttribute(LOCALE, new Locale(locale));
        return REDIRECT + uri;
    }
}
