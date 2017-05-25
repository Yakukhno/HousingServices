package ua.training.controller.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static ua.training.controller.Attributes.LOCALE;
import static ua.training.controller.NewRoutes.REDIRECT;

@Controller
@RequestMapping("/web")
public class LocaleController {

    @PostMapping("/locale")
    public String setLocale(@RequestParam String uri,
                            @RequestParam String locale,
                            HttpServletRequest request) {
        request.getSession().setAttribute(LOCALE, new Locale(locale));
        return REDIRECT + uri;
    }
}
