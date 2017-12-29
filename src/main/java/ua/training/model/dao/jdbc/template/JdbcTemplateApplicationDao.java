package ua.training.model.dao.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.entities.Application;

@Repository("applicationDao")
@Transactional
public class JdbcTemplateApplicationDao extends AbstractJdbcTemplateDao implements ApplicationDao {

    private static final String SELECT = "SELECT * FROM application " +
            "JOIN type_of_work USING (id_type_of_work) " +
            "LEFT JOIN user USING (id_user) ";
    private static final String ORDER_BY = "ORDER BY application.status, application.desired_time DESC";

    private static final String SELECT_ALL = SELECT + ORDER_BY;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_application = ?";
    private static final String SELECT_BY_TYPE_OF_WORK = SELECT + "WHERE type_of_work.description LIKE ?";
    private static final String SELECT_BY_TENANT = SELECT + "WHERE id_user = ?";
    private static final String SELECT_BY_STATUS = SELECT + "WHERE status = ?";

    private static final String INSERT = "INSERT INTO application " +
            "(id_user, id_type_of_work, scale_of_problem, desired_time, status) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM application WHERE id_application = ?";
    private static final String UPDATE = "UPDATE application SET id_user = ?, id_type_of_work = ?, " +
            "scale_of_problem = ?, desired_time = ?, status = ? " +
            "WHERE id_application = ?";

    @Override
    public Optional<Application> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getApplicationRowMapper(), id));
    }

    @Override
    public List<Application> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getApplicationRowMapper());
    }

    @Override
    public void add(Application application) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, application.getUser().getId());
            statement.setInt(2, application.getTypeOfWork().getId());
            statement.setString(3, application.getProblemScale().name());
            statement.setTimestamp(4, convertLocalDateTimeInTimestamp(application.getDesiredTime()));
            statement.setString(5, application.getStatus().name());
            statement.setString(6, application.getAddress());
            return statement;
        }, keyHolder);
        application.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(Application application) {
        jdbcTemplate.update(UPDATE, application.getUser().getId(), application.getTypeOfWork().getId(),
                application.getProblemScale().name(), convertLocalDateTimeInTimestamp(application.getDesiredTime()),
                application.getStatus().name(), application.getAddress(), application.getId());
    }

    private Timestamp convertLocalDateTimeInTimestamp(LocalDateTime localDateTime) {
        return (localDateTime != null) ? Timestamp.valueOf(localDateTime) : null;
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        return jdbcTemplate.query(SELECT_BY_TYPE_OF_WORK, getApplicationRowMapper(), '%' + typeOfWork + '%');
    }

    @Override
    public List<Application> getApplicationsByUserId(int userId) {
        return jdbcTemplate.query(SELECT_BY_TENANT, getApplicationRowMapper(), userId);
    }

    @Override
    public List<Application> getApplicationsByStatus(Application.Status status) {
        return jdbcTemplate.query(SELECT_BY_STATUS, getApplicationRowMapper(), status.name());
    }

    private RowMapper<Application> getApplicationRowMapper() {
        return (resultSet, i) -> jdbcHelper.getApplicationFromResultSet(resultSet);
    }
}
