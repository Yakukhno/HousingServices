package ua.training.model.dao.jdbc;

import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTaskDao implements TaskDao {

    private static final String SELECT_ALL =
            "SELECT * FROM task";
    private static final String SELECT_BY_ID = "SELECT * FROM task " +
        "WHERE id_task = ?";
    private static final String SELECT_BY_ACTIVE = "SELECT * FROM task " +
            "WHERE is_active = TRUE";

    private static final String INSERT =
            "INSERT INTO task (id_application, id_brigade, is_active) " +
                    "VALUES (?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM task WHERE id_task = ?";
    private static final String UPDATE =
            "UPDATE task " +
                    "SET id_application = ?, id_brigade = ?, is_active = ? " +
                    "WHERE id_task = ?";

    private static final String TASK_ID = "id_task";
    private static final String IS_ACTIVE = "is_active";

    private Connection connection;

    JdbcTaskDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Task get(int id) {
        Task task = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                task = new Task.Builder()
                        .setId(resultSet.getInt(TASK_ID))
                        .setApplication(new JdbcApplicationDao(connection)
                                .get(resultSet.getInt(JdbcApplicationDao
                                        .APPLICATION_ID)))
                        .setBrigade(new JdbcBrigadeDao(connection)
                                .get(resultSet.getInt(JdbcBrigadeDao
                                        .BRIGADE_ID)))
                        .setActive(resultSet.getBoolean(IS_ACTIVE))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Task task) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromTask(statement, task);
            statement.setInt(4, task.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return tasks;
    }

    private Task getTaskFromResultSet(ResultSet resultSet)
            throws SQLException {
        return new Task.Builder()
                .setId(resultSet.getInt(TASK_ID))
                .setApplication(new JdbcApplicationDao(connection)
                        .get(resultSet.getInt(JdbcApplicationDao
                                .APPLICATION_ID)))
                .setBrigade(new JdbcBrigadeDao(connection)
                        .get(resultSet.getInt(JdbcBrigadeDao
                                .BRIGADE_ID)))
                .setActive(resultSet.getBoolean(IS_ACTIVE))
                .build();
    }

    private void setStatementFromTask(PreparedStatement statement,
                                        Task task)
            throws SQLException {
        statement.setInt(1, task.getApplication().getId());
        statement.setInt(2, task.getBrigade().getId());
        statement.setBoolean(3, task.isActive());
    }
}
