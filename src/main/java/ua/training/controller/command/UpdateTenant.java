package ua.training.controller.command;

import ua.training.model.dao.DaoException;
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

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "newPassword";

    private static final String TENANT_PATH = "/rest/tenant/%s";

    private static final String TENANT_URI_REGEXP = "(?<=/tenant/)[\\d]+";
    private static final String DISPATCHER_JSP_PATH
            = "/WEB-INF/view/tenant.jsp";

    private TenantService tenantService = TenantServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(TENANT_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());

        String pageToGo;
        String newEmail = request.getParameter(PARAM_EMAIL);
        String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
        if (matcher.find() && (newEmail != null)
                && (newPassword != null) && (oldPassword != null)) {
            int tenantId = Integer.parseInt(matcher.group());
            Tenant tenant = tenantService.getTenantById(tenantId)
                    .orElseThrow(
                            () -> new DaoException("Invalid tenant id")
                    );

            if (!newEmail.isEmpty()) {
                tenant.setEmail(newEmail);
            }
            if (!newPassword.isEmpty()) {
                tenant.setPassword(newPassword);
            }

            try {
                tenantService.updateTenant(tenant, oldPassword);
                pageToGo = String.format(TENANT_PATH, tenant.getId());
            } catch (DaoException e) {
                tenant = tenantService.getTenantById(tenantId)
                        .orElseThrow(() -> e);
                request.setAttribute("tenant", tenant);
                request.setAttribute("message", e.getMessage());
                pageToGo = DISPATCHER_JSP_PATH;
            }
        } else {
            throw new RuntimeException("Invalid URL");
        }
        return pageToGo;
    }
}
