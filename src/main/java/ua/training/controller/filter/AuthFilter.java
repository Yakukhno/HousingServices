package ua.training.controller.filter;

import ua.training.model.entities.person.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.training.controller.Routes.*;

public class AuthFilter implements Filter {

    private static final String RESOURCE_NOT_FOUND = "Resource not found!";
    private static final String ATTR_USER = "user";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String uri = req.getMethod().toUpperCase() + ":"
                + req.getRequestURI().replaceAll(".*/rest", "");
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
        if (user != null) {
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
    }

    private void filterGuest(String uri, HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain chain)
            throws ServletException, IOException {
        if ((uri.equals(GET_TASKS)) || (uri.equals(GET_LOGIN_PAGE))
                || (uri.equals(POST_LOGIN))
                || (uri.equals(GET_REGISTER_TENANT_PAGE))
                || (uri.equals(POST_TENANT))
                || (uri.equals(GET_REGISTER_DISPATCHER_PAGE))
                || (uri.equals(POST_DISPATCHER))
                || (uri.matches(GET_BRIGADE))) {
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
        if ((uri.equals(String.format(GET_TENANT_APPLICATIONS_WITH_ID, userId)))
                || (uri.equals(POST_APPLICATION))
                || (uri.equals(POST_LOGOUT))
                || (uri.equals(GET_ADD_APPLICATION_PAGE))
                || (uri.matches(GET_BRIGADE))
                || (uri.equals(String.format(GET_TENANT_WITH_ID, userId)))
                || (uri.equals(String.format(UPDATE_TENANT_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else if ((uri.matches(GET_TENANT))
                || uri.matches(UPDATE_TENANT)) {
            response.sendRedirect("/rest/tenant/" + userId);
        } else if ((uri.matches(GET_TENANT_APPLICATION))){
            response.sendRedirect("/rest/tenant/" + userId + "/application");
        } else {
            response.sendRedirect("/rest/tenant/" + userId);
        }
    }

    private void filterDispatcher(String uri, HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        int userId = user.getId();
        if ((uri.equals(String.format(UPDATE_DISPATCHER_WITH_ID, userId)))
                || (uri.equals(POST_LOGOUT))
                || (uri.equals(GET_APPLICATIONS))
                || (uri.matches(GET_ADD_BRIGADE_PAGE))
                || (uri.matches(GET_BRIGADE))
                || (uri.equals(POST_BRIGADE))
                || (uri.equals(String.format(GET_DISPATCHER_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else if ((uri.matches(GET_DISPATCHER)
                || (uri.matches(UPDATE_DISPATCHER)))) {
            response.sendRedirect("/rest/dispatcher/" + user.getId());
        } else {
            response.sendRedirect("/rest/dispatcher/" + user.getId());
        }
    }
}
