package ua.training.controller.command;

import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostDispatcher implements Command {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/rest/register_dispatcher";
        String name = request.getParameter(NAME);
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);
        if ((name != null) && (email != null) && (password != null)) {
            DispatcherService dispatcherService = DispatcherServiceImpl.getInstance();
            dispatcherService.createNewDispatcher(new Dispatcher.Builder()
                    .setName(name)
                    .setEmail(email)
                    .setPassword(password)
                    .build()
            );
            pageToGo = "/rest/login";
        }
        return pageToGo;
    }
}
