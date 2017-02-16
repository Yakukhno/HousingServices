package ua.training.controller.command.application;

import ua.training.controller.command.Command;
import ua.training.model.entities.ProblemScale;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.PROBLEM_SCALE;
import static ua.training.controller.Attributes.TYPE_OF_WORK;
import static ua.training.controller.Routes.ADD_APPLICATION_JSP_PATH;

public class NewApplicationPageCommand implements Command {

    private TypeOfWorkService typeOfWorkService;

    public NewApplicationPageCommand() {
        typeOfWorkService = TypeOfWorkServiceImpl.getInstance();
    }

    NewApplicationPageCommand(TypeOfWorkService typeOfWorkService) {
        this.typeOfWorkService = typeOfWorkService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(TYPE_OF_WORK,
                typeOfWorkService.getAllTypesOfWork());
        request.setAttribute(PROBLEM_SCALE, ProblemScale.values());
        return ADD_APPLICATION_JSP_PATH;
    }
}
