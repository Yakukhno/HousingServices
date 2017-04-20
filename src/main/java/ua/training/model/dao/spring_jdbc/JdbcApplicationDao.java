package ua.training.model.dao.spring_jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.jdbc.JdbcHelper;
import ua.training.model.entities.Application;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcApplicationDao implements ApplicationDao {

    private static final String SELECT =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "LEFT JOIN user USING (id_user) ";
    private static final String ORDER_BY =
            "ORDER BY application.status, application.desired_time DESC";

    private static final String SELECT_ALL = SELECT + ORDER_BY;
    private static final String SELECT_BY_ID = SELECT +
            "WHERE id_application = ?";
    private static final String SELECT_BY_TYPE_OF_WORK = SELECT +
            "WHERE type_of_work.description LIKE ?";
    private static final String SELECT_BY_TENANT = SELECT +
            "WHERE id_user = ?";
    private static final String SELECT_BY_STATUS = SELECT +
            "WHERE status = ?";

    private static final String INSERT =
            "INSERT INTO application " +
                    "(id_user, id_type_of_work, scale_of_problem, " +
                    "desired_time, status) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM application WHERE id_application = ?";
    private static final String UPDATE =
            "UPDATE application SET id_user = ?, id_type_of_work = ?, " +
                    "scale_of_problem = ?, desired_time = ?, status = ? " +
                    "WHERE id_application = ?";

    private JdbcHelper helper = new JdbcHelper();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcApplicationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Application> get(int id) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        SELECT_BY_ID, new Object[]{id}, getApplicationRowMapper()
                )
        );
    }

    @Override
    public List<Application> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getApplicationRowMapper());
    }

    @Override
    public void add(Application application) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, application.getTenant().getId());
                    ps.setInt(2, application.getTypeOfWork().getId());
                    ps.setString(3, application.getScaleOfProblem().name());
                    ps.setTimestamp(4, convertLocalDateTimeInTimestamp(application.getDesiredTime()));
                    ps.setString(5, application.getStatus().name());
                    return ps;
                },
                keyHolder);
        application.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(Application application) {
        jdbcTemplate.update(UPDATE,
                application.getTenant().getId(),
                application.getTypeOfWork().getId(),
                application.getScaleOfProblem().name(),
                convertLocalDateTimeInTimestamp(application.getDesiredTime()),
                application.getStatus().name(),
                application.getId());
    }

    private Timestamp convertLocalDateTimeInTimestamp(LocalDateTime localDateTime) {
        return (localDateTime != null)
                ? Timestamp.valueOf(localDateTime)
                : null;
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        return jdbcTemplate.query(
                SELECT_BY_TYPE_OF_WORK, new Object[]{typeOfWork},
                getApplicationRowMapper()
        );
    }

    @Override
    public List<Application> getApplicationsByUserId(int userId) {
        return jdbcTemplate.query(
                SELECT_BY_TENANT, new Object[]{userId}, getApplicationRowMapper()
        );
    }

    @Override
    public List<Application> getApplicationsByStatus(Application.Status status) {
        return jdbcTemplate.query(
                SELECT_BY_STATUS, new Object[]{status.name()},
                getApplicationRowMapper()
        );
    }

    private RowMapper<Application> getApplicationRowMapper() {
        return (resultSet, i) -> helper.getApplicationFromResultSet(resultSet);
    }
}
