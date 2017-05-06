package ua.training.model.dao.jdbc;

import org.apache.log4j.Logger;
import ua.training.model.dao.ApplicationDao;
import ua.training.model.entities.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcApplicationDao extends AbstractJdbcDao
        implements ApplicationDao {

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

    private static final String EXCEPTION_GET_BY_ID
            = "Failed select from 'application' with id = %d";
    private static final String EXCEPTION_GET_BY_TYPE_OF_WORK
            = "Failed select from 'application' with type_of_work like %s";
    private static final String EXCEPTION_GET_BY_USER_ID
            = "Failed select from 'application' with id_user = %d";
    private static final String EXCEPTION_GET_BY_STATUS
            = "Failed select from 'application' with status = %s";
    private static final String EXCEPTION_GET_ALL
            = "Failed select from 'application'";
    private static final String EXCEPTION_ADD
            = "Failed insert into 'application' value = %s";
    private static final String EXCEPTION_UPDATE
            = "Failed update 'application' value = %s";

    static final String APPLICATION_TABLE = "application";
    static final String APPLICATION_ID = "id_application";
    static final String APPLICATION_SCALE_OF_PROBLEM = "scale_of_problem";
    static final String APPLICATION_DESIRED_TIME = "desired_time";
    static final String APPLICATION_STATUS = "status";

    JdbcApplicationDao(Connection connection) {
        this.connection = connection;
        logger = Logger.getLogger(JdbcApplicationDao.class);
    }

    @Override
    public Optional<Application> get(int id) {
        Optional<Application> application = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                application = Optional.of(
                        helper.getApplicationFromResultSet(resultSet)
                );
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ID, id);
            throw getDaoException(message, e);
        }
        return application;
    }

    @Override
    public List<Application> getAll() {
        List<Application> applications = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                applications.add(helper.getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_ALL, e);
        }
        return applications;
    }

    @Override
    public void add(Application application) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromApplication(statement, application);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                application.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_ADD, application);
            throw getDaoException(message, e);
        }
    }

    @Override
    public void delete(int id) {
        delete(APPLICATION_TABLE, DELETE_BY_ID, id);
    }

    @Override
    public void update(Application application) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromApplication(statement, application);
            statement.setInt(6, application.getId());
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_UPDATE, application);
            throw getDaoException(message, e);
        }
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TYPE_OF_WORK)) {
            statement.setString(1, '%' + typeOfWork + '%');

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(helper.getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_TYPE_OF_WORK,
                    typeOfWork);
            throw getDaoException(message, e);
        }
        return applications;
    }

    @Override
    public List<Application> getApplicationsByUserId(int userId) {
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TENANT)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(helper.getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_USER_ID, userId);
            throw getDaoException(message, e);
        }
        return applications;
    }

    @Override
    public List<Application> getApplicationsByStatus(Application.Status status) {
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_STATUS)) {
            statement.setString(1, status.name());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(helper.getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_STATUS, status);
            throw getDaoException(message, e);
        }
        return applications;
    }

    private void setStatementFromApplication(PreparedStatement statement,
                                            Application application)
            throws SQLException {
        Timestamp timestamp = (application.getDesiredTime() != null)
                ? Timestamp.valueOf(application.getDesiredTime())
                : null;
        statement.setInt(1, application.getTenant().getId());
        statement.setInt(2, application.getTypeOfWork().getId());
        statement.setString(3, application.getProblemScale().name());
        statement.setTimestamp(4, timestamp);
        statement.setString(5, application.getStatus().name());
    }
}
