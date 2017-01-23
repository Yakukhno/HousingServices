package ua.training.controller.command;

import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostDispatcher implements Command {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASSWORD = "password";

    private static final String REGISTER_DISPATCHER_PATH
            = "/rest/register_dispatcher";
    private static final String LOGIN_PATH = "/rest/login";

    private DispatcherService dispatcherService
            = DispatcherServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = REGISTER_DISPATCHER_PATH;
        String paramName = request.getParameter(PARAM_NAME);
        String paramEmail = request.getParameter(PARAM_EMAIL);
        String paramPassword = request.getParameter(PARAM_PASSWORD);
        if ((paramName != null) && (paramEmail != null)
                && (paramPassword != null)) {
            dispatcherService.createNewDispatcher(new Dispatcher.Builder()
                    .setName(paramName)
                    .setEmail(paramEmail)
                    .setPassword(paramPassword)
                    .build()
            );
            pageToGo = LOGIN_PATH;
        }
        return pageToGo;
    }
}
