package ua.training.controller.command;

import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetApplications implements Command {

    private static final String TENANT_ID_REGEXP = "(?<=/tenant/)[\\d]+(?=/application)";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(TENANT_ID_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int tenantId = Integer.parseInt(matcher.group());
            ApplicationService applicationService
                    = ApplicationServiceImpl.getInstance();
            request.setAttribute("applications",
                    applicationService.getApplicationsByTenantId(tenantId));
            return "/WEB-INF/view/applications.jsp";
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
