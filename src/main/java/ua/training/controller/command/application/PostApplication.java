package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.impl.ApplicationServiceImpl;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static ua.training.controller.Attributes.*;

public class PostApplication implements Command {

    private static final String PARAM_USER = "user";
    private static final String PARAM_TYPE_OF_WORK = "typeOfWork";
    private static final String PARAM_PROBLEM_SCALE = "problemScale";
    private static final String PARAM_DATE_TIME = "dateTime";

    private static final String ADD_APPLICATION_JSP_PATH
            = "/WEB-INF/view/add_application.jsp";
    private static final String ADD_APPLICATION_PATH = "/rest/add_application";
    private static final String TENANT_APPLICATIONS_PATH =
            "/rest/user/%s/application";

    private Validator validator = new Validator();

    private TypeOfWorkService typeOfWorkService;
    private ApplicationService applicationService;

    public PostApplication() {
        typeOfWorkService = TypeOfWorkServiceImpl.getInstance();
        applicationService = ApplicationServiceImpl.getInstance();
    }

    PostApplication(TypeOfWorkService typeOfWorkService,
                    ApplicationService applicationService) {
        this.typeOfWorkService = typeOfWorkService;
        this.applicationService = applicationService;
    }

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
            try {
                int typeOfWorkId = Integer.parseInt(paramTypeOfWork);
                ProblemScale problemScale = ProblemScale.valueOf(paramProblemScale);
                LocalDateTime localDateTime = getLocalDateTime(paramDateTime);
                applicationService.createNewApplication(sessionUser.getId(),
                        typeOfWorkId, problemScale, localDateTime);
                pageToGo = String.format(TENANT_APPLICATIONS_PATH,
                        sessionUser.getId());
            } catch (ApplicationException e) {
                pageToGo = getPageToGo(request, e);
            }
        }
        return pageToGo;
    }

    private LocalDateTime getLocalDateTime(String paramDateTime) {
        if (!paramDateTime.isEmpty()) {
            validator.validateDateTime(paramDateTime);
            return LocalDateTime.parse(paramDateTime);
        }
        return null;
    }

    private String getPageToGo(HttpServletRequest request,
                               ApplicationException e) {
        if (e.isUserMessage()) {
            request.setAttribute(TYPE_OF_WORK,
                    typeOfWorkService.getAllTypesOfWork());
            request.setAttribute(PROBLEM_SCALE, ProblemScale.values());
            request.setAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (e.getParameters().size() != 0) {
                request.setAttribute(PARAMS, parameters);
            }
            return ADD_APPLICATION_JSP_PATH;
        } else {
            throw e;
        }
    }
}
