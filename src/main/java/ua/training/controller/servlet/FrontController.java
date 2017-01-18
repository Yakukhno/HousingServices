package ua.training.controller.servlet;

import ua.training.controller.command.Command;
import ua.training.controller.command.GetTenants;
import ua.training.controller.command.Login;
import ua.training.controller.command.MainPage;

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
        commands.put("POST:/login", new Login());
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
        String jspPath = commands.get(command).execute(request, response);
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
}
