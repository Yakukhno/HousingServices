package ua.training.controller.command;

import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Tenant;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.impl.ApplicationServiceImpl;
import ua.training.model.service.impl.TenantServiceImpl;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

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
            "/rest/tenant/%s/application";

    private ApplicationService applicationService
            = ApplicationServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = ADD_APPLICATION_PATH;
        User user = (User) request.getSession().getAttribute(PARAM_USER);
        String paramTypeOfWork = request.getParameter(PARAM_TYPE_OF_WORK);
        String paramProblemScale = request.getParameter(PARAM_PROBLEM_SCALE);
        String paramDateTime = request.getParameter(PARAM_DATE_TIME);
        if ((user != null) && (paramTypeOfWork != null)
                && (paramProblemScale != null) && (paramDateTime != null)) {
            LocalDateTime localDateTime = paramDateTime.isEmpty()
                    ? null
                    : LocalDateTime.parse(paramDateTime);
            Tenant tenant = TenantServiceImpl.getInstance()
                    .getTenantById(user.getId())
                    .orElseThrow(
                            () -> new RuntimeException("Invalid tenant id")
                    );
            TypeOfWork typeOfWork = TypeOfWorkServiceImpl.getInstance()
                    .getTypeOfWorkByDescription(paramTypeOfWork).get(0);

            Application application = new Application.Builder()
                    .setTenant(tenant)
                    .setTypeOfWork(typeOfWork)
                    .setScaleOfProblem(ProblemScale.valueOf(paramProblemScale))
                    .setDesiredTime(localDateTime)
                    .build();
            applicationService.createNewApplication(application);

            pageToGo = String.format(TENANT_APPLICATIONS_PATH,
                    user.getId());
        }
        return pageToGo;
    }
}
