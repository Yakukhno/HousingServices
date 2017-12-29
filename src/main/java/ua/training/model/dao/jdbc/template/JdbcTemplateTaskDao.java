package ua.training.model.dao.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;

@Component("taskDao")
@Transactional
public class JdbcTemplateTaskDao extends AbstractJdbcTemplateDao implements TaskDao {

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

    @Override
    public Optional<Task> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getTaskRowMapper(), id));
    }

    @Override
    public List<Task> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getTaskRowMapper());
    }

    @Override
    public void add(Task task) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setInt(1, task.getApplication().getId());
            statement.setInt(2, task.getBrigade().getId());
            statement.setTimestamp(3, Timestamp.valueOf(task.getScheduledTime()));
            statement.setBoolean(4, task.isActive());
            return statement;
        }, keyHolder);
        task.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(Task task) {
        jdbcTemplate.update(UPDATE, task.getApplication().getId(), task.getBrigade().getId(), task.getScheduledTime(),
                task.isActive(), task.getId());
    }

    @Override
    public List<Task> getActiveTasks() {
        return jdbcTemplate.query(SELECT_BY_ACTIVE, getTaskRowMapper());
    }

    private RowMapper<Task> getTaskRowMapper() {
        return (resultSet, i) -> jdbcHelper.getTaskFromResultSet(resultSet);
    }
}
