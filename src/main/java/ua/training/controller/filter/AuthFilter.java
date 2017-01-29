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

    private Set<String> guestAllowedRoutes = new HashSet<>();
    private Logger logger = Logger.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        guestAllowedRoutes.add(GET_TASKS);
        guestAllowedRoutes.add(GET_LOGIN_PAGE);
        guestAllowedRoutes.add(POST_LOGIN);
        guestAllowedRoutes.add(GET_REGISTER_USER_PAGE);
        guestAllowedRoutes.add(POST_USER);
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
                + req.getRequestURI().replaceAll(".*/rest", "");
    }


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
                String message = EXCEPTION_UNKNOWN_ENUM_COMPONENT;
                IllegalArgumentException e
                        = new IllegalArgumentException(message);
                logger.error(message, e);
                throw e;
        }
    }

    private void filterGuest(String uri, HttpServletRequest request,
                             HttpServletResponse response,
                             FilterChain chain)
            throws ServletException, IOException {
        if (guestAllowedRoutes.contains(uri) || uri.startsWith(GET_BRIGADE)) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/rest/login");
        }
    }

    private void filterTenant(String uri, HttpServletRequest request,
                              HttpServletResponse response,
                              FilterChain chain)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(USER);
        int userId = user.getId();
        if ((uri.equals(String.format(GET_USER_APPLICATIONS_WITH_ID, userId)))
                || (uri.equals(POST_APPLICATION))
                || (uri.equals(POST_LOGOUT))
                || (uri.equals(GET_ADD_APPLICATION_PAGE))
                || (uri.startsWith(GET_BRIGADE))
                || (uri.equals(String.format(GET_USER_WITH_ID, userId)))
                || (uri.equals(String.format(UPDATE_USER_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/rest/user/" + userId);
        }
    }

    private void filterDispatcher(String uri, HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(USER);
        int userId = user.getId();
        if ((uri.equals(POST_LOGOUT))
                || (uri.equals(GET_APPLICATIONS))
                || (uri.equals(GET_ADD_TASK_PAGE))
                || (uri.startsWith(GET_BRIGADE))
                || (uri.equals(POST_TASK))
                || (uri.equals(String.format(GET_USER_WITH_ID, userId)))
                || (uri.equals(String.format(UPDATE_USER_WITH_ID, userId)))
                || (uri.equals(GET_TASKS))) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect("/rest/user/" + user.getId());
        }
    }
}
