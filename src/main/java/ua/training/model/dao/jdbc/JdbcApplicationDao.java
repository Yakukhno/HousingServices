package ua.training.model.dao.jdbc;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.entities.Application;
import ua.training.model.entities.Tenant;
import ua.training.model.entities.TypeOfWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcApplicationDao implements ApplicationDao {

    private static final String SELECT_ALL =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "JOIN tenant USING (id_tenant)";
    private static final String SELECT_BY_ID =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "JOIN tenant USING (id_tenant) " +
            "WHERE id_application = ?";
    private static final String SELECT_BY_TYPE_OF_WORK =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "JOIN tenant USING (id_tenant) " +
                    "WHERE type_of_work.description LIKE ?";
    private static final String SELECT_BY_TENANT =
            "SELECT * FROM application " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "JOIN tenant USING (id_tenant) " +
                    "WHERE id_tenant = ?";

    private static final String INSERT =
            "INSERT INTO application (id_tenant, id_type_of_work, " +
                    "scale_of_problem, desired_time) VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM application WHERE id_application = ?";
    private static final String UPDATE =
            "UPDATE application SET id_tenant = ?, id_type_of_work = ?, " +
                    "scale_of_problem = ?, desired_time = ? " +
                    "WHERE id_application = ?";

    private static final String APPLICATION_ID = "id_application";
    private static final String APPLICATION_SCALE_OF_PROBLEM =
            "scale_of_problem";
    private static final String APPLICATION_DESIRED_TIME =
            "desired_time";

    private Connection connection;

    public JdbcApplicationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Application get(int id) {
        Application application = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                application = getApplicationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return applications;
    }

    @Override
    public void add(Application application) {
        try(PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromApplication(statement, application);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                application.setId(resultSet.getInt(1));
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
    public void update(Application application) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromApplication(statement, application);
            statement.setInt(5, application.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return applications;
    }

    @Override
    public List<Application> getApplicationsByTenantId(int tenantId) {
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TENANT)) {
            statement.setInt(1, tenantId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                applications.add(getApplicationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return applications;
    }

    private Application getApplicationFromResultSet(ResultSet resultSet) {
        try {
            TypeOfWork typeOfWork = new TypeOfWork.Builder()
                    .setId(resultSet
                            .getInt(JdbcTypeOfWorkDao.TYPE_OF_WORK_ID))
                    .setDescription(resultSet
                            .getString(JdbcTypeOfWorkDao.TYPE_OF_WORK_STRING))
                    .build();

            Tenant tenant = new Tenant.Builder()
                    .setId(resultSet.getInt(JdbcTenantDao.TENANT_ID))
                    .setAccount(resultSet.getInt(JdbcTenantDao.TENANT_ACCOUNT))
                    .setName(resultSet.getString(JdbcTenantDao.TENANT_NAME))
                    .setEmail(resultSet.getString(JdbcTenantDao.TENANT_EMAIL))
                    .setPassword(resultSet.getString(JdbcTenantDao.TENANT_PASSWORD))
                    .build();
            
            return new Application.Builder()
                    .setId(resultSet.getInt(APPLICATION_ID))
                    .setTypeOfWork(typeOfWork)
                    .setTenant(tenant)
                    .setScaleOfProblem(resultSet
                            .getString(APPLICATION_SCALE_OF_PROBLEM))
                    .setDesiredTime(resultSet
                            .getTimestamp(APPLICATION_DESIRED_TIME))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setStatementFromApplication(PreparedStatement statement,
                                            Application application)
            throws SQLException {
        statement.setInt(1, application.getTenant().getId());
        statement.setInt(2, application.getTypeOfWork().getId());
        statement.setString(3, application.getScaleOfProblem());
        statement.setTimestamp(4, application.getDesiredTime());
    }
}
