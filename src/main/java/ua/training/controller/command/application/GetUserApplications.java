package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.APPLICATIONS;

public class GetUserApplications implements Command {

    private static final String TENANT_APPLICATIONS_JSP_PATH
            = "/WEB-INF/view/tenant_applications.jsp";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int tenantId = Integer.parseInt(getUserIdFromRequest(request));
        request.setAttribute(APPLICATIONS,
                applicationService.getApplicationsByUserId(tenantId));
        return TENANT_APPLICATIONS_JSP_PATH;
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(0, uri.lastIndexOf('/'));
        uri = uri.substring(uri.lastIndexOf('/') + 1);
        return uri;
    }
}
