package ua.training.controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static ua.training.controller.Attributes.LOCALE;

public class PostLocale implements Command {

    private static final String PARAM_LOCALE = "locale";
    private static final String PARAM_URI = "uri";

    private static final String HOME_PATH = "/";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = HOME_PATH;
        String paramURI = request.getParameter(PARAM_URI);
        String paramLocale = request.getParameter(PARAM_LOCALE);
        if ((paramLocale != null) && (paramURI != null)) {
            Locale locale = new Locale(paramLocale);
            request.getSession().setAttribute(LOCALE, locale);
            pageToGo = paramURI;
        }
        return pageToGo;
    }
}
