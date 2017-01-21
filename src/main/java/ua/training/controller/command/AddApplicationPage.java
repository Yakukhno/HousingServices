package ua.training.controller.command;

import ua.training.model.entities.ProblemScale;
import ua.training.model.service.impl.TypeOfWorkServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class AddApplicationPage implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("typesOfWork",
                TypeOfWorkServiceImpl.getInstance().getAllTypesOfWork());
        request.setAttribute("problemScales", ProblemScale.values());
        return "/WEB-INF/view/add_application.jsp";
    }
}
