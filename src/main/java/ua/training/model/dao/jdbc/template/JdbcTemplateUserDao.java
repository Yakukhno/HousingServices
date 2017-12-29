package ua.training.model.dao.jdbc.template;

import static ua.training.util.ExceptionConstants.EXCEPTION_DUPLICATE_EMAIL;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.exception.DaoException;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository("userDao")
@Transactional
public class JdbcTemplateUserDao extends AbstractJdbcTemplateDao implements UserDao {

    private static final String SELECT = "SELECT * FROM user ";

    private static final String SELECT_ALL = SELECT;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_user = ?";
    private static final String SELECT_BY_EMAIL = SELECT + "WHERE email = ?";
    private static final String SELECT_BY_ROLE = SELECT + "WHERE role = ?";

    private static final String INSERT = "INSERT INTO user (name, email, password, role) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM user WHERE id_user = ?";
    private static final String UPDATE = "UPDATE user SET name = ?, email = ?, password = ?, role = ? " +
            "WHERE id_user = ?";

    @Override
    public Optional<User> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getUserRowMapper(), id));
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getUserRowMapper());
    }

    @Override
    public void add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getRole().name());
                return ps;
            }, keyHolder);
            user.setId(keyHolder.getKey().intValue());
        } catch (DuplicateKeyException e) {
            throw getDaoException(e, user.getEmail());
        }
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(User user) {
        try {
            jdbcTemplate.update(UPDATE, user.getName(), user.getEmail(), user.getPassword(), user.getRole().name(),
                    user.getId());
        } catch (DuplicateKeyException e) {
            throw getDaoException(e, user.getEmail());
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_EMAIL, getUserRowMapper(), email));
    }

    @Override
    public List<User> getUsersByRole(User.Role role) {
        return jdbcTemplate.query(SELECT_BY_ROLE, getUserRowMapper(), role.name());
    }

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, i) -> jdbcHelper.getUserFromResultSet(resultSet);
    }

    private DaoException getDaoException(DuplicateKeyException e, String email) {
        DaoException daoException = new DaoException(e);
        daoException.setUserMessage(EXCEPTION_DUPLICATE_EMAIL).addParameter(email);
        logger.info("Duplicate email : " + email, e);
        return daoException;
    }
}
