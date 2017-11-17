package ua.training.util;

public class RepositoryConstants {
    public static final String USER_TABLE = "user";
    public static final String USER_ID = "id_user";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";

    public static final String WORKER_TABLE = "worker";
    public static final String WORKER_ID = "id_worker";
    public static final String WORKER_NAME = "name";

    public static final String WORKER_HAS_TYPE_OF_WORK_TABLE = "worker_has_type_of_work";

    public static final String TYPE_OF_WORK_TABLE = "type_of_work";
    public static final String TYPE_OF_WORK_ID = "id_type_of_work";
    public static final String TYPE_OF_WORK_DESCRIPTION = "description";

    public static final String APPLICATION_TABLE = "application";
    public static final String APPLICATION_ID = "id_application";
    public static final String APPLICATION_TYPE_OF_WORK_ID = "id_type_of_work";
    public static final String APPLICATION_USER_ID = "id_user";
    public static final String APPLICATION_SCALE_OF_PROBLEM = "scale_of_problem";
    public static final String APPLICATION_DESIRED_TIME = "desired_time";
    public static final String APPLICATION_STATUS = "status";
    public static final String APPLICATION_ADDRESS = "address";

    public static final String BRIGADE_TABLE = "brigade";
    public static final String BRIGADE_ID = "id_brigade";
    public static final String BRIGADE_MANAGER = "manager";

    public static final String BRIGADE_HAS_WORKER_TABLE = "brigade_has_worker";

    public static final String TASK_TABLE = "task";
    public static final String TASK_ID = "id_task";
    public static final String TASK_APPLICATION_ID = "id_application";
    public static final String TASK_BRIGADE_ID = "id_brigade";
    public static final String TASK_SCHEDULED_TIME = "scheduled_time";
    public static final String TASK_IS_ACTIVE = "is_active";
}
