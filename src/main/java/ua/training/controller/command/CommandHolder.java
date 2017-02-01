package ua.training.controller.command;

import ua.training.controller.command.application.*;
import ua.training.controller.command.auth.PostLogin;
import ua.training.controller.command.auth.LoginPage;
import ua.training.controller.command.auth.PostLogout;
import ua.training.controller.command.brigade.GetBrigade;
import ua.training.controller.command.error.ErrorPage;
import ua.training.controller.command.task.AddTaskPage;
import ua.training.controller.command.task.GetTasks;
import ua.training.controller.command.task.PostTask;
import ua.training.controller.command.user.GetUser;
import ua.training.controller.command.user.PostUser;
import ua.training.controller.command.user.RegisterUserPage;
import ua.training.controller.command.user.UpdateUser;

import java.util.HashMap;
import java.util.Map;

import static ua.training.controller.Routes.*;

public final class CommandHolder {

    public final Map<String, Command> commands = new HashMap<>();

    {
        commands.put(HOME, new HomePage());
        commands.put(GET_USER, new GetUser());
        commands.put(POST_USER, new PostUser());
        commands.put(UPDATE_USER, new UpdateUser());
        commands.put(GET_USER_APPLICATION, new GetUserApplications());
        commands.put(GET_APPLICATIONS, new GetApplications());
        commands.put(POST_APPLICATION, new PostApplication());
        commands.put(DELETE_APPLICATION, new DeleteApplication());
        commands.put(POST_LOGIN, new PostLogin());
        commands.put(POST_LOGOUT, new PostLogout());
        commands.put(GET_LOGIN_PAGE, new LoginPage());
        commands.put(GET_REGISTER_USER_PAGE, new RegisterUserPage());
        commands.put(GET_ADD_APPLICATION_PAGE, new AddApplicationPage());
        commands.put(POST_ADD_TASK_PAGE, new AddTaskPage());
        commands.put(GET_TASKS, new GetTasks());
        commands.put(POST_TASK, new PostTask());
        commands.put(GET_BRIGADE, new GetBrigade());
        commands.put(POST_LOCALE, new PostLocale());
        commands.put(GET_ERROR_PAGE, new ErrorPage());
        commands.put(POST_ERROR_PAGE, new ErrorPage());
    }
}
