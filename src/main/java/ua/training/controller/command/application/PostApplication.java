package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class PostApplication implements Command {

    private static final String PARAM_USER = "user";
    private static final String PARAM_TYPE_OF_WORK = "typeOfWork";
    private static final String PARAM_PROBLEM_SCALE = "problemScale";
    private static final String PARAM_DATE_TIME = "dateTime";

    private static final String ADD_APPLICATION_PATH = "/rest/add_application";
    private static final String TENANT_APPLICATIONS_PATH =
            "/rest/user/%s/application";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = ADD_APPLICATION_PATH;
        User sessionUser = (User) request.getSession().getAttribute(PARAM_USER);
        String paramTypeOfWork = request.getParameter(PARAM_TYPE_OF_WORK);
        String paramProblemScale = request.getParameter(PARAM_PROBLEM_SCALE);
        String paramDateTime = request.getParameter(PARAM_DATE_TIME);
        if ((sessionUser != null) && (paramTypeOfWork != null)
                && (paramProblemScale != null) && (paramDateTime != null)) {
            int typeOfWorkId = Integer.parseInt(paramTypeOfWork);
            ProblemScale problemScale = ProblemScale.valueOf(paramProblemScale);
            LocalDateTime localDateTime = getLocalDateTime(paramDateTime);
            applicationService.createNewApplication(sessionUser.getId(),
                    typeOfWorkId, problemScale, localDateTime);

            pageToGo = String.format(TENANT_APPLICATIONS_PATH,
                    sessionUser.getId());
        }
        return pageToGo;
    }

    private LocalDateTime getLocalDateTime(String paramDateTime) {
        return paramDateTime.isEmpty()
                ? null
                : LocalDateTime.parse(paramDateTime);
    }
}
