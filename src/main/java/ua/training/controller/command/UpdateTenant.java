package ua.training.controller.command;

import ua.training.model.entities.person.Tenant;
import ua.training.model.service.TenantService;
import ua.training.model.service.impl.TenantServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateTenant implements Command {

    private TenantService tenantService = TenantServiceImpl.getInstance();
    private static final String TENANT_URI_REGEXP = "(?<=/tenant/)[\\d]+";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(TENANT_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());

        String newEmail = request.getParameter("newEmail");
        String newPassword = request.getParameter("newPassword");
        if (matcher.find() && (newEmail != null) && (newPassword != null)) {
            int tenantId = Integer.parseInt(matcher.group());
            Tenant tenant = tenantService.getTenantById(tenantId)
                    .orElseThrow(
                            () -> new RuntimeException("Invalid tenant id")
                    );

            if (!newEmail.isEmpty()) {
                tenant.setEmail(newEmail);
            }
            if (!newPassword.isEmpty()) {
                tenant.setPassword(newPassword);
            }

            tenantService.updateTenant(tenant);

            request.setAttribute("tenant", tenant);
            return "/rest/tenant/" + tenant.getId();
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
