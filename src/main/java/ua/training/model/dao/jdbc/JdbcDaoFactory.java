package ua.training.model.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TaskDao;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.dao.UserDao;
import ua.training.model.dao.WorkerDao;

public class JdbcDaoFactory extends DaoFactory {

    private DataSource dataSource;

    public JdbcDaoFactory() {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/housing_services");
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
    public UserDao createUserDao(DaoConnection daoConnection) {
        return new JdbcUserDao(getSqlConnection(daoConnection));
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
