package ua.training.controller.command;

import ua.training.model.entities.person.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GetTenant implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String pageToGo = "/WEB-INF/view/tenant.jsp";
        HttpSession session = request.getSession();
        if ((session != null) && (session.getAttribute("user") != null)) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.getRole().equals(User.Role.DISPATCHER)) {
                pageToGo = "/rest/dispatcher/" + user.getId();
            }
        }
        return pageToGo;
    }
}
