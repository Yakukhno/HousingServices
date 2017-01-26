package ua.training.controller.command;

import ua.training.model.dao.DaoException;
import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDispatcher implements Command {

    private static final String PARAM_NAME = "newName";
    private static final String PARAM_EMAIL = "newEmail";
    private static final String PARAM_OLD_PASSWORD = "oldPassword";
    private static final String PARAM_NEW_PASSWORD = "newPassword";

    private static final String DISPATCHER_PATH = "/rest/dispatcher/%s";
    private static final String DISPATCHER_JSP_PATH
            = "/WEB-INF/view/dispatcher.jsp";

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

        String pageToGo;
        String newName = request.getParameter(PARAM_NAME);
        String newEmail = request.getParameter(PARAM_EMAIL);
        String oldPassword = request.getParameter(PARAM_OLD_PASSWORD);
        String newPassword = request.getParameter(PARAM_NEW_PASSWORD);
        if (matcher.find() && (newEmail != null) && (newPassword != null)) {
            int dispatcherId = Integer.parseInt(matcher.group());
            Dispatcher dispatcher = getDispatcherWithSetFields(dispatcherId, newName,
                    newEmail, newPassword);

            try {
                dispatcherService.updateDispatcher(dispatcher, oldPassword);
                pageToGo = String.format(DISPATCHER_PATH, dispatcher.getId());
            } catch (DaoException e) {
                dispatcher = dispatcherService.getDispatcherById(dispatcherId)
                        .orElseThrow(() -> e);
                request.setAttribute("dispatcher", dispatcher);
                request.setAttribute("message", e.getMessage());
                pageToGo = DISPATCHER_JSP_PATH;
            }
        } else {
            throw new RuntimeException("Invalid URL");
        }
        return pageToGo;
    }

    private Dispatcher getDispatcherWithSetFields(int dispatcherId,
                                                  String name,
                                                  String email,
                                                  String password) {
        Dispatcher dispatcher = dispatcherService
                .getDispatcherById(dispatcherId)
                .orElseThrow(
                        () -> new DaoException("Invalid dispatcher id")
                );
        if (!name.isEmpty()) {
            dispatcher.setName(name);
        }
        if (!email.isEmpty()) {
            dispatcher.setEmail(email);
        }
        if (!password.isEmpty()) {
            dispatcher.setPassword(password);
        }
        return dispatcher;
    }
}
