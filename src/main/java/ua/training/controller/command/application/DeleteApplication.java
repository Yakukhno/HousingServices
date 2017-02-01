package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.USER;

public class DeleteApplication implements Command {

    private static final String TENANT_APPLICATIONS_PATH
            = "/rest/user/%d/application";

    private ApplicationService applicationService;

    public DeleteApplication() {
        applicationService = ApplicationServiceImpl.getInstance();
    }

    DeleteApplication(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int userId = ((User) request.getSession().getAttribute(USER)).getId();
        int applicationId = Integer.parseInt(getApplicationIdFromRequest(request));
        applicationService.deleteApplication(applicationId, userId);
        return String.format(TENANT_APPLICATIONS_PATH, userId);
    }

    private String getApplicationIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(0, uri.lastIndexOf("/delete"));
        uri = uri.substring(uri.lastIndexOf('/') + 1);
        return uri;
    }
}
