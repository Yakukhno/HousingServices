package ua.training.model.dao.jdbc;

import ua.training.model.dao.DaoException;
import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTaskDao implements TaskDao {

    private static final String SELECT_ALL =
            "SELECT * FROM task";
    private static final String SELECT_BY_ID = "SELECT * FROM task " +
        "WHERE id_task = ?";
    private static final String SELECT_BY_ACTIVE = "SELECT * FROM task " +
            "WHERE is_active = TRUE";

    private static final String INSERT =
            "INSERT INTO task " +
                    "(id_application, id_brigade, scheduled_time, is_active) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM task WHERE id_task = ?";
    private static final String UPDATE =
            "UPDATE task " +
                    "SET id_application = ?, id_brigade = ?, " +
                    "scheduled_time = ?, is_active = ? " +
                    "WHERE id_task = ?";

    private static final String TASK_ID = "id_task";
    private static final String TASK_SCHEDULED_TIME = "scheduled_time";
    private static final String TASK_IS_ACTIVE = "is_active";

    private Connection connection;

    JdbcTaskDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Task> get(int id) {
        Optional<Task> task = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                task = Optional.of(getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return task;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                tasks.add(getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromTask(statement, task);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                task.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Task task) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromTask(statement, task);
            statement.setInt(5, task.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Task> getActiveTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_BY_ACTIVE)) {
            while (resultSet.next()) {
                tasks.add(getTaskFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return tasks;
    }

    private Task getTaskFromResultSet(ResultSet resultSet)
            throws SQLException {
        return new Task.Builder()
                .setId(resultSet.getInt(TASK_ID))
                .setApplication(new JdbcApplicationDao(connection)
                        .get(resultSet.getInt(JdbcApplicationDao
                                .APPLICATION_ID)).orElse(null))
                .setBrigade(new JdbcBrigadeDao(connection)
                        .get(resultSet.getInt(JdbcBrigadeDao
                                .BRIGADE_ID)).orElse(null))
                .setScheduledTime(resultSet.getTimestamp(TASK_SCHEDULED_TIME)
                        .toLocalDateTime())
                .setActive(resultSet.getBoolean(TASK_IS_ACTIVE))
                .build();
    }

    private void setStatementFromTask(PreparedStatement statement,
                                        Task task)
            throws SQLException {
        statement.setInt(1, task.getApplication().getId());
        statement.setInt(2, task.getBrigade().getId());
        statement.setTimestamp(3, Timestamp.valueOf(task.getScheduledTime()));
        statement.setBoolean(4, task.isActive());
    }
}
