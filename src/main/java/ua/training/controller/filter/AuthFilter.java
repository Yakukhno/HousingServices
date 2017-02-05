package ua.training.controller.filter;

import org.apache.log4j.Logger;
import ua.training.model.entities.person.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ua.training.controller.Attributes.USER;
import static ua.training.controller.Routes.*;

public class AuthFilter implements Filter {

    private static final String EXCEPTION_UNKNOWN_ENUM_COMPONENT
            = "Unknown component of Role enum";

    private Set<String> allAllowedRoutes = new HashSet<>();
    private Set<String> guestAllowedRoutes = new HashSet<>();
    private Set<String> userAllowedRoutes = new HashSet<>();
    private Set<String> tenantAllowedRoutes = new HashSet<>();
    private Set<String> dispatcherAllowedRoutes = new HashSet<>();
    private Logger logger = Logger.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allAllowedRoutes.add(GET_TASKS);
        allAllowedRoutes.add(POST_LOCALE);
        allAllowedRoutes.add(GET_BRIGADE);
        allAllowedRoutes.add(GET_HOME);

        guestAllowedRoutes.addAll(allAllowedRoutes);
        guestAllowedRoutes.add(GET_LOGIN_PAGE);
        guestAllowedRoutes.add(POST_LOGIN);
        guestAllowedRoutes.add(GET_NEW_USER_PAGE);
        guestAllowedRoutes.add(POST_USER);

        userAllowedRoutes.addAll(allAllowedRoutes);
        userAllowedRoutes.add(GET_USER);
        userAllowedRoutes.add(UPDATE_USER);
        userAllowedRoutes.add(POST_LOGOUT);

        tenantAllowedRoutes.addAll(userAllowedRoutes);
        tenantAllowedRoutes.add(POST_APPLICATION);
        tenantAllowedRoutes.add(GET_USER_APPLICATION);
        tenantAllowedRoutes.add(GET_NEW_APPLICATION_PAGE);
        tenantAllowedRoutes.add(DELETE_APPLICATION);

        dispatcherAllowedRoutes.addAll(userAllowedRoutes);
        dispatcherAllowedRoutes.add(GET_APPLICATIONS);
        dispatcherAllowedRoutes.add(POST_NEW_TASK_PAGE);
        dispatcherAllowedRoutes.add(POST_TASK);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String uri = getUri(req);
        User user = ((User) session.getAttribute(USER));
        if (user != null) {
            filterUser(user, uri, req, resp, chain);
        } else {
            filterGuest(uri, req, resp, chain);
        }
    }
    @Override
    public void destroy() {}

    private String getUri(HttpServletRequest req) {
        return req.getMethod().toUpperCase() + ":"
                + req.getRequestURI().replaceAll("(.*/rest)|(\\d+)", "");
    }


    private void filterUser(User user, String uri,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws ServletException, IOException {
        boolean isAllowed;
        int userId = user.getId();
        switch (user.getRole()) {
            case TENANT:
                isAllowed = tenantAllowedRoutes.contains(uri);
                break;
            case DISPATCHER:
                isAllowed = dispatcherAllowedRoutes.contains(uri);
                break;
            default:
                String message = EXCEPTION_UNKNOWN_ENUM_COMPONENT;
                IllegalArgumentException e
                        = new IllegalArgumentException(message);
                logger.error(message, e);
                throw e;
        }
        if (isAllowed) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/rest/user/" + userId);
        }
    }

    private void filterGuest(String uri, HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain chain)
            throws ServletException, IOException {
        if (guestAllowedRoutes.contains(uri)) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/rest/login");
        }
    }
}
