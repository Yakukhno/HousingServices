package ua.training.model.dao.spring_jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.jdbc.JdbcHelper;
import ua.training.model.entities.person.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserDao implements UserDao {
    private static final String SELECT = "SELECT * FROM user ";

    private static final String SELECT_ALL = SELECT;
    private static final String SELECT_BY_ID = SELECT +
            "WHERE id_user = ?";
    private static final String SELECT_BY_EMAIL = SELECT +
            "WHERE email = ?";
    private static final String SELECT_BY_ROLE = SELECT +
            "WHERE role = ?";

    private static final String INSERT =
            "INSERT INTO user (name, email, password, role) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM user WHERE id_user = ?";
    private static final String UPDATE =
            "UPDATE user " +
                    "SET name = ?, email = ?, password = ?, role = ? " +
                    "WHERE id_user = ?";

    private JdbcHelper helper = new JdbcHelper();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> get(int id) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        SELECT_BY_ID, new Object[]{id}, getUserRowMapper()
                )
        );
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getUserRowMapper());
    }

    @Override
    public void add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getRole().name());
                    return ps;
                },
                keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.getId());
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        SELECT_BY_EMAIL, new Object[]{email}, getUserRowMapper()
                )
        );
    }

    @Override
    public List<User> getUsersByRole(User.Role role) {
        return jdbcTemplate.query(
                SELECT_BY_ROLE, new Object[]{role.name()}, getUserRowMapper()
        );
    }

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, i) -> helper.getUserFromResultSet(resultSet);
    }
}
