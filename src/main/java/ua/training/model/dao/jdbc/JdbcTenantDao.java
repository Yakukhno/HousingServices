package ua.training.model.dao.jdbc;

import ua.training.model.dao.TenantDao;
import ua.training.model.entities.person.Tenant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTenantDao implements TenantDao {

    private static final String SELECT_BY_ID =
            "SELECT * FROM tenant WHERE id_tenant = ?";
    private static final String SELECT_BY_ACCOUNT =
            "SELECT * FROM tenant WHERE account = ?";
    private static final String SELECT_BY_EMAIL =
            "SELECT * FROM tenant WHERE email = ?";
    private static final String SELECT_ALL = "SELECT * FROM tenant";

    private static final String INSERT =
            "INSERT INTO tenant (account, name, email, password) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM tenant WHERE id_tenant = ?";
    private static final String UPDATE =
            "UPDATE tenant " +
                    "SET account = ?, name = ?, email = ?, password = ? " +
                    "WHERE id_tenant = ?";

    static final String TENANT_ID = "id_tenant";
    static final String TENANT_ACCOUNT = "account";
    static final String TENANT_NAME = "name";
    static final String TENANT_EMAIL = "email";
    static final String TENANT_PASSWORD = "password";

    private Connection connection;

    JdbcTenantDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Tenant> get(int id) {
        return getTenant(SELECT_BY_ID, id);
    }

    @Override
    public List<Tenant> getAll() {
        List<Tenant> tenants = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                tenants.add(getTenantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tenants;
    }

    @Override
    public void add(Tenant tenant) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromTenant(statement, tenant);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                tenant.setId(resultSet.getInt(1));
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
    public void update(Tenant tenant) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            setStatementFromTenant(statement, tenant);
            statement.setInt(5, tenant.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Tenant> getTenantByAccount(int account) {
        return getTenant(SELECT_BY_ACCOUNT, account);
    }

    @Override
    public Optional<Tenant> getTenantByEmail(String email) {
        Optional<Tenant> tenant = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_EMAIL)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tenant = Optional.of(getTenantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tenant;
    }

    private Optional<Tenant> getTenant(String query, int parameter) {
        Optional<Tenant> tenant = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setInt(1, parameter);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tenant = Optional.of(getTenantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tenant;
    }

    static Tenant getTenantFromResultSet(ResultSet resultSet)
            throws SQLException {
        return new Tenant.Builder()
                .setId(resultSet.getInt(TENANT_ID))
                .setAccount(resultSet.getInt(TENANT_ACCOUNT))
                .setName(resultSet.getString(TENANT_NAME))
                .setEmail(resultSet.getString(TENANT_EMAIL))
                .setPassword(resultSet.getString(TENANT_PASSWORD))
                .build();
    }

    private void setStatementFromTenant(PreparedStatement statement,
                                             Tenant tenant)
            throws SQLException {
        statement.setInt(1, tenant.getAccount());
        statement.setString(2, tenant.getName());
        statement.setString(3, tenant.getEmail());
        statement.setString(4, tenant.getPassword());
    }
}
