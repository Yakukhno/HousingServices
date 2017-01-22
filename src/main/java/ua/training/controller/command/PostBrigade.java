package ua.training.controller.command;

import ua.training.model.service.TaskService;
import ua.training.model.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostBrigade implements Command {

    private static final String MANAGER = "manager";
    private static final String APPLICATION = "application";
    private static final String WORKER = "worker";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/rest/application";
        String applicationIdParam = request.getParameter(APPLICATION);
        String managerIdParam = request.getParameter(MANAGER);
        String[] workersIds = request.getParameterValues(WORKER);
        if ((managerIdParam != null) && (workersIds != null)) {
            TaskService taskService = TaskServiceImpl.getInstance();

            int applicationId = Integer.parseInt(applicationIdParam);
            int managerId = Integer.parseInt(managerIdParam);
            List<Integer> workersIdsList = new ArrayList<>();
            for (String string : workersIds) {
                workersIdsList.add(Integer.parseInt(string));
            }

            taskService.createNewTask(applicationId, managerId, workersIdsList);

            pageToGo = "/rest/application";
        }
        return pageToGo;
    }
}
