package ua.training.controller.servlet;

import ua.training.controller.command.*;
import ua.training.model.entities.person.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static ua.training.controller.Attributes.DISPATCHER;
import static ua.training.controller.Attributes.TENANT;
import static ua.training.controller.Routes.*;

public class FrontController extends HttpServlet {

    private static final String FORWARD_ROUTE = ".jsp";
    private static final String REDIRECT_ROUTE = "/rest";

    private final Map<String, Command> commands = CommandHolder.commands;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute(TENANT, User.Role.TENANT);
        servletContext.setAttribute(DISPATCHER, User.Role.DISPATCHER);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        String tempCommand = formCommand(request);
        String commandStr = commands.keySet()
                .stream()
                .filter(tempCommand::matches)
                .findFirst()
                .orElse(HOME);
        Command command = commands.get(commandStr);
        String route = command.execute(request, response);
        if (route.endsWith(FORWARD_ROUTE)) {
            request.getRequestDispatcher(route).forward(request, response);
        } else if (route.startsWith(REDIRECT_ROUTE)) {
            response.sendRedirect(route);
        }
    }

    private String formCommand(HttpServletRequest request) {
        return request.getMethod().toUpperCase() + ":"
                + request.getRequestURI().replaceAll(".*/rest", "");
    }



}
