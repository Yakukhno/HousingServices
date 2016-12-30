package ua.training.model.dao.jdbc;

import ua.training.model.dao.*;

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

    private Connection connection;

    public JdbcDaoFactory() {
        try {
            Properties properties = new Properties();
            properties.load(JdbcTenantDao.class
                    .getResourceAsStream(DB_PROPERTIES_FILE));
            connection = DriverManager.getConnection(
                    properties.getProperty(DB_URL),
                    properties.getProperty(DB_USER),
                    properties.getProperty(DB_PASSWORD));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApplicationDao createApplicationDao() {
        return new JdbcApplicationDao(connection);
    }

    @Override
    public TenantDao createTenantDao() {
        return new JdbcTenantDao(connection);
    }

    @Override
    public WorkerDao createWorkerDao() {
        return null;
    }

    @Override
    public TaskDao createWorkDao() {
        return null;
    }

    @Override
    public TypeOfWorkDao createTypeOfWorkDao() {
        return new JdbcTypeOfWorkDao(connection);
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
