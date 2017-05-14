package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.training.controller.validator.DateTimeValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.exception.ValidationException;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.person.User;
import ua.training.model.service.TaskService;
import ua.training.model.service.WorkerService;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
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

    private static final String TASKS_VIEW = "/task/tasks";
    private static final String NEW_TASK_VIEW = "/task/new_task";
    private static final String ALL_APPLICATIONS_REDIRECT = "redirect:/rest/application";

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
        return TASKS_VIEW;
    }

    @PostMapping("/task")
    public String addTask(@RequestParam("application") int applicationId,
                          @RequestParam(value = "workers", required = false) String[] paramWorkers,
                          @RequestParam(value = "manager", required = false) String paramManager,
                          @RequestParam LocalDateTime dateTime,
                          @SessionAttribute User user) {
        TaskDto taskDto = new TaskDto.Builder()
                .setApplicationId(applicationId)
                .setManagerId(getManagerId(paramManager))
                .setWorkersIds(getWorkersIds(paramWorkers))
                .setDateTime(dateTime)
                .build();
        taskService.createNewTask(taskDto, user.getRole());
        return ALL_APPLICATIONS_REDIRECT;
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

    @PostMapping("/new_task")
    public String getNewTaskPage(@RequestParam("application") int applicationId, Model model) {
        model.addAttribute(APPLICATION, applicationId);
        model.addAttribute(WORKERS, workerService.getAllWorkers());
        return NEW_TASK_VIEW;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             Model model,
                                             HttpServletRequest request) {
        if (e.isUserMessage()) {
            model.addAttribute(APPLICATION, request.getParameter(APPLICATION));
            model.addAttribute(WORKERS, workerService.getAllWorkers());
            model.addAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                model.addAttribute(PARAMS, parameters);
            }
            return NEW_TASK_VIEW;
        } else {
            throw e;
        }
    }

    @InitBinder
    public void localDateTimeBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String s) throws IllegalArgumentException {
                if (!s.isEmpty()) {
                    dateTimeValidator.validate(s);
                    setValue(LocalDateTime.parse(s));
                } else {
                    throw new ValidationException()
                            .setUserMessage(EXCEPTION_INCORRECT_DATE);
                }
            }
        });
    }
}
