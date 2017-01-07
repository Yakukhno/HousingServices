package ua.training.model.dao.jdbc;

import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final String WORKER_ID = "id_worker";
    public static final String WORKER_NAME = "name";

    private Connection connection;

    public JdbcWorkerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Worker get(int id) {
        Worker worker = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                Worker.Builder builder = new Worker.Builder()
                        .setId(resultSet.getInt(WORKER_ID))
                        .setName(resultSet.getString(WORKER_NAME));

                builder.addTypeOfWork(new TypeOfWork.Builder()
                        .setId(resultSet
                                .getInt(JdbcTypeOfWorkDao.TYPE_OF_WORK_ID))
                        .setDescription(resultSet
                                .getString(JdbcTypeOfWorkDao.TYPE_OF_WORK_STRING))
                        .build());
                while (resultSet.next()) {
                    builder.addTypeOfWork(new TypeOfWork.Builder()
                            .setId(resultSet
                                    .getInt(JdbcTypeOfWorkDao.TYPE_OF_WORK_ID))
                            .setDescription(resultSet
                                    .getString(JdbcTypeOfWorkDao.TYPE_OF_WORK_STRING))
                            .build());
                }

                worker = builder.build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return worker;
    }

    @Override
    public List<Worker> getAll() {
        List<Worker> workers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_WORKERS)) {
            int currentId = -1;

            Worker worker = null;
            while (resultSet.next()) {
                if (currentId != resultSet.getInt(WORKER_ID)) {
                    if (worker != null) {
                        workers.add(worker);
                    }
                    worker = new Worker.Builder()
                            .setId(resultSet.getInt(WORKER_ID))
                            .setName(resultSet.getString(WORKER_NAME))
                            .addTypeOfWork(getTypeOfWork(resultSet))
                            .build();
                } else {
                    if (worker != null) {
                        worker.addTypeOfWork(getTypeOfWork(resultSet));
                    }
                }
                currentId = resultSet.getInt(WORKER_ID);
            }
            if (worker != null) {
                workers.add(worker);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public List<Worker> getWorkersByTypeOfWork(TypeOfWork typeOfWork) {
        List<Worker> workers = new ArrayList<>();
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_TYPE)) {
            statement.setInt(1, typeOfWork.getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                workers.add(get(resultSet.getInt(WORKER_ID)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return workers;
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
            throw new RuntimeException(e);
        }
    }

    private TypeOfWork getTypeOfWork(ResultSet resultSet) throws SQLException {
        return new TypeOfWork.Builder()
                .setId(resultSet
                        .getInt(JdbcTypeOfWorkDao.TYPE_OF_WORK_ID))
                .setDescription(resultSet
                        .getString(JdbcTypeOfWorkDao.TYPE_OF_WORK_STRING))
                .build();
    }

    private void insertTypesOfWork(Worker worker) {
        try (PreparedStatement typeStatement =
                     connection.prepareStatement(INSERT_TYPE_OF_WORK)) {
            for (TypeOfWork typeOfWork : worker.getTypesOfWork()) {
                typeStatement.setInt(1, worker.getId());
                typeStatement.setInt(2, typeOfWork.getId());
                typeStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
