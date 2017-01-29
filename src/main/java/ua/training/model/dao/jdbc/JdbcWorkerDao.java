package ua.training.model.dao.jdbc;

import org.apache.log4j.Logger;
import ua.training.model.dao.DaoException;
import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkerDao implements WorkerDao {

    private static final String SELECT_ALL_WORKERS =
            "SELECT * FROM worker " +
                    "JOIN worker_has_type_of_work USING (id_worker) " +
                    "JOIN type_of_work USING (id_type_of_work) ORDER BY id_worker";
    private static final String SELECT_BY_ID =
            "SELECT * FROM worker " +
                    "JOIN worker_has_type_of_work USING (id_worker) " +
                    "JOIN type_of_work USING (id_type_of_work) " +
                    "WHERE id_worker = ?";
    private static final String SELECT_BY_TYPE =
            "SELECT id_worker FROM worker_has_type_of_work WHERE id_type_of_work = ?";

    private static final String INSERT =
            "INSERT INTO worker (name) VALUES (?)";
    private static final String INSERT_TYPE_OF_WORK =
            "INSERT INTO worker_has_type_of_work (id_worker, id_type_of_work) VALUES (?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM worker WHERE id_worker = ?";
    private static final String DELETE_TYPE_OF_WORK_BY_ID =
            "DELETE FROM worker_has_type_of_work WHERE id_worker = ?";
    private static final String UPDATE =
            "UPDATE worker SET name = ? WHERE id_worker = ?";

    private static final String EXCEPTION_GET_BY_ID
            = "Failed select from 'worker' with id = %d";
    private static final String EXCEPTION_GET_BY_TYPE_OF_WORK_ID
            = "Failed select from 'worker' with id_type_of_work = %d";
    private static final String EXCEPTION_GET_ALL
            = "Failed select from 'worker'";
    private static final String EXCEPTION_ADD
            = "Failed insert into 'worker' value = %s";
    private static final String EXCEPTION_DELETE
            = "Failed delete from 'worker' with id = %d";
    private static final String EXCEPTION_UPDATE
            = "Failed update 'worker' value = %s";

    static final String WORKER_ID = "id_worker";
    static final String WORKER_NAME = "name";

    private Connection connection;
    private Logger logger = Logger.getLogger(JdbcWorkerDao.class);

    JdbcWorkerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Worker> get(int id) {
        Optional<Worker> worker = Optional.empty();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                worker = Optional.of(getWorkerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_ID, id);
            throw getDaoException(message, e);
        }
        return worker;
    }

    @Override
    public List<Worker> getAll() {
        List<Worker> workers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_WORKERS)) {
            resultSet.next();
            while (!resultSet.isAfterLast()) {
                workers.add(getWorkerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw getDaoException(EXCEPTION_GET_ALL, e);
        }
        return workers;
    }

    @Override
    public void add(Worker worker) {
        try (PreparedStatement workerStatement =
                     connection.prepareStatement(INSERT,
                             Statement.RETURN_GENERATED_KEYS)) {
            workerStatement.setString(1, worker.getName());
            workerStatement.execute();

            ResultSet resultSet = workerStatement.getGeneratedKeys();
            if (resultSet.next()) {
                worker.setId(resultSet.getInt(1));
            }
            insertTypesOfWork(worker);
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_ADD, worker);
            throw getDaoException(message, e);
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(DELETE_BY_ID)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_DELETE, id);
            throw getDaoException(message, e);
        }
    }

    @Override
    public void update(Worker worker) {
        try (PreparedStatement workerStatement =
                     connection.prepareStatement(UPDATE);
             PreparedStatement typeStatement =
                     connection.prepareStatement(DELETE_TYPE_OF_WORK_BY_ID)) {
            workerStatement.setString(1, WORKER_NAME);
            workerStatement.setInt(2, worker.getId());
            workerStatement.execute();

            typeStatement.setInt(1, worker.getId());
            typeStatement.execute();

            insertTypesOfWork(worker);
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_UPDATE, worker);
            throw getDaoException(message, e);
        }
    }

    @Override
    public List<Worker> getWorkersByTypeOfWork(int typeOfWorkId) {
        List<Worker> workers = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TYPE)) {
            statement.setInt(1, typeOfWorkId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                workers.add(getWorkerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            String message = String.format(EXCEPTION_GET_BY_TYPE_OF_WORK_ID,
                    typeOfWorkId);
            throw getDaoException(message, e);
        }
        return workers;
    }

    static Worker getWorkerFromResultSet(ResultSet resultSet)
            throws SQLException {
        int currentId = resultSet.getInt(WORKER_ID);
        Worker.Builder builder = new Worker.Builder()
                .setId(resultSet.getInt(WORKER_ID))
                .setName(resultSet.getString(WORKER_NAME))
                .addTypeOfWork(JdbcTypeOfWorkDao
                        .getTypeOfWorkFromResultSet(resultSet));
        while (resultSet.next() && resultSet.getInt(WORKER_ID) == currentId) {
            builder.addTypeOfWork(JdbcTypeOfWorkDao
                    .getTypeOfWorkFromResultSet(resultSet));
        }
        return builder.build();
    }

    private void insertTypesOfWork(Worker worker) throws SQLException {
        try (PreparedStatement typeStatement =
                     connection.prepareStatement(INSERT_TYPE_OF_WORK)) {
            for (TypeOfWork typeOfWork : worker.getTypesOfWork()) {
                typeStatement.setInt(1, worker.getId());
                typeStatement.setInt(2, typeOfWork.getId());
                typeStatement.execute();
            }
        }
    }

    private DaoException getDaoException(String message, SQLException e) {
        logger.error(message, e);
        return new DaoException(e);
    }
}
