package ua.training.model.dao.jdbc;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBrigadeDao implements BrigadeDao {

    private static final String SELECT_ALL =
            "SELECT * FROM brigade " +
                    "JOIN brigade_has_worker USING (id_brigade) " +
                    "JOIN worker USING (id_worker) ORDER BY id_brigade";
//                    "JOIN worker_has_type_of_work USING (id_worker)" +
//                    "JOIN type_of_work USING (id_type_of_work)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM brigade " +
                    "JOIN brigade_has_worker USING (id_brigade) " +
                    "WHERE id_brigade = ?";

    private static final String INSERT =
            "INSERT INTO brigade (manager) VALUES (?)";
    private static final String INSERT_WORKER =
            "INSERT INTO brigade_has_worker (id_brigade, id_worker) VALUES (?, ?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM brigade WHERE id_brigade = ?";
    private static final String DELETE_TYPE_OF_WORK_BY_ID =
            "DELETE FROM brigade_has_worker WHERE id_brigade = ?";
    private static final String UPDATE =
            "UPDATE brigade SET manager = ? WHERE id_brigade = ?";

    private static final String BRIGADE_ID = "id_brigade";

    private Connection connection;

    public JdbcBrigadeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Brigade get(int id) {
        Brigade brigade = null;
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.first()) {
                Brigade.Builder builder = new Brigade.Builder()
                        .setId(resultSet.getInt(BRIGADE_ID));

                JdbcWorkerDao jdbcWorkerDao = new JdbcWorkerDao(connection);

                builder.addWorker(jdbcWorkerDao
                        .get(resultSet.getInt(JdbcWorkerDao.WORKER_ID)));
                while (resultSet.next()) {
                    builder.addWorker(jdbcWorkerDao
                            .get(resultSet.getInt(JdbcWorkerDao.WORKER_ID)));
                }

                brigade = builder.build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return brigade;
    }

    @Override
    public List<Brigade> getAll() {
        List<Brigade> brigades = new ArrayList<>();
        try (Statement statement =
                     connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            JdbcWorkerDao jdbcWorkerDao = new JdbcWorkerDao(connection);

            int currentId = -1;
            Brigade brigade = null;
            while (resultSet.next()) {
                if (currentId != resultSet.getInt(BRIGADE_ID)) {
                    if (brigade != null) {
                        brigades.add(brigade);
                    }
                    brigade = new Brigade.Builder()
                            .setId(resultSet.getInt(BRIGADE_ID))
                            .addWorker(jdbcWorkerDao.get(resultSet
                                    .getInt(JdbcWorkerDao.WORKER_ID)))
                            .build();
                } else {
                    if (brigade != null) {
                        brigade.addWorker(jdbcWorkerDao.get(resultSet
                                .getInt(JdbcWorkerDao.WORKER_ID)));
                    }
                }
                currentId = resultSet.getInt(BRIGADE_ID);
            }
            if (brigade != null) {
                brigades.add(brigade);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return brigades;
    }

    @Override
    public void add(Brigade brigade) {
        try (PreparedStatement brigadeStatement =
                     connection.prepareStatement(INSERT,
                             Statement.RETURN_GENERATED_KEYS)) {
            brigadeStatement.setInt(1, brigade.getManager().getId());
            brigadeStatement.execute();

            ResultSet resultSet = brigadeStatement.getGeneratedKeys();
            if (resultSet.next()) {
                brigade.setId(resultSet.getInt(1));
            }
            insertWorkers(brigade);
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
    public void update(Brigade brigade) {
        try (PreparedStatement brigadeStatement =
                     connection.prepareStatement(UPDATE);
             PreparedStatement workerStatement =
                     connection.prepareStatement(DELETE_TYPE_OF_WORK_BY_ID)) {
            brigadeStatement.setInt(1, brigade.getManager().getId());
            brigadeStatement.setInt(2, brigade.getId());
            brigadeStatement.execute();

            workerStatement.setInt(1, brigade.getId());
            workerStatement.execute();

            insertWorkers(brigade);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertWorkers(Brigade brigade) {
        try (PreparedStatement workerStatement =
                     connection.prepareStatement(INSERT_WORKER)) {
            for (Worker worker : brigade.getWorkers()) {
                workerStatement.setInt(1, brigade.getId());
                workerStatement.setInt(2, worker.getId());
                workerStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
