package ua.training.controller.command.task;

import ua.training.controller.command.Command;
import ua.training.controller.validator.ValidationException;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
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

import static ua.training.controller.Attributes.MESSAGE;

public class PostTask implements Command {

    private static final String PARAM_MANAGER = "manager";
    private static final String PARAM_APPLICATION = "application";
    private static final String PARAM_WORKER = "workers";
    private static final String PARAM_TIME = "dateTime";

    private static final String ADD_TASK_JSP_PATH
            = "/WEB-INF/view/add_task.jsp";
    private static final String APPLICATIONS_PATH = "/rest/application";

    private Validator validator = new Validator();

    private TaskService taskService = TaskServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = APPLICATIONS_PATH;
        String paramApplication = request.getParameter(PARAM_APPLICATION);
        String paramDateTime = request.getParameter(PARAM_TIME);
        String paramManager = request.getParameter(PARAM_MANAGER);
        String[] paramWorkers = request.getParameterValues(PARAM_WORKER);
        if ((paramApplication != null) && (paramManager != null)
                && (paramDateTime != null)) {
            int applicationId = Integer.parseInt(paramApplication);
            int managerId = Integer.parseInt(paramManager);
            List<Integer> workersIdsList = getWorkersIds(paramWorkers);
            try {
                LocalDateTime dateTime = getLocalDateTime(paramDateTime);
                taskService.createNewTask(applicationId, managerId,
                        workersIdsList, dateTime);
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e);
            }
        }
        return pageToGo;
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
        throw new ValidationException()
                .setUserMessage("Date should be determined");
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e) {
        if (e.isUserMessage()) {
            request.setAttribute(MESSAGE, e.getUserMessage());
            return ADD_TASK_JSP_PATH;
        } else {
            throw e;
        }
    }
}
