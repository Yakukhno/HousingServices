package ua.training.model.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ua.training.exception.DaoException;
import ua.training.model.dao.jdbc.util.JdbcHelper;

public abstract class AbstractJdbcDao {

    private static final String EXCEPTION_DELETE = "Failed delete from '%s' with id = %d";

    protected JdbcHelper jdbcHelper;
    protected DataSource dataSource;

    protected Logger logger = Logger.getLogger(this.getClass());

    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected void delete(String tableName, String deleteQuery, int id) {
        try (PreparedStatement statement = getConnection().prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_DELETE, tableName, id);
            throw getDaoException(message, e);
        }
    }

    protected DaoException getDaoException(String message, SQLException e) {
        logger.error(message, e);
        return new DaoException(e);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcHelper getJdbcHelper() {
        return jdbcHelper;
    }

    @Autowired
    public void setJdbcHelper(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }
}
