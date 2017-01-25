package ua.training.controller.command;

import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetTenant implements Command {

    private static final String TENANT_JSP_PATH = "/WEB-INF/view/tenant.jsp";
    private static final String ERROR = "error";

    private static final String TENANT_URI_REGEXP = "(?<=/tenant/)[\\d]+";

    private TenantService tenantService = TenantServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(TENANT_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int tenantId = Integer.parseInt(matcher.group());
            Optional<Tenant> tenant = tenantService.getTenantById(tenantId);
            if (tenant.isPresent()) {
                request.setAttribute("tenant", tenant.get());
                return TENANT_JSP_PATH;
            } else {
                response.sendError(404,
                        "Tenant with id = " + tenantId + " not found.");
                return ERROR;
            }
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
