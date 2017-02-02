package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.model.entities.Application;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.*;

public class GetUserApplications implements Command {

    private static final String TENANT_APPLICATIONS_JSP_PATH
            = "/WEB-INF/view/tenant_applications.jsp";

    private ApplicationService applicationService;

    public GetUserApplications() {
        applicationService = ApplicationServiceImpl.getInstance();
    }

    GetUserApplications(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int userId = ((User) request.getSession().getAttribute(USER)).getId();
        request.setAttribute(APPLICATIONS,
                applicationService.getApplicationsByUserId(userId));
        request.setAttribute(STATUS_NEW, Application.Status.NEW);
        return TENANT_APPLICATIONS_JSP_PATH;
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(0, uri.lastIndexOf('/'));
        uri = uri.substring(uri.lastIndexOf('/') + 1);
        return uri;
    }
}
