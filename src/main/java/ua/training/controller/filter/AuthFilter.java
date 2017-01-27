package ua.training.controller.filter;

import ua.training.model.entities.person.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ua.training.controller.Routes.*;

public class AuthFilter implements Filter {

    private static final String RESOURCE_NOT_FOUND = "Resource not found!";
    private static final String ATTR_USER = "user";

    private Set<String> guestAllowedRoutes = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        guestAllowedRoutes.add(GET_TASKS);
        guestAllowedRoutes.add(GET_LOGIN_PAGE);
        guestAllowedRoutes.add(POST_LOGIN);
        guestAllowedRoutes.add(GET_REGISTER_USER_PAGE);
        guestAllowedRoutes.add(POST_USER);
        guestAllowedRoutes.add(GET_BRIGADE);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String uri = req.getMethod().toUpperCase() + ":"
                + req.getRequestURI().replaceAll("(.*/rest)|(\\d+)", "");
        User user = ((User) session.getAttribute(ATTR_USER));
        if (user != null) {
            filterUser(user, uri, req, resp, chain);
        } else {
            filterGuest(uri, req, resp, chain);
        }
    }

    @Override
    public void destroy() {}

    private void filterUser(User user, String uri, HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws ServletException, IOException {
        switch (user.getRole()) {
            case TENANT:
                filterTenant(uri, request, response, chain);
                break;
            case DISPATCHER:
                filterDispatcher(uri, request, response, chain);
                break;
            default:
                throw new IllegalArgumentException("Unknown enum component");
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

    private void filterTenant(String uri, HttpServletRequest request,
                              HttpServletResponse response,
                              FilterChain chain)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        int userId = user.getId();
        if ((uri.equals(String.format(GET_USER_APPLICATIONS_WITH_ID, userId)))
                || (uri.equals(POST_APPLICATION))
                || (uri.equals(POST_LOGOUT))
                || (uri.equals(GET_ADD_APPLICATION_PAGE))
                || (uri.equals(GET_BRIGADE))
                || (uri.equals(String.format(GET_USER_WITH_ID, userId)))
                || (uri.equals(String.format(UPDATE_USER_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else if ((uri.equals(GET_USER))
                || uri.equals(UPDATE_USER)) {
            response.sendRedirect("/rest/user/" + userId);
        } else if ((uri.equals(GET_USER_APPLICATIONS_WITH_ID))){
            response.sendRedirect("/rest/user/" + userId + "/application");
        } else {
            response.sendRedirect("/rest/user/" + userId);
        }
    }

    private void filterDispatcher(String uri, HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        int userId = user.getId();
        if ((uri.equals(POST_LOGOUT))
                || (uri.equals(GET_APPLICATIONS))
                || (uri.equals(GET_ADD_TASK_PAGE))
                || (uri.equals(GET_BRIGADE))
                || (uri.equals(POST_TASK))
                || (uri.equals(String.format(GET_USER_WITH_ID, userId)))
                || (uri.equals(String.format(UPDATE_USER_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else if ((uri.equals(GET_USER)
                || (uri.equals(UPDATE_USER)))) {
            response.sendRedirect("/rest/user/" + user.getId());
        } else {
            response.sendRedirect("/rest/user/" + user.getId());
        }
    }
}
