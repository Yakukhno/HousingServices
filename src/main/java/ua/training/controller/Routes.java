package ua.training.controller;

public class Routes {
    public static final String HOME = "GET:/";
    public static final String GET_TENANTS = "GET:/tenant";
    public static final String GET_TENANT = "^GET:/tenant/[\\d]+$";
    public static final String POST_TENANT = "POST:/tenant";
    public static final String UPDATE_TENANT = "^POST:/tenant/[\\d]+$";
    public static final String GET_DISPATCHER = "^GET:/dispatcher/[\\d]+$";
    public static final String POST_DISPATCHER = "POST:/dispatcher";
    public static final String UPDATE_DISPATCHER = "^POST:/dispatcher/[\\d]+$";
    public static final String GET_TENANT_APPLICATION = "^GET:/tenant/[\\d]+/application$";
    public static final String GET_APPLICATIONS = "GET:/application";
    public static final String POST_APPLICATION = "POST:/application";
    public static final String POST_LOGIN = "POST:/login";
    public static final String POST_LOGOUT = "POST:/logout";
    public static final String GET_LOGIN_PAGE = "GET:/login";
    public static final String GET_REGISTER_TENANT_PAGE = "GET:/register_tenant";
    public static final String GET_REGISTER_DISPATCHER_PAGE = "GET:/register_dispatcher";
    public static final String GET_ADD_APPLICATION_PAGE = "GET:/add_application";
    public static final String GET_ADD_BRIGADE_PAGE = "^GET:/application/[\\d]+/add_brigade";
    public static final String GET_TASKS = "GET:/task";
    public static final String GET_BRIGADE = "^GET:/brigade/[\\d]+$";
    public static final String POST_BRIGADE = "POST:/brigade";

    public static final String GET_TENANT_WITH_ID = "GET:/tenant/%d";
    public static final String GET_TENANT_APPLICATIONS_WITH_ID = "GET:/tenant/%d/application";
    public static final String GET_DISPATCHER_WITH_ID = "GET:/dispatcher/%d";

    public static final String UPDATE_TENANT_WITH_ID = "POST:/tenant/%d";
    public static final String UPDATE_DISPATCHER_WITH_ID = "POST:/dispatcher/%d";

    public static final String ERROR_PAGE = "GET:/error";
}
