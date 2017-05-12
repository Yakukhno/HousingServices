package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.validator.DateTimeValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.exception.ValidationException;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.person.User;
import ua.training.model.service.TaskService;
import ua.training.model.service.WorkerService;
import ua.training.model.service.impl.TaskServiceImpl;
import ua.training.model.service.impl.WorkerServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.controller.Attributes.*;

@Controller
@RequestMapping("/rest")
public class TaskController {

    private static final String EXCEPTION_INCORRECT_DATE = "exception.date";
    private static final String EXCEPTION_NULL_MANAGER = "exception.manager";

    private static final String APPLICATIONS_PATH = "redirect:/rest/application";

    private Validator dateTimeValidator = new DateTimeValidator();

    private TaskService taskService;
    private WorkerService workerService;

    @Autowired
    public TaskController(TaskService taskService,
                          WorkerService workerService) {
        this.taskService = taskService;
        this.workerService = workerService;
    }

    @GetMapping("/task")
    public String getTasks(Model model) {
        model.addAttribute(TASKS, taskService.getActiveTasks());
        return "/task/tasks";
    }

    @PostMapping("/task")
    public String addTask(@RequestParam("application") int applicationId,
                          @RequestParam(value = "workers", required = false) String[] paramWorkers,
                          @RequestParam(value = "manager", required = false) String paramManager,
                          @RequestParam("dateTime") String paramDateTime,
                          @SessionAttribute User user,
                          Model model) {
        String pageToGo = APPLICATIONS_PATH;
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
            pageToGo = getPageToGo(model, e, applicationId);
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
            dateTimeValidator.validate(paramDateTime);
            return LocalDateTime.parse(paramDateTime);
        }
        throw new ValidationException()
                .setUserMessage(EXCEPTION_INCORRECT_DATE);
    }

    private String getPageToGo(Model model,
                               ApplicationException e,
                               int applicationId) {
        if (e.isUserMessage()) {
            model.addAttribute(APPLICATION, applicationId);
            model.addAttribute(WORKERS, workerService.getAllWorkers());
            model.addAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                model.addAttribute(PARAMS, parameters);
            }
            return "task/new_task";
        } else {
            throw e;
        }
    }

    @PostMapping("/new_task")
    public String getNewTaskPage(@RequestParam("application") int applicationId, Model model) {
        model.addAttribute(APPLICATION, applicationId);
        model.addAttribute(WORKERS, workerService.getAllWorkers());
        return "/task/new_task";
    }
}
