package ua.training.model.dao.jdbc;

import ua.training.model.dao.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FakeJdbcDaoFactory extends DaoFactory {

    private static final String DB_PROPERTIES_FILE = "/db.properties";
    private static final String DB_URL = "url";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";

    private Connection connection;

    public FakeJdbcDaoFactory() {
        try {
            Properties properties = new Properties();
            properties.load(FakeJdbcDaoFactory.class
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
    public DaoConnection getConnection() {
        return null;
    }

    @Override
    public ApplicationDao createApplicationDao(DaoConnection daoConnection) {
        return null;
    }

    @Override
    public UserDao createUserDao(DaoConnection daoConnection) {
        return new JdbcUserDao(connection);
    }

    @Override
    public WorkerDao createWorkerDao(DaoConnection daoConnection) {
        return null;
    }

    @Override
    public BrigadeDao createBrigadeDao(DaoConnection daoConnection) {
        return null;
    }

    @Override
    public TaskDao createTaskDao(DaoConnection daoConnection) {
        return null;
    }

    @Override
    public TypeOfWorkDao createTypeOfWorkDao(DaoConnection daoConnection) {
        return null;
    }
}
