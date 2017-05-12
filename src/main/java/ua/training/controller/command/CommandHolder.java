package ua.training.controller.command;

import ua.training.controller.command.application.*;
import ua.training.controller.command.auth.PostLoginCommand;
import ua.training.controller.command.auth.LoginPageCommand;
import ua.training.controller.command.auth.PostLogoutCommand;
import ua.training.controller.command.brigade.GetBrigadeCommand;
import ua.training.controller.command.exception.ErrorPageCommand;
import ua.training.controller.command.exception.ExceptionPageCommand;
import ua.training.controller.command.locale.PostLocaleCommand;
import ua.training.controller.command.task.NewTaskPageCommand;
import ua.training.controller.command.task.GetTasksCommand;
import ua.training.controller.command.task.PostTaskCommand;
import ua.training.controller.command.user.GetUserCommand;
import ua.training.controller.command.user.PostUserCommand;
import ua.training.controller.command.user.NewUserPageCommand;
import ua.training.controller.command.user.UpdateUserCommand;

import java.util.HashMap;
import java.util.Map;

import static ua.training.controller.Routes.*;

public final class CommandHolder {

    public final Map<String, Command> commands = new HashMap<>();

    {
        commands.put(HOME, new HomePage());
        commands.put(GET_HOME, new HomePage());
//        commands.put(GET_USER, new GetUserCommand());
//        commands.put(POST_USER, new PostUserCommand());
//        commands.put(UPDATE_USER, new UpdateUserCommand());
        commands.put(GET_USER_APPLICATION, new GetUserApplicationsCommand());
        commands.put(GET_APPLICATIONS, new GetApplicationsCommand());
        commands.put(POST_APPLICATION, new PostApplicationCommand());
        commands.put(DELETE_APPLICATION, new DeleteApplicationCommand());
//        commands.put(POST_LOGIN, new PostLoginCommand());
        commands.put(POST_LOGOUT, new PostLogoutCommand());
        commands.put(GET_LOGIN_PAGE, new LoginPageCommand());
        commands.put(GET_NEW_USER_PAGE, new NewUserPageCommand());
        commands.put(GET_NEW_APPLICATION_PAGE, new NewApplicationPageCommand());
        commands.put(POST_NEW_TASK_PAGE, new NewTaskPageCommand());
        commands.put(GET_TASKS, new GetTasksCommand());
        commands.put(POST_TASK, new PostTaskCommand());
        commands.put(GET_BRIGADE, new GetBrigadeCommand());
        commands.put(POST_LOCALE, new PostLocaleCommand());
        commands.put(GET_EXCEPTION_PAGE, new ExceptionPageCommand());
        commands.put(POST_EXCEPTION_PAGE, new ExceptionPageCommand());
        commands.put(GET_ERROR_PAGE, new ErrorPageCommand());
    }
}
