package ua.training.controller.command;

import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class Login implements Command {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";

    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";
    private static final String ACCOUNT_REGEXP = "[1-9]\\d{3}";

    private TenantService tenantService = TenantServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/WEB-INF/view/index.jsp";
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        if (login != null && password != null) {
            Optional<Tenant> tenant = Optional.empty();
            if (login.matches(EMAIL_REGEXP)) {
                tenant = tenantService.loginEmail(login, password);
            } else if (login.matches(ACCOUNT_REGEXP)) {
                tenant = tenantService.loginAccount(Integer.parseInt(login),
                                                    password);
            }
            if (tenant.isPresent()) {
                request.getSession().setAttribute("user", tenant.get());
                pageToGo = "/rest/tenant";
            }
        }
        return pageToGo;
    }
}


