package ua.training.controller.command.task;

import ua.training.controller.command.Command;
import ua.training.model.entities.Application;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.WorkerService;
import ua.training.model.service.impl.ApplicationServiceImpl;
import ua.training.model.service.impl.WorkerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.APPLICATION;
import static ua.training.controller.Attributes.WORKERS;

public class AddTaskPage implements Command {

    private static final String APPLICATIONS_PATH = "/rest/applications";
    private static final String ADD_TASK_JSP_PATH
            = "/WEB-INF/view/add_task.jsp";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();
    private WorkerService workerService = WorkerServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String paramApplicationId = request.getParameter(APPLICATION);
        if (paramApplicationId != null) {
            int applicationId = Integer.parseInt(paramApplicationId);
            Application application = applicationService
                    .getApplicationById(applicationId);
            request.setAttribute(APPLICATION, application);
            request.setAttribute(WORKERS, workerService.getAllWorkers());
            return ADD_TASK_JSP_PATH;
        }
        return APPLICATIONS_PATH;
    }
}
