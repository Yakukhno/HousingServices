package ua.training.model.dao.jdbc;

import static ua.training.util.RepositoryConstants.TYPE_OF_WORK_TABLE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;

@Repository("typeOfWorkDao")
@Transactional
public class JdbcTypeOfWorkDao extends AbstractJdbcDao implements TypeOfWorkDao {

    private static final String SELECT = "SELECT * FROM type_of_work ";

    private static final String SELECT_ALL = SELECT;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_type_of_work = ?";
    private static final String SELECT_BY_DESCRIPTION = SELECT + "WHERE description LIKE ?";

    private static final String INSERT = "INSERT INTO type_of_work (description) VALUES (?)";
    private static final String DELETE_BY_ID = "DELETE FROM type_of_work WHERE id_type_of_work = ?";
    private static final String UPDATE = "UPDATE type_of_work SET description = ? WHERE id_type_of_work = ?";

    private static final String EXCEPTION_GET_BY_ID = "Failed select from 'type_of_work' with id = %d";
    private static final String EXCEPTION_GET_BY_DESCRIPTION =
            "Failed select from 'type_of_work' with type_of_work like %s";
    private static final String EXCEPTION_GET_ALL = "Failed select from 'type_of_work'";
    private static final String EXCEPTION_ADD = "Failed insert into 'type_of_work' value = %s";
    private static final String EXCEPTION_UPDATE = "Failed update 'type_of_work' value = %s";

    @Override
    public Optional<TypeOfWork> get(int id) {
        Optional<TypeOfWork> typeOfWork = Optional.empty();
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                typeOfWork = Optional.of(jdbcHelper.getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ID, id);
            throw getDaoException(message, e);
        }
        return typeOfWork;
    }

    @Override
    public List<TypeOfWork> getAll() {
        List<TypeOfWork> typesOfWork = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                typesOfWork.add(jdbcHelper.getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_ALL, e);
        }
        return typesOfWork;
    }

    @Override
    public void add(TypeOfWork typeOfWork) {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, typeOfWork.getDescription());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                typeOfWork.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_ADD, typeOfWork);
            throw getDaoException(message, e);
        }
    }

    @Override
    public void delete(int id) {
        delete(TYPE_OF_WORK_TABLE, DELETE_BY_ID, id);
    }

    @Override
    public void update(TypeOfWork typeOfWork) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE)) {
            statement.setString(1, typeOfWork.getDescription());
            statement.setInt(2, typeOfWork.getId());
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_UPDATE, typeOfWork);
            throw getDaoException(message, e);
        }
    }

    @Override
    public List<TypeOfWork> getByDescription(String description) {
        List<TypeOfWork> typesOfWork = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_DESCRIPTION)) {
            statement.setString(1, '%' + description + '%');

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                typesOfWork.add(jdbcHelper.getTypeOfWorkFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_DESCRIPTION, description);
            throw getDaoException(message, e);
        }
        return typesOfWork;
    }
}
