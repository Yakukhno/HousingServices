package ua.training.controller.command;

import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetApplications implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        ApplicationService applicationService
                = ApplicationServiceImpl.getInstance();
        request.setAttribute("applications",
                applicationService.getAllApplications());
        return "/WEB-INF/view/applications.jsp";
    }
}
