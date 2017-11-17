package ua.training.util;

public class RouteConstants {
    public static final String REDIRECT = "redirect:";

    public static final String WEB = "/web";

    public static final String ROOT_ROUTE = "/";
    public static final String HOME_ROUTE = WEB + "/home";

    public static final String LOCALE_ROUTE = WEB + "/locale";

    public static final String LOGIN_ROUTE = WEB + "/login";
    public static final String LOGOUT_ROUTE = WEB + "/logout";

    public static final String USER_ROUTE = WEB + "/user";
    public static final String USER_WITH_ID_ROUTE = WEB + "/user/\\d+";
    public static final String NEW_USER_ROUTE = WEB + "/new_user";

    public static final String APPLICATION_ROUTE = WEB + "/application";
    public static final String USER_APPLICATIONS_ROUTE = WEB + "/user/application";
    public static final String NEW_APPLICATION_ROUTE = WEB + "/new_application";
    public static final String DELETE_APPLICATION_ROUTE = WEB + "/application/\\d+/delete";

    public static final String TASK_ROUTE = WEB + "/task";
    public static final String NEW_TASK_ROUTE = WEB + "/new_task";

    public static final String BRIGADE_WITH_ID_ROUTE = WEB + "/brigade/\\d+";

    public static final String WORKER_ROUTE = WEB + "/worker";
    public static final String NEW_WORKER_ROUTE = WEB + "/new_worker";
}
