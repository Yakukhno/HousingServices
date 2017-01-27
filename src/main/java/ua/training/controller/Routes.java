package ua.training.controller;

public class    Routes {
    public static final String HOME = "GET:/";
    public static final String GET_USER = "^GET:/user/[\\d]+$";
    public static final String POST_USER = "POST:/user";
    public static final String UPDATE_USER = "^POST:/user/[\\d]+$";
    public static final String GET_TENANT_APPLICATION = "^GET:/user/[\\d]+/application$";
    public static final String GET_APPLICATIONS = "GET:/application";
    public static final String POST_APPLICATION = "POST:/application";
    public static final String POST_LOGIN = "POST:/login";
    public static final String POST_LOGOUT = "POST:/logout";
    public static final String GET_LOGIN_PAGE = "GET:/login";
    public static final String GET_REGISTER_USER_PAGE = "GET:/register_user";
    public static final String GET_ADD_APPLICATION_PAGE = "GET:/add_application";
    public static final String GET_ADD_TASK_PAGE = "^GET:/application/[\\d]+/add_task";
    public static final String GET_TASKS = "GET:/task";
    public static final String POST_TASK = "POST:/task";
    public static final String GET_BRIGADE = "^GET:/brigade/[\\d]+$";

    public static final String GET_USER_WITH_ID = "GET:/user/%d";
    public static final String GET_USER_APPLICATIONS_WITH_ID = "GET:/user/%d/application";
    public static final String UPDATE_USER_WITH_ID = "POST:/user/%d";

    public static final String ERROR_PAGE = "GET:/error";
}
