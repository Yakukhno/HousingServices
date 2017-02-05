package ua.training.controller.command.task;

import ua.training.controller.command.Command;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.exception.ValidationException;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.person.User;
import ua.training.model.service.TaskService;
import ua.training.model.service.WorkerService;
import ua.training.model.service.impl.TaskServiceImpl;
import ua.training.model.service.impl.WorkerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.controller.Attributes.*;

public class PostTaskCommand implements Command {

    private static final String PARAM_MANAGER = "manager";
    private static final String PARAM_APPLICATION = "application";
    private static final String PARAM_WORKERS = "workers";
    private static final String PARAM_TIME = "dateTime";

    private static final String EXCEPTION_INCORRECT_DATE = "exception.date";
    private static final String EXCEPTION_NULL_MANAGER = "exception.manager";

    private static final String ADD_TASK_JSP_PATH
            = "/WEB-INF/view/task/new_task.jsp";
    private static final String APPLICATIONS_PATH = "/rest/application";

    private Validator validator = new Validator();

    private TaskService taskService;
    private WorkerService workerService;

    public PostTaskCommand() {
        taskService = TaskServiceImpl.getInstance();
        workerService = WorkerServiceImpl.getInstance();
    }

    PostTaskCommand(TaskService taskService, WorkerService workerService) {
        this.taskService = taskService;
        this.workerService = workerService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(USER);
        String pageToGo = APPLICATIONS_PATH;
        String paramApplication = request.getParameter(PARAM_APPLICATION);
        String paramDateTime = request.getParameter(PARAM_TIME);
        String paramManager = request.getParameter(PARAM_MANAGER);
        String[] paramWorkers = request.getParameterValues(PARAM_WORKERS);
        if ((paramApplication != null) && (paramDateTime != null)) {
            int applicationId = Integer.parseInt(paramApplication);
            List<Integer> workersIdsList = getWorkersIds(paramWorkers);
            try {
                int managerId = getManagerId(paramManager);
                LocalDateTime dateTime = getLocalDateTime(paramDateTime);
                TaskDto taskDto = new TaskDto.Builder()
                        .setApplicationId(applicationId)
                        .setManagerId(managerId)
                        .setWorkersIds(workersIdsList)
                        .setDateTime(dateTime)
                        .build();
                taskService.createNewTask(taskDto, user.getRole());
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e, applicationId);
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

    private int getManagerId(String managerId) {
        if (managerId != null) {
            return Integer.parseInt(managerId);
        }
        throw new ValidationException()
                .setUserMessage(EXCEPTION_NULL_MANAGER);
    }

    private LocalDateTime getLocalDateTime(String paramDateTime) {
        if (!paramDateTime.isEmpty()) {
            validator.validateDateTime(paramDateTime);
            return LocalDateTime.parse(paramDateTime);
        }
        throw new ValidationException()
                .setUserMessage(EXCEPTION_INCORRECT_DATE);
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e,
                               int applicationId) {
        if (e.isUserMessage()) {
            request.setAttribute(APPLICATION, applicationId);
            request.setAttribute(WORKERS, workerService.getAllWorkers());
            request.setAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                request.setAttribute(PARAMS, parameters);
            }
            return ADD_TASK_JSP_PATH;
        } else {
            throw e;
        }
    }
}
