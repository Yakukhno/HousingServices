package ua.training.controller.session;

import ua.training.model.entities.person.User;
import ua.training.model.service.DispatcherService;
import ua.training.model.service.impl.DispatcherServiceImpl;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class DispatcherLogoutSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {}

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        User user = (User) se.getSession().getAttribute("user");
        if (user != null && user.getRole().equals(User.Role.DISPATCHER)) {
            DispatcherService dispatcherService = DispatcherServiceImpl.getInstance();
            dispatcherService.setOffline(user.getId());
        }
    }
}
