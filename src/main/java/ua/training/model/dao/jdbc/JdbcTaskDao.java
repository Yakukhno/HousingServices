package ua.training.model.dao.jdbc;

import static ua.training.util.RepositoryConstants.TASK_TABLE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;

public class JdbcTaskDao extends AbstractJdbcDao implements TaskDao {

    private static final String SELECT =
        "SELECT table1.*, type_of_work.description AS worker_type_description FROM " +
            "(SELECT task.id_task, task.scheduled_time, task.is_active, " +
            "application.id_application, application.scale_of_problem, " +
            "application.desired_time, application.status, " +
            "application.address, user.*, type_of_work.*, " +
            "brigade.*, worker.id_worker, " +
            "worker.name AS worker_name, " +
            "worker_has_type_of_work.id_type_of_work AS worker_type_id " +
            "FROM task " +
                "JOIN application USING (id_application) " +
                "LEFT JOIN user USING (id_user) " +
                "LEFT JOIN type_of_work USING (id_type_of_work) " +
                "LEFT JOIN brigade USING (id_brigade) " +
                "LEFT JOIN brigade_has_worker USING (id_brigade) " +
                "LEFT JOIN worker USING (id_worker) " +
                "LEFT JOIN worker_has_type_of_work USING (id_worker)) AS table1 " +
            "LEFT JOIN type_of_work ON table1.worker_type_id = type_of_work.id_type_of_work ";
    private static final String ORDER_BY = "ORDER BY scheduled_time, id_task, id_worker";

    private static final String SELECT_ALL = SELECT + ORDER_BY;
    private static final String SELECT_BY_ACTIVE = SELECT + "WHERE is_active = TRUE " + ORDER_BY;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_task = ? " + ORDER_BY;

    private static final String INSERT = "INSERT INTO task (id_application, id_brigade, scheduled_time, is_active) " +
            "VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM task WHERE id_task = ?";
    private static final String UPDATE = "UPDATE task SET id_application = ?, id_brigade = ?, scheduled_time = ?, "
            + "is_active = ? WHERE id_task = ?";

    private static final String EXCEPTION_GET_BY_ID = "Failed select from 'task' with id = %d";
    private static final String EXCEPTION_GET_BY_ACTIVE = "Failed select from 'task' with is_active = true";
    private static final String EXCEPTION_GET_ALL = "Failed select from 'task'";
    private static final String EXCEPTION_ADD = "Failed insert into 'task' value = %s";
    private static final String EXCEPTION_UPDATE = "Failed update 'task' value = %s";

    JdbcTaskDao(Connection connection) {
        this.connection = connection;
        logger = Logger.getLogger(JdbcTaskDao.class);
    }

    @Override
    public Optional<Task> get(int id) {
        Optional<Task> task = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                task = Optional.of(helper.getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ID, id);
            throw getDaoException(message, e);
        }
        return task;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                tasks.add(helper.getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_ALL, e);
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromTask(statement, task);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                task.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_ADD, task);
            throw getDaoException(message, e);
        }
    }

    @Override
    public void delete(int id) {
        delete(TASK_TABLE, DELETE_BY_ID, id);
    }

    @Override
    public void update(Task task) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setStatementFromTask(statement, task);
            statement.setInt(5, task.getId());
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_UPDATE, task);
            throw getDaoException(message, e);
        }
    }

    @Override
    public List<Task> getActiveTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_BY_ACTIVE)) {
            resultSet.next();
            while (!resultSet.isAfterLast()) {
                tasks.add(helper.getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_BY_ACTIVE, e);
        }
        return tasks;
    }

    private void setStatementFromTask(PreparedStatement statement, Task task) throws SQLException {
        statement.setInt(1, task.getApplication().getId());
        statement.setInt(2, task.getBrigade().getId());
        statement.setTimestamp(3, Timestamp.valueOf(task.getScheduledTime()));
        statement.setBoolean(4, task.isActive());
    }
}
