package ua.training.controller.command;

import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.person.User;
import ua.training.model.service.impl.ApplicationServiceImpl;
import ua.training.model.service.impl.TenantServiceImpl;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostApplication implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/rest/add_application";
        User user = (User) request.getSession().getAttribute("user");
        String typeOfWork = request.getParameter("typeOfWork");
        String problemScale = request.getParameter("problemScale");
        if ((user != null) && (typeOfWork != null) && (problemScale != null)) {
            Application application = new Application.Builder()
                    .setTenant(
                            TenantServiceImpl.getInstance()
                                    .getTenantById(user.getId())
                                    .orElseThrow(() -> new RuntimeException("Invalid tenant id"))
                    )
                    .setTypeOfWork(
                            TypeOfWorkServiceImpl.getInstance()
                                    .getTypeOfWorkByDescription(typeOfWork).get(0)
                    )
                    .setScaleOfProblem(
                            ProblemScale.valueOf(problemScale)
                    )
                    .build();
            ApplicationServiceImpl.getInstance().createNewApplication(application);
            pageToGo = String.format("/rest/tenant/%s/application", user.getId());
        }
        return pageToGo;
    }
}
