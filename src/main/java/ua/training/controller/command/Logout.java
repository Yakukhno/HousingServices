package ua.training.controller.command;

import ua.training.model.entities.person.User;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Logout implements Command {

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user.getRole().equals(User.Role.DISPATCHER)) {
                DispatcherService dispatcherService = DispatcherServiceImpl.getInstance();
                dispatcherService.setOffline(user.getId());
            }
            session.invalidate();
        }
        return "/rest/login";
    }
}
