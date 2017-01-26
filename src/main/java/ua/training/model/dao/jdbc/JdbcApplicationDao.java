package ua.training.model.dao.jdbc;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.DaoException;
import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcApplicationDao implements ApplicationDao {

    private static final String SELECT_ALL =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "LEFT JOIN user USING (id_user)";
    private static final String SELECT_BY_ID =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "LEFT JOIN user USING (id_user) " +
            "WHERE id_application = ?";
    private static final String SELECT_BY_TYPE_OF_WORK =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "LEFT JOIN user USING (id_user) " +
                    "WHERE type_of_work.description LIKE ?";
    private static final String SELECT_BY_TENANT =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "JOIN user USING (id_user) " +
                    "WHERE id_user = ?";

    private static final String INSERT =
            "INSERT INTO application (id_user, id_type_of_work, " +
                    "scale_of_problem, desired_time) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM application WHERE id_application = ?";
    private static final String UPDATE =
            "UPDATE application SET id_user = ?, id_type_of_work = ?, " +
                    "scale_of_problem = ?, desired_time = ? " +
                    "WHERE id_application = ?";

    static final String APPLICATION_ID = "id_application";
    static final String APPLICATION_SCALE_OF_PROBLEM =
            "scale_of_problem";
    static final String APPLICATION_DESIRED_TIME =
            "desired_time";

    private Connection connection;

    JdbcApplicationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Application> get(int id) {
        Optional<Application> application = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                application = Optional.of(getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return application;
    }

    @Override
    public List<Application> getAll() {
        List<Application> applications = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                applications.add(getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
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
    public void update(Application application) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromApplication(statement, application);
            statement.setInt(5, application.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
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
                applications.add(getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return applications;
    }

    @Override
    public List<Application> getApplicationsByUserId(int tenantId) {
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TENANT)) {
            statement.setInt(1, tenantId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return applications;
    }

    static Application getApplicationFromResultSet(ResultSet resultSet)
            throws SQLException {
        LocalDateTime localDateTime = null;
        if (resultSet.getTimestamp(APPLICATION_DESIRED_TIME) != null) {
            localDateTime = resultSet.getTimestamp(APPLICATION_DESIRED_TIME)
                    .toLocalDateTime();
        }
        return new Application.Builder()
                    .setId(resultSet.getInt(APPLICATION_ID))
                    .setTypeOfWork(JdbcTypeOfWorkDao
                            .getTypeOfWorkFromResultSet(resultSet))
                    .setTenant(JdbcUserDao
                            .getUserFromResultSet(resultSet))
                    .setScaleOfProblem(ProblemScale.valueOf(resultSet
                            .getString(APPLICATION_SCALE_OF_PROBLEM)))
                    .setDesiredTime(localDateTime)
                    .build();
    }

    private void setStatementFromApplication(PreparedStatement statement,
                                            Application application)
            throws SQLException {
        Timestamp timestamp = (application.getDesiredTime() != null)
                ? Timestamp.valueOf(application.getDesiredTime())
                : null;
        statement.setInt(1, application.getTenant().getId());
        statement.setInt(2, application.getTypeOfWork().getId());
        statement.setString(3, application.getScaleOfProblem().name());
        statement.setTimestamp(4, timestamp);
    }
}
