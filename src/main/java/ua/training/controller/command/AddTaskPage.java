package ua.training.controller.command;

import ua.training.model.service.ApplicationService;
import ua.training.model.service.WorkerService;
import ua.training.model.service.impl.ApplicationServiceImpl;
import ua.training.model.service.impl.WorkerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.training.controller.Attributes.APPLICATION;
import static ua.training.controller.Attributes.WORKERS;

public class AddTaskPage implements Command {

    private static final String ADD_TASK_JSP_PATH
            = "/WEB-INF/view/add_task.jsp";
    private static final String ERROR = "error";
    private static final String RESOURCE_NOT_FOUND = "Resource not found!";

    private static final String APPLICATION_ID_REGEXP
            = "(?<=/application/)[\\d]+(?=/add_task)";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();
    private WorkerService workerService = WorkerServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(APPLICATION_ID_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int applicationId = Integer.parseInt(matcher.group());
            return applicationService.getApplicationById(applicationId)
                    .map(application -> {
                        request.setAttribute(APPLICATION,
                                applicationId);
                        request.setAttribute(WORKERS,
                                workerService.getAllWorkers());
                        return ADD_TASK_JSP_PATH;
                    })
                    .orElse(ERROR);
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
