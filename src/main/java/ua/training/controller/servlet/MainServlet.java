package ua.training.controller.servlet;

import ua.training.controller.command.Command;
import ua.training.controller.command.GetTenants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {

    private final Map<String, Command> commmands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        commmands.put("GET:/tenant", new GetTenants());
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
//        String command = request.getMethod().toUpperCase() + ":"
//                + request.getRequestURI().replaceAll("/housing-services", "");
//        String jspPath = commmands.get(command).execute(request, response);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
