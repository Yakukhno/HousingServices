package ua.training.controller.command;

import ua.training.model.service.TaskService;
import ua.training.model.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetTasks implements Command {

    private TaskService taskService = TaskServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("tasks", taskService.getAllTasks());
        return "/WEB-INF/view/tasks.jsp";
    }
}
