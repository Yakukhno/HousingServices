package ua.training.model.dao.jdbc;

import ua.training.model.dao.DispatcherDao;
import ua.training.model.entities.person.Dispatcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDispatcherDao implements DispatcherDao {

    private static final String SELECT_BY_ID =
            "SELECT * FROM dispatcher WHERE id_dispatcher = ?";
    private static final String SELECT_BY_ONLINE =
            "SELECT * FROM dispatcher WHERE is_online = TRUE";
    private static final String SELECT_BY_EMAIL =
            "SELECT * FROM dispatcher WHERE email = ?";
    private static final String SELECT_ALL = "SELECT * FROM dispatcher";

    private static final String INSERT =
            "INSERT INTO dispatcher (is_online, name, email, password) " +
                    "VALUES (?, ?, ?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM dispatcher WHERE id_dispatcher = ?";
    private static final String UPDATE =
            "UPDATE dispatcher " +
                    "SET is_online = ?, name = ?, email = ?, password = ? " +
                    "WHERE id_dispatcher = ?";
    private static final String SET_ONLINE =
            "UPDATE dispatcher " +
                    "SET is_online = ? " +
                    "WHERE id_dispatcher = ?";

    private static final String DISPATCHER_ID = "id_dispatcher";
    private static final String DISPATCHER_ONLINE = "is_online";
    private static final String DISPATCHER_NAME = "name";
    private static final String DISPATCHER_EMAIL = "email";
    private static final String DISPATCHER_PASSWORD = "password";

    private Connection connection;

    JdbcDispatcherDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Dispatcher> get(int id) {
        Optional<Dispatcher> dispatcher = Optional.empty();
        try (PreparedStatement statement
                     = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                dispatcher = Optional.of(getDispatcherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dispatcher;
    }

    @Override
    public Optional<Dispatcher> getDispatcherByEmail(String email) {
        Optional<Dispatcher> dispatcher = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_EMAIL)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                dispatcher = Optional.of(getDispatcherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dispatcher;
    }

    @Override
    public List<Dispatcher> getAll() {
        return getDispatchers(SELECT_ALL);
    }

    @Override
    public void add(Dispatcher dispatcher) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            setStatementFromDispatcher(statement, dispatcher);
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                dispatcher.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement
                     = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Dispatcher dispatcher) {
        try (PreparedStatement statement
                     = connection.prepareStatement(UPDATE)) {
            setStatementFromDispatcher(statement, dispatcher);
            statement.setInt(5, dispatcher.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setDispatcherOnline(int dispatcherId, boolean isOnline) {
        try (PreparedStatement statement
                     = connection.prepareStatement(SET_ONLINE)) {
            statement.setBoolean(1, isOnline);
            statement.setInt(2, dispatcherId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Dispatcher> getOnlineDispatchers() {
        return getDispatchers(SELECT_BY_ONLINE);
    }

    private List<Dispatcher> getDispatchers(String query) {
        List<Dispatcher> dispatchers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                dispatchers.add(getDispatcherFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dispatchers;
    }

    private Dispatcher getDispatcherFromResultSet(ResultSet resultSet)
            throws SQLException {
       return new Dispatcher.Builder()
               .setId(resultSet.getInt(DISPATCHER_ID))
               .setOnline(resultSet.getBoolean(DISPATCHER_ONLINE))
               .setName(resultSet.getString(DISPATCHER_NAME))
               .setEmail(resultSet.getString(DISPATCHER_EMAIL))
               .setPassword(resultSet.getString(DISPATCHER_PASSWORD))
               .build();
    }

    private void setStatementFromDispatcher(PreparedStatement statement,
                                            Dispatcher dispatcher)
            throws SQLException {
        statement.setBoolean(1, dispatcher.isOnline());
        statement.setString(2, dispatcher.getName());
        statement.setString(3, dispatcher.getEmail());
        statement.setString(4, dispatcher.getPassword());
    }
}
