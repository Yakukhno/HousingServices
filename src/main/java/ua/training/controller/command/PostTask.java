package ua.training.controller.command;

import ua.training.model.service.TaskService;
import ua.training.model.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostTask implements Command {

    private static final String PARAM_MANAGER = "manager";
    private static final String PARAM_APPLICATION = "application";
    private static final String PARAM_WORKER = "worker";
    private static final String PARAM_TIME = "dateTime";

    private static final String APPLICATIONS_PATH = "/rest/application";

    private TaskService taskService = TaskServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String paramApplication = request.getParameter(PARAM_APPLICATION);
        String paramDateTime = request.getParameter(PARAM_TIME);
        String paramManager = request.getParameter(PARAM_MANAGER);
        String[] paramWorkers = request.getParameterValues(PARAM_WORKER);
        if ((paramApplication != null) && (paramManager != null)
                && (paramWorkers != null) && (paramDateTime != null)) {
            int applicationId = Integer.parseInt(paramApplication);
            int managerId = Integer.parseInt(paramManager);
            List<Integer> workersIdsList = Arrays.stream(paramWorkers)
                    .map(Integer::parseInt).collect(Collectors.toList());
            LocalDateTime dateTime = LocalDateTime.parse(paramDateTime);
            taskService.createNewTask(applicationId, managerId,
                    workersIdsList, dateTime);
        }
        return APPLICATIONS_PATH;
    }
}
