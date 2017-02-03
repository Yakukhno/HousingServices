package ua.training.controller.command.brigade;

import ua.training.controller.command.Command;
import ua.training.model.entities.Brigade;
import ua.training.model.service.BrigadeService;
import ua.training.model.service.impl.BrigadeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.training.controller.Attributes.BRIGADE;

public class GetBrigade implements Command {

    private static final String BRIGADE_JSP_PATH = "/WEB-INF/view/brigade/brigade.jsp";

    private BrigadeService brigadeService;

    public GetBrigade() {
        brigadeService = BrigadeServiceImpl.getInstance();
    }

    GetBrigade(BrigadeService brigadeService) {
        this.brigadeService = brigadeService;
    }

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        int brigadeId = Integer.parseInt(getBrigadeIdFromRequest(request));
        Brigade brigade = brigadeService.getBrigadeById(brigadeId);
        request.setAttribute(BRIGADE, brigade);
        return BRIGADE_JSP_PATH;
    }

    private String getBrigadeIdFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(uri.lastIndexOf('/') + 1);
        return uri;
    }
}
