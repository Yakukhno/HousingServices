package ua.training.controller.command;

import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostTenant implements Command {

    private static final String ACCOUNT = "account";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/rest/register_tenant";
        String name = request.getParameter(NAME);
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);
        if ((request.getParameter(ACCOUNT) != null)
                && (name != null)
                && (email != null)
                && (password != null)) {
            int account = Integer.parseInt(request.getParameter(ACCOUNT));
            TenantService tenantService = TenantServiceImpl.getInstance();
            tenantService.createNewTenant(new Tenant.Builder()
                    .setAccount(account)
                    .setName(name)
                    .setEmail(email)
                    .setPassword(password)
                    .build()
            );
            pageToGo = "/rest/login";
        }
        return pageToGo;
    }
}
