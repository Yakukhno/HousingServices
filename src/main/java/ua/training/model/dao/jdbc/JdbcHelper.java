package ua.training.model.dao.jdbc;

import ua.training.model.entities.*;
import ua.training.model.entities.person.User;
import ua.training.model.entities.person.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static ua.training.model.dao.jdbc.JdbcApplicationDao.*;
import static ua.training.model.dao.jdbc.JdbcBrigadeDao.*;
import static ua.training.model.dao.jdbc.JdbcTaskDao.*;
import static ua.training.model.dao.jdbc.JdbcTypeOfWorkDao.*;
import static ua.training.model.dao.jdbc.JdbcUserDao.*;
import static ua.training.model.dao.jdbc.JdbcWorkerDao.*;

class JdbcHelper {

    private static final String TASK_WORKER_NAME = "worker_name";
    private static final String TASK_WORKER_TYPE_ID = "worker_type_id";
    private static final String TASK_WORKER_TYPE_DESCRIPTION
            = "worker_type_description";

    User getUserFromResultSet(ResultSet resultSet)
            throws SQLException {
        return new User.Builder()
                .setId(resultSet.getInt(USER_ID))
                .setName(resultSet.getString(USER_NAME))
                .setEmail(resultSet.getString(USER_EMAIL))
                .setPassword(resultSet.getString(USER_PASSWORD))
                .setRole(User.Role.valueOf(
                        resultSet.getString(USER_ROLE)
                ))
                .build();
    }

    Application getApplicationFromResultSet(ResultSet resultSet)
            throws SQLException {
        LocalDateTime localDateTime = null;
        if (resultSet.getTimestamp(APPLICATION_DESIRED_TIME) != null) {
            localDateTime = resultSet.getTimestamp(APPLICATION_DESIRED_TIME)
                    .toLocalDateTime();
        }
        return new Application.Builder()
                .setId(resultSet.getInt(APPLICATION_ID))
                .setTenant(getUserFromResultSet(resultSet))
                .setTypeOfWork(getTypeOfWorkFromResultSet(resultSet))
                .setDesiredTime(localDateTime)
                .setScaleOfProblem(ProblemScale.valueOf(
                        resultSet.getString(APPLICATION_SCALE_OF_PROBLEM)
                ))
                .setStatus(Application.Status.valueOf(
                        resultSet.getString(APPLICATION_STATUS)
                ))
                .build();
    }

    TypeOfWork getTypeOfWorkFromResultSet(ResultSet resultSet)
            throws SQLException {
        return getTypeOfWorkFromResultSet(resultSet, TYPE_OF_WORK_ID,
                TYPE_OF_WORK_DESCRIPTION);
    }

    Worker getWorkerFromResultSet(ResultSet resultSet)
            throws SQLException {
        int currentId = resultSet.getInt(WORKER_ID);
        Worker.Builder builder = new Worker.Builder()
                .setId(resultSet.getInt(WORKER_ID))
                .setName(resultSet.getString(WORKER_NAME))
                .addTypeOfWork(getTypeOfWorkFromResultSet(resultSet));
        while (resultSet.next() && resultSet.getInt(WORKER_ID) == currentId) {
            builder.addTypeOfWork(getTypeOfWorkFromResultSet(resultSet));
        }
        return builder.build();
    }

    Brigade getBrigadeFromResultSet(ResultSet resultSet)
            throws SQLException {
        int currentId = resultSet.getInt(BRIGADE_ID);
        Brigade.Builder builder = new Brigade.Builder()
                .setId(currentId);
        resultSet.getInt(WORKER_ID);
        if (!resultSet.wasNull()) {
            builder.addWorker(getWorkerFromResultSet(resultSet));
            while ((!resultSet.isAfterLast())
                    && (resultSet.getInt(BRIGADE_ID) == currentId)) {
                builder.addWorker(getWorkerFromResultSet(resultSet));
            }
        }
        return builder.build();
    }

    Task getTaskFromResultSet(ResultSet resultSet)
            throws SQLException {
        int taskId = resultSet.getInt(TASK_ID);
        boolean isActive = resultSet.getBoolean(TASK_IS_ACTIVE);
        LocalDateTime dateTime = resultSet.getTimestamp(TASK_SCHEDULED_TIME)
                .toLocalDateTime();
        Application application = getApplicationFromResultSet(resultSet);
        Brigade brigade = getTaskBrigadeFromResultSet(resultSet, taskId);

        Task.Builder builderTask = new Task.Builder()
                .setId(taskId)
                .setScheduledTime(dateTime)
                .setApplication(application)
                .setBrigade(brigade)
                .setActive(isActive);
        return builderTask.build();
    }

    private Brigade getTaskBrigadeFromResultSet(ResultSet resultSet,
                                                int taskId)
            throws SQLException {
        int brigadeId = resultSet.getInt(BRIGADE_ID);
        Brigade.Builder builderBrigade = new Brigade.Builder()
                .setId(brigadeId);
        while ((!resultSet.isAfterLast()
                && (resultSet.getInt(TASK_ID) == taskId))) {
            Worker.Builder builderWorker = getTaskWorkerFormResultSet(resultSet);
            builderBrigade.addWorker(builderWorker.build());
        }
        return builderBrigade.build();
    }

    private Worker.Builder getTaskWorkerFormResultSet(ResultSet resultSet)
            throws SQLException {
        int currentId = resultSet.getInt(WORKER_ID);
        Worker.Builder builderWorker = new Worker.Builder()
                .setId(resultSet.getInt(WORKER_ID))
                .setName(resultSet.getString(TASK_WORKER_NAME))
                .addTypeOfWork(getTypeOfWorkFromResultSet(
                        resultSet,
                        TASK_WORKER_TYPE_ID,
                        TASK_WORKER_TYPE_DESCRIPTION)
                );
        while (resultSet.next() && resultSet.getInt(WORKER_ID) == currentId) {
            builderWorker.addTypeOfWork(getTypeOfWorkFromResultSet(
                    resultSet,
                    TASK_WORKER_TYPE_ID,
                    TASK_WORKER_TYPE_DESCRIPTION)
            );
        }
        return builderWorker;
    }

    private TypeOfWork getTypeOfWorkFromResultSet(ResultSet resultSet,
                                                  String typeId,
                                                  String typeDescription)
            throws SQLException {
        return new TypeOfWork.Builder()
                .setId(resultSet.getInt(typeId))
                .setDescription(resultSet.getString(typeDescription))
                .build();
    }
}
