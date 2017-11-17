package ua.training.model.dao.jdbc;

import org.apache.log4j.Logger;
import ua.training.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractJdbcDao {

    private static final String EXCEPTION_DELETE = "Failed delete from '%s' with id = %d";

    protected JdbcHelper helper = new JdbcHelper();
    protected Connection connection;
    protected Logger logger;

    protected void delete(String tableName, String deleteQuery, int id) {
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
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
}
