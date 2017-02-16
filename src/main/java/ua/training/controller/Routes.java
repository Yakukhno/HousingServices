package ua.training.controller;

public class Routes {
    private static final String GET = "GET:/";
    private static final String POST = "POST:/";

    public static final String HOME = GET;
    public static final String GET_HOME = GET + "home";
    public static final String GET_USER = GET + "user/";
    public static final String POST_USER = POST + "user";
    public static final String UPDATE_USER = POST + "user/";
    public static final String GET_USER_APPLICATION = GET + "user/application";
    public static final String GET_APPLICATIONS = GET + "application";
    public static final String POST_APPLICATION = POST + "application";
    public static final String DELETE_APPLICATION = POST + "application//delete";
    public static final String POST_LOGIN = POST + "login";
    public static final String POST_LOGOUT = POST + "logout";
    public static final String GET_LOGIN_PAGE = GET + "login";
    public static final String GET_NEW_USER_PAGE = GET + "new_user";
    public static final String GET_NEW_APPLICATION_PAGE = GET + "new_application";
    public static final String POST_NEW_TASK_PAGE = POST + "new_task";
    public static final String GET_TASKS = GET + "task";
    public static final String POST_TASK = POST + "task";
    public static final String GET_BRIGADE = GET + "brigade/";
    public static final String POST_LOCALE = POST + "locale";

    public static final String GET_EXCEPTION_PAGE = GET + "exception";
    public static final String POST_EXCEPTION_PAGE = "POST:/exception";
    public static final String GET_ERROR_PAGE = GET + "error";

    private static final String JSP_ROOT = "/WEB-INF/view/";

    public static final String APPLICATIONS_JSP_PATH
            = JSP_ROOT + "application/applications.jsp";
    public static final String TENANT_APPLICATIONS_JSP_PATH
            = JSP_ROOT + "application/tenant_applications.jsp";
    public static final String ADD_APPLICATION_JSP_PATH
            = JSP_ROOT + "application/new_application.jsp";
    public static final String LOGIN_JSP_PATH = JSP_ROOT + "auth/login.jsp";
    public static final String BRIGADE_JSP_PATH
            = JSP_ROOT + "brigade/brigade.jsp";
    public static final String ERROR_JSP_PATH = JSP_ROOT + "error/error.jsp";
    public static final String EXCEPTION_JSP_PATH
            = JSP_ROOT + "error/exception.jsp";
    public static final String TASKS_JSP_PATH = JSP_ROOT + "task/tasks.jsp";
    public static final String ADD_TASK_JSP_PATH
            = JSP_ROOT + "task/new_task.jsp";
    public static final String USER_JSP_PATH = JSP_ROOT + "user/user.jsp";
    public static final String REGISTER_USER_JSP_PATH
            = JSP_ROOT + "user/new_user.jsp";
}
