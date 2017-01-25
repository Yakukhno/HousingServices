package ua.training.controller.command;

import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDispatcher implements Command {

    private static final String DISPATCHER_JSP_PATH
            = "/WEB-INF/view/dispatcher.jsp";
    private static final String ERROR = "error";

    private static final String DISPATCHER_URI_REGEXP
            = "(?<=/dispatcher/)[\\d]+";

    private DispatcherService dispatcherService
            = DispatcherServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(DISPATCHER_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());
        if (matcher.find()) {
            int dispatcherId = Integer.parseInt(matcher.group());
            Optional<Dispatcher> dispatcher
                    = dispatcherService.getDispatcherById(dispatcherId);
            if (dispatcher.isPresent()) {
                request.setAttribute("dispatcher", dispatcher.get());
                return DISPATCHER_JSP_PATH;
            } else {
                response.sendError(404,
                        "Dispatcher with id = " + dispatcherId + " not found.");
                return ERROR;
            }
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
