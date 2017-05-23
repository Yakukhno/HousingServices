package ua.training.controller;

public class NewRoutes {
    public static final String REDIRECT = "redirect:";

    public static final String REST = "/rest";

    public static final String HOME_ROUTE = REST + "/home";

    public static final String LOCALE_ROUTE = REST + "/locale";

    public static final String LOGIN_ROUTE = REST + "/login";
    public static final String LOGOUT_ROUTE = REST + "/logout";

    public static final String USER_ROUTE = REST + "/user";
    public static final String USER_WITH_ID_ROUTE = REST + "/user/\\d+";
    public static final String NEW_USER_ROUTE = REST + "/new_user";

    public static final String APPLICATION_ROUTE = REST + "/application";
    public static final String USER_APPLICATIONS_ROUTE = REST + "/user/application";
    public static final String NEW_APPLICATION_ROUTE = REST + "/new_application";
    public static final String DELETE_APPLICATION_ROUTE = REST + "/application/\\d+/delete";

    public static final String TASK_ROUTE = REST + "/task";
    public static final String NEW_TASK_ROUTE = REST + "/new_task";

    public static final String BRIGADE_WITH_ID_ROUTE = REST + "/brigade/\\d+";

    public static final String WORKER_ROUTE = REST + "/worker";
    public static final String NEW_WORKER_ROUTE = REST + "/new_worker";
}
