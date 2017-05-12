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
import static ua.training.controller.Routes.TENANT_APPLICATIONS_JSP_PATH;

public class GetUserApplicationsCommand implements Command {

    private ApplicationService applicationService;

    public GetUserApplicationsCommand() {
//        applicationService = ApplicationServiceImpl.getInstance();
    }

    GetUserApplicationsCommand(ApplicationService applicationService) {
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
}
