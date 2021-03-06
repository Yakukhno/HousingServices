package ua.training.model.dao.jdbc;

import org.apache.log4j.Logger;
import ua.training.exception.DaoException;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

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
                    "SET name = ?, email = ?, password = ?, role = ?" +
                    "WHERE id_user = ?";

    private static final String EXCEPTION_GET_BY_ID
            = "Failed select from 'user' with id = %d";
    private static final String EXCEPTION_GET_BY_EMAIL
            = "Failed select from 'user' with email = %s";
    private static final String EXCEPTION_GET_BY_ROLE
            = "Failed select from 'user' with role = %s";
    private static final String EXCEPTION_GET_ALL
            = "Failed select from 'user'";
    private static final String EXCEPTION_ADD
            = "Failed insert into 'user' value = %s";
    private static final String EXCEPTION_UPDATE
            = "Failed update 'user' value = %s";
    private static final String EXCEPTION_DUPLICATE_EMAIL
            = "exception.email_exists";
    private static final int ERROR_CODE_DUPLICATE_FIELD = 1062;

    static final String USER_TABLE = "user";
    static final String USER_ID = "id_user";
    static final String USER_NAME = "name";
    static final String USER_EMAIL = "email";
    static final String USER_PASSWORD = "password";
    static final String USER_ROLE = "role";

    JdbcUserDao(Connection connection) {
        this.connection = connection;
        logger = Logger.getLogger(JdbcUserDao.class);
    }

    @Override
    public Optional<User> get(int id) {
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = Optional.of(helper.getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ID, id);
            throw getDaoException(message, e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                users.add(helper.getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_ALL, e);
        }
        return users;
    }

    @Override
    public void add(User user) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromUser(statement, user);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_ADD, user);
            throw getDaoException(message, e, user);
        }
    }

    @Override
    public void delete(int id) {
        delete(USER_TABLE, DELETE_BY_ID, id);
    }

    @Override
    public void update(User user) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromUser(statement, user);
            statement.setInt(5, user.getId());
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_UPDATE, user);
            throw getDaoException(message, e, user);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_EMAIL)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = Optional.of(helper.getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_EMAIL, email);
            throw getDaoException(message, e);
        }
        return user;
    }

    @Override
    public List<User> getUsersByRole(User.Role role) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement
                     = connection.prepareStatement(SELECT_BY_ROLE)) {
            statement.setString(1, role.name());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(helper.getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ROLE, role);
            throw getDaoException(message, e);
        }
        return users;
    }

    private void setStatementFromUser(PreparedStatement statement,
                                      User user)
            throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getRole().name());
    }

    private DaoException getDaoException(String message, SQLException e,
                                         User user) {
        DaoException daoException = new DaoException(e);
        if (e.getErrorCode() == ERROR_CODE_DUPLICATE_FIELD) {
            daoException.setUserMessage(EXCEPTION_DUPLICATE_EMAIL)
                    .addParameter(user.getEmail());
            logger.info(message, e);
        } else {
            logger.error(message, e);
        }
        return daoException;
    }
}
