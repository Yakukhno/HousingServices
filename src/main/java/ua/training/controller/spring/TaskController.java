package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.controller.validator.DateTimeValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.exception.ValidationException;
import ua.training.model.dto.TaskDto;
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
    private static final String NEW_TASK_REDIRECT = "redirect:/rest/new_task";

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
                          @RequestParam(
                                  value = "workers",
                                  required = false) String[] paramWorkers,
                          @RequestParam(
                                  value = "manager",
                                  required = false) String paramManager,
                          @RequestParam LocalDateTime dateTime) {
        TaskDto taskDto = new TaskDto.Builder()
                .setApplicationId(applicationId)
                .setManagerId(getManagerId(paramManager))
                .setWorkersIds(getWorkersIds(paramWorkers))
                .setDateTime(dateTime)
                .build();
        taskService.createNewTask(taskDto);
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
        } else {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_NULL_MANAGER);
        }
    }

    @PostMapping("/new_task")
    public String postNewTaskPage(@RequestParam int application,
                                  RedirectAttributes model) {
        model.addFlashAttribute(APPLICATION, application);
        return NEW_TASK_REDIRECT;
    }

    @GetMapping("/new_task")
    public String getNewTaskPage(Model model) {
        model.addAttribute(WORKERS, workerService.getAllWorkers());
        return NEW_TASK_VIEW;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             RedirectAttributes model,
                                             HttpServletRequest request) {
        if (!e.isUserMessage()) {
            throw e;
        }
        model.addFlashAttribute(APPLICATION, request.getParameter(APPLICATION));
        model.addFlashAttribute(MESSAGE, e.getUserMessage());
        List<String> parameters = e.getParameters();
        if (parameters.size() != 0) {
            model.addFlashAttribute(PARAMS, parameters);
        }
        return NEW_TASK_REDIRECT;
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
