package ua.training.controller.command;

import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetApplications implements Command {

    private static final String APPLICATIONS_JSP_PATH
            = "/WEB-INF/view/applications.jsp";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("applications",
                applicationService.getAllApplications());
        return APPLICATIONS_JSP_PATH;
    }
}
