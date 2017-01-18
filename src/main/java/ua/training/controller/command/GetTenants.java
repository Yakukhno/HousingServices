package ua.training.controller.command;

import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetTenants implements Command {

    private TenantService tenantService = new TenantServiceImpl();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tenants", tenantService.getAllTenants());
        return "tenant_list.jsp";
    }
}
