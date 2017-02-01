package ua.training.controller.command.task;

import ua.training.controller.command.Command;
import ua.training.model.service.TaskService;
import ua.training.model.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.TASKS;

public class GetTasks implements Command {

    private static final String TASKS_JSP_PATH = "/WEB-INF/view/tasks.jsp";

    private TaskService taskService;

    public GetTasks() {
        taskService = TaskServiceImpl.getInstance();
    }

    GetTasks(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(TASKS, taskService.getActiveTasks());
        return TASKS_JSP_PATH;
    }
}
