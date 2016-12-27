package ua.training.model.dao.jdbc;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final String TYPE_OF_WORK_ID = "id_type_of_work";
    public static final String TYPE_OF_WORK_STRING = "description";

    private Connection connection;

    public JdbcTypeOfWorkDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public TypeOfWork get(int id) {
        TypeOfWork typeOfWork = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                typeOfWork = new TypeOfWork.Builder()
                        .setId(resultSet.getInt(TYPE_OF_WORK_ID))
                        .setDescription(resultSet.getString(TYPE_OF_WORK_STRING))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return typeOfWork;
    }

    @Override
    public List<TypeOfWork> getAll() {
        List<TypeOfWork> typesOfWork = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            while (resultSet.next()) {
                TypeOfWork typeOfWork = new TypeOfWork.Builder()
                        .setId(resultSet.getInt(TYPE_OF_WORK_ID))
                        .setDescription(resultSet.getString(TYPE_OF_WORK_STRING))
                        .build();
                typesOfWork.add(typeOfWork);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void update(TypeOfWork typeOfWork) {
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE)) {
            statement.setString(1, typeOfWork.getDescription());
            statement.setInt(2, typeOfWork.getId());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                TypeOfWork typeOfWork = new TypeOfWork.Builder()
                        .setId(resultSet.getInt(TYPE_OF_WORK_ID))
                        .setDescription(resultSet.getString(TYPE_OF_WORK_STRING))
                        .build();
                typesOfWork.add(typeOfWork);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return typesOfWork;
    }
}
