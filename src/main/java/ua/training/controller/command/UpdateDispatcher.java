package ua.training.controller.command;

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

    private DispatcherService dispatcherService = DispatcherServiceImpl.getInstance();
    private static final String DISPATCHER_URI_REGEXP = "(?<=/dispatcher/)[\\d]+";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        Pattern pattern = Pattern.compile(DISPATCHER_URI_REGEXP);
        Matcher matcher = pattern.matcher(request.getRequestURI());

        String newName = request.getParameter("newName");
        String newEmail = request.getParameter("newEmail");
        String newPassword = request.getParameter("newPassword");
        if (matcher.find() && (newEmail != null) && (newPassword != null)) {
            int dispatcherId = Integer.parseInt(matcher.group());
            Dispatcher dispatcher = dispatcherService
                    .getDispatcherById(dispatcherId)
                    .orElseThrow(
                            () -> new RuntimeException("Invalid tenant id")
                    );

            if (!newName.isEmpty()) {
                dispatcher.setName(newName);
            }
            if (!newEmail.isEmpty()) {
                dispatcher.setEmail(newEmail);
            }
            if (!newPassword.isEmpty()) {
                dispatcher.setPassword(newPassword);
            }

            dispatcherService.updateDispatcher(dispatcher);

            request.setAttribute("dispatcher", dispatcher);
            return "/rest/dispatcher/" + dispatcher.getId();
        } else {
            throw new RuntimeException("Invalid URL");
        }
    }
}
