package ua.training.controller.servlet;

import ua.training.controller.command.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController extends HttpServlet {

    private final Map<String, Command> commands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        commands.put("GET:/", new MainPage());
        commands.put("GET:/tenant", new GetTenants());
        commands.put("GET:/tenant/", new GetTenant());
        commands.put("POST:/tenant", new PostTenant());
        commands.put("GET:/dispatcher/", new GetDispatcher());
        commands.put("POST:/dispatcher", new PostDispatcher());
        commands.put("GET:/login", new LoginPage());
        commands.put("POST:/login", new Login());
        commands.put("GET:/register_tenant", new TenantRegisterPage());
        commands.put("GET:/register_dispatcher", new DispatcherRegisterPage());
        commands.put("POST:/logout", new Logout());
        commands.put("GET:/task", new GetTasks());
//        commands.put("GET:/brigade", new GetBrigade());
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
        String command = request.getMethod().toUpperCase() + ":"
                + request.getRequestURI().replaceAll(".*/rest", "");
        if (command.substring(command.length() - 1).matches("\\d")) {
            command = command.substring(0, command.lastIndexOf('/') + 1);
        }
        String jspPath = commands.get(command).execute(request, response);
        if (jspPath.endsWith(".jsp")) {
            request.getRequestDispatcher(jspPath).forward(request, response);
        } else {
            response.sendRedirect(jspPath);
        }
    }
}
