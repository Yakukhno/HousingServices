package ua.training.model.dao.jdbc;

import ua.training.model.dao.DaoException;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTypeOfWorkDao implements TypeOfWorkDao {

    private static final String SELECT_ALL =
            "SELECT * FROM type_of_work";
    private static final String SELECT_BY_ID =
            "SELECT * FROM type_of_work WHERE id_type_of_work = ?";
    private static final String SELECT_BY_DESCRIPTION =
            "SELECT * FROM type_of_work WHERE description LIKE ?";

    private static final String INSERT =
            "INSERT INTO type_of_work (description) VALUES (?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM type_of_work WHERE id_type_of_work = ?";
    private static final String UPDATE =
            "UPDATE type_of_work SET description = ? WHERE id_type_of_work = ?";

    static final String TYPE_OF_WORK_ID = "id_type_of_work";
    static final String TYPE_OF_WORK_DESCRIPTION = "description";

    private Connection connection;

    JdbcTypeOfWorkDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<TypeOfWork> get(int id) {
        Optional<TypeOfWork> typeOfWork = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                typeOfWork = Optional.of(getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return typeOfWork;
    }

    @Override
    public List<TypeOfWork> getAll() {
        List<TypeOfWork> typesOfWork = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                typesOfWork.add(getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return typesOfWork;
    }

    @Override
    public void add(TypeOfWork typeOfWork) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, typeOfWork.getDescription());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                typeOfWork.setId(resultSet.getInt(1));
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
    public void update(TypeOfWork typeOfWork) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            statement.setString(1, typeOfWork.getDescription());
            statement.setInt(2, typeOfWork.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<TypeOfWork> getByDescription(String string) {
        List<TypeOfWork> typesOfWork = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_DESCRIPTION)) {
            statement.setString(1, '%' + string + '%');

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                typesOfWork.add(getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return typesOfWork;
    }

    static TypeOfWork getTypeOfWorkFromResultSet(ResultSet resultSet)
            throws SQLException {
        return new TypeOfWork.Builder()
                .setId(resultSet.getInt(TYPE_OF_WORK_ID))
                .setDescription(resultSet.getString(TYPE_OF_WORK_DESCRIPTION))
                .build();
    }

}
