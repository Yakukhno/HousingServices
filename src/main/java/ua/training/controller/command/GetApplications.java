package ua.training.controller.command;

import ua.training.model.entities.Application;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.APPLICATIONS;
import static ua.training.controller.Attributes.STATUS_NEW;

public class GetApplications implements Command {

    private static final String APPLICATIONS_JSP_PATH
            = "/WEB-INF/view/applications.jsp";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(STATUS_NEW, Application.Status.NEW);
        request.setAttribute(APPLICATIONS,
                applicationService.getAllApplications());
        return APPLICATIONS_JSP_PATH;
    }
}
