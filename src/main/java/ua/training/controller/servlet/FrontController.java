package ua.training.controller.servlet;

import org.apache.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.controller.command.CommandHolder;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
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
import static ua.training.controller.Routes.HOME;

public class FrontController extends HttpServlet {

    private static final String URI_REPLACE_REGEXP = "(.*/rest)|(\\d+)";
    private static final String FORWARD_ROUTE = ".jsp";
    private static final String REDIRECT_ROUTE = "/rest";

    private Map<String, Command> commands;
    private Logger logger = Logger.getLogger(FrontController.class);

    @Override
    public void init() throws ServletException {
        super.init();
        commands = new CommandHolder().commands;
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

    void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        String strCommand = formCommand(request);
        Command command = commands.getOrDefault(strCommand, commands.get(HOME));
        try {
            String route = command.execute(request, response);
            if (route.endsWith(FORWARD_ROUTE)) {
                request.getRequestDispatcher(route).forward(request, response);
            } else if (route.startsWith(REDIRECT_ROUTE)) {
                response.sendRedirect(route);
            }
        } catch (ResourceNotFoundException e) {
            response.sendError(404, e.getMessage());
        } catch (AccessForbiddenException e) {
            response.sendError(403, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    String formCommand(HttpServletRequest request) {
        return request.getMethod().toUpperCase() + ":"
                + request.getRequestURI().replaceAll(URI_REPLACE_REGEXP, "");
    }

    void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }
}
