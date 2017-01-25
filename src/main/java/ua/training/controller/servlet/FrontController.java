package ua.training.controller.servlet;

import ua.training.controller.command.*;
import ua.training.model.entities.person.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ua.training.controller.Routes.*;

public class FrontController extends HttpServlet {

    private final Map<String, Command> commands = new HashMap<>();

    private static boolean isInit = false;

    @Override
    public void init() throws ServletException {
        commands.put(HOME, new HomePage());
        commands.put(GET_TENANTS, new GetTenants());
        commands.put(GET_TENANT, new GetTenant());
        commands.put(POST_TENANT, new PostTenant());
        commands.put(UPDATE_TENANT, new UpdateTenant());
        commands.put(GET_DISPATCHER, new GetDispatcher());
        commands.put(POST_DISPATCHER, new PostDispatcher());
        commands.put(UPDATE_DISPATCHER, new UpdateDispatcher());
        commands.put(GET_TENANT_APPLICATION, new GetTenantApplications());
        commands.put(GET_APPLICATIONS, new GetApplications());
        commands.put(POST_APPLICATION, new PostApplication());
        commands.put(POST_LOGIN, new Login());
        commands.put(POST_LOGOUT, new Logout());
        commands.put(GET_LOGIN_PAGE, new LoginPage());
        commands.put(GET_REGISTER_TENANT_PAGE, new TenantRegisterPage());
        commands.put(GET_REGISTER_DISPATCHER_PAGE, new DispatcherRegisterPage());
        commands.put(GET_ADD_APPLICATION_PAGE, new AddApplicationPage());
        commands.put(GET_ADD_BRIGADE_PAGE, new AddBrigadePage());
        commands.put(GET_TASKS, new GetTasks());
        commands.put(GET_BRIGADE, new GetBrigade());
        commands.put(POST_BRIGADE, new PostBrigade());
        commands.put(ERROR_PAGE, new ErrorPage());
        super.init();
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
        if (!isInit) {
            ServletContext servletContext = request.getServletContext();
            servletContext.setAttribute("tenant", User.Role.TENANT);
            servletContext.setAttribute("dispatcher", User.Role.DISPATCHER);
            isInit = true;
        }

        String tempCommand = request.getMethod().toUpperCase() + ":"
                + request.getRequestURI().replaceAll(".*/rest", "");
        String command = commands.keySet()
                .stream()
                .filter(tempCommand::matches)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid URL"));
        String jspPath = commands.get(command).execute(request, response);
        if (jspPath.endsWith(".jsp")) {
            request.getRequestDispatcher(jspPath).forward(request, response);
        } else if (!jspPath.equals("error")) {
            response.sendRedirect(jspPath);
        }
    }
}
