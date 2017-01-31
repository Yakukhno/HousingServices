package ua.training.controller.command.task;

import ua.training.controller.command.Command;
import ua.training.controller.validator.ValidationException;
import ua.training.controller.validator.Validator;
import ua.training.model.service.TaskService;
import ua.training.model.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostTask implements Command {

    private static final String PARAM_MANAGER = "manager";
    private static final String PARAM_APPLICATION = "application";
    private static final String PARAM_WORKER = "worker";
    private static final String PARAM_TIME = "dateTime";

    private static final String APPLICATIONS_PATH = "/rest/application";

    private Validator validator = new Validator();

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
                && (paramDateTime != null)) {
            int applicationId = Integer.parseInt(paramApplication);
            int managerId = Integer.parseInt(paramManager);
            List<Integer> workersIdsList = getWorkersIds(paramWorkers);
            LocalDateTime dateTime = getLocalDateTime(paramDateTime);
            taskService.createNewTask(applicationId, managerId,
                    workersIdsList, dateTime);
        }
        return APPLICATIONS_PATH;
    }

    private List<Integer> getWorkersIds(String[] paramWorkers) {
        List<Integer> workersIdsList = new ArrayList<>();
        if (paramWorkers != null) {
            workersIdsList = Arrays.stream(paramWorkers)
                    .map(Integer::parseInt).collect(Collectors.toList());
        }
        return workersIdsList;
    }

    private LocalDateTime getLocalDateTime(String paramDateTime) {
        if (!paramDateTime.isEmpty()) {
            validator.validateDateTime(paramDateTime);
            return LocalDateTime.parse(paramDateTime);
        }
        throw new ValidationException("Date should be determined");
    }

}
