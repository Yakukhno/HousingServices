package ua.training.controller.command;

import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostTenant implements Command {

    private static final String PARAM_ACCOUNT = "account";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";

    private static final String REGISTER_TENANT_PATH = "/rest/register_tenant";
    private static final String LOGIN_PATH = "/rest/login";

    private TenantService tenantService = TenantServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = REGISTER_TENANT_PATH;
        String name = request.getParameter(PARAM_NAME);
        String email = request.getParameter(PARAM_EMAIL);
        String password = request.getParameter(PARAM_PASSWORD);
        if ((request.getParameter(PARAM_ACCOUNT) != null)
                && (name != null)
                && (email != null)
                && (password != null)) {
            int account = Integer.parseInt(request.getParameter(PARAM_ACCOUNT));
            tenantService.createNewTenant(new Tenant.Builder()
                    .setAccount(account)
                    .setName(name)
                    .setEmail(email)
                    .setPassword(password)
                    .build()
            );
            pageToGo = LOGIN_PATH;
        }
        return pageToGo;
    }
}
