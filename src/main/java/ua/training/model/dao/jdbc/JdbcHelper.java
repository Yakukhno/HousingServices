package ua.training.model.dao.jdbc;

import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.entities.person.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static ua.training.model.dao.jdbc.JdbcApplicationDao.*;
import static ua.training.model.dao.jdbc.JdbcBrigadeDao.BRIGADE_ID;
import static ua.training.model.dao.jdbc.JdbcTypeOfWorkDao.TYPE_OF_WORK_DESCRIPTION;
import static ua.training.model.dao.jdbc.JdbcTypeOfWorkDao.TYPE_OF_WORK_ID;
import static ua.training.model.dao.jdbc.JdbcUserDao.*;
import static ua.training.model.dao.jdbc.JdbcWorkerDao.WORKER_ID;
import static ua.training.model.dao.jdbc.JdbcWorkerDao.WORKER_NAME;

class JdbcHelper {

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
        return new TypeOfWork.Builder()
                .setId(resultSet.getInt(TYPE_OF_WORK_ID))
                .setDescription(resultSet.getString(TYPE_OF_WORK_DESCRIPTION))
                .build();
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
}
