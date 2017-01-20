package ua.training.model.dao.jdbc;

import com.mysql.jdbc.Driver;
import ua.training.model.dao.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDaoFactory extends DaoFactory {

    private static final String DB_PROPERTIES_FILE = "/db.properties";
    private static final String DB_URL = "url";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";

    private DataSource dataSource;

    public JdbcDaoFactory() {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup(
                    "java:comp/env/jdbc/housing_services"
            );
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DaoConnection getConnection() {
        try {
            return new JdbcDaoConnection(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApplicationDao createApplicationDao(DaoConnection daoConnection) {
        return new JdbcApplicationDao(getSqlConnection(daoConnection));
    }

    @Override
    public TenantDao createTenantDao(DaoConnection daoConnection) {
        return new JdbcTenantDao(getSqlConnection(daoConnection));
    }

    @Override
    public DispatcherDao createDispatcherDao(DaoConnection daoConnection) {
        return new JdbcDispatcherDao(getSqlConnection(daoConnection));
    }

    @Override
    public WorkerDao createWorkerDao(DaoConnection daoConnection) {
        return new JdbcWorkerDao(getSqlConnection(daoConnection));
    }

    @Override
    public BrigadeDao createBrigadeDao(DaoConnection daoConnection) {
        return new JdbcBrigadeDao(getSqlConnection(daoConnection));
    }

    @Override
    public TaskDao createTaskDao(DaoConnection daoConnection) {
        return new JdbcTaskDao(getSqlConnection(daoConnection));
    }

    @Override
    public TypeOfWorkDao createTypeOfWorkDao(DaoConnection daoConnection) {
        return new JdbcTypeOfWorkDao(getSqlConnection(daoConnection));
    }

    private Connection getSqlConnection(DaoConnection daoConnection) {
        JdbcDaoConnection jdbcDaoConnection = (JdbcDaoConnection) daoConnection;
        return jdbcDaoConnection.getConnection();
    }
}
