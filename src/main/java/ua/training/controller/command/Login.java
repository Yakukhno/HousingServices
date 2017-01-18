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

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";

    private TenantService tenantService = TenantServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/WEB-INF/view/index.jsp";
        String email = request.getParameter(PARAM_EMAIL);
        String password = request.getParameter(PARAM_PASSWORD);
        if (email != null && password != null) {
            Optional<Tenant> tenant = tenantService.login(email, password);
            if (tenant.isPresent()) {
                request.getSession().setAttribute("user", tenant.get());
                pageToGo = "/rest/tenant";
            }
        }
        return pageToGo;
    }
}


