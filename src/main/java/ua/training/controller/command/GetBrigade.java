package ua.training.controller.command;

import ua.training.model.service.BrigadeService;
import ua.training.model.service.impl.BrigadeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetBrigade implements Command {

    private static final String BRIGADE_JSP_PATH = "/WEB-INF/view/brigade.jsp";
    private static final String ERROR = "error";

    private static final String BRIGADE_URI_REGEXP = "(?<=/brigade/)[\\d]+";

    private BrigadeService brigadeService = BrigadeServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(BRIGADE_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int brigadeId = Integer.parseInt(matcher.group());
            return brigadeService.getBrigadeById(brigadeId)
                    .map(brigade -> {
                        request.setAttribute("brigade", brigade);
                        return BRIGADE_JSP_PATH;
                    })
                    .orElse(ERROR);
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
