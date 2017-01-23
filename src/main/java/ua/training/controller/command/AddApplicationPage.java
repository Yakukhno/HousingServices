package ua.training.controller.command;

import ua.training.model.entities.ProblemScale;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddApplicationPage implements Command {

    private static final String ADD_APPLICATION_JSP_PATH
            = "/WEB-INF/view/add_application.jsp";

    private TypeOfWorkService typeOfWorkService
            = TypeOfWorkServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("typesOfWork",
                typeOfWorkService.getAllTypesOfWork());
        request.setAttribute("problemScales", ProblemScale.values());
        return ADD_APPLICATION_JSP_PATH;
    }
}
