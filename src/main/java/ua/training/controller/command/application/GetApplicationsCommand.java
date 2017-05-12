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
import static ua.training.controller.Routes.APPLICATIONS_JSP_PATH;

public class GetApplicationsCommand implements Command {

    private ApplicationService applicationService;

    public GetApplicationsCommand() {
//        applicationService = ApplicationServiceImpl.getInstance();
    }

    GetApplicationsCommand(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(USER);
        request.setAttribute(STATUS_NEW, Application.Status.NEW);
        request.setAttribute(APPLICATIONS,
                applicationService.getAllApplications(user.getRole()));
        return APPLICATIONS_JSP_PATH;
    }
}
