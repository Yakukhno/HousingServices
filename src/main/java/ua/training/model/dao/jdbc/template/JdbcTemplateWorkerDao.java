package ua.training.model.dao.jdbc.template;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;

@Repository("workerDao")
@Transactional
public class JdbcTemplateWorkerDao extends AbstractJdbcTemplateDao implements WorkerDao {

    private static final String SELECT = "SELECT * FROM worker " +
            "JOIN worker_has_type_of_work USING (id_worker) " +
            "JOIN type_of_work USING (id_type_of_work) ";
    private static final String ORDER_BY = "ORDER BY id_worker ";

    private static final String SELECT_ALL = SELECT + ORDER_BY;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_worker = ?";
    private static final String SELECT_BY_TYPE = "SELECT id_worker FROM worker_has_type_of_work " +
            "WHERE id_type_of_work = ?";

    private static final String INSERT = "INSERT INTO worker (name) VALUES (?);";
    private static final String INSERT_TYPE_OF_WORK =
            "INSERT INTO worker_has_type_of_work (id_worker, id_type_of_work) VALUES (?, ?);";
    private static final String DELETE_BY_ID = "DELETE FROM worker WHERE id_worker = ?";
    private static final String UPDATE = "UPDATE worker SET name = ? WHERE id_worker = ?; " +
            "DELETE FROM worker_has_type_of_work WHERE id_worker = ?";

    @Override
    public Optional<Worker> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getWorkerRowMapper(), 1));
    }

    @Override
    public List<Worker> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getWorkerRowMapper());
    }

    @Override
    public void add(Worker worker) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, worker.getName());
            return statement;
        }, keyHolder);
        worker.setId(keyHolder.getKey().intValue());

        addConnectionToTypesOfWork(worker);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, 1);
    }

    @Override
    public void update(Worker worker) {
        jdbcTemplate.update(UPDATE, worker.getName(), worker.getId(), worker.getId());
        addConnectionToTypesOfWork(worker);
    }

    @Override
    public List<Worker> getWorkersByTypeOfWork(int typeOfWorkId) {
        return jdbcTemplate.query(SELECT_BY_TYPE, getWorkerRowMapper(), typeOfWorkId);
    }

    private RowMapper<Worker> getWorkerRowMapper() {
        return (resultSet, i) -> jdbcHelper.getWorkerFromResultSet(resultSet);
    }

    private void addConnectionToTypesOfWork(Worker worker) {
        List<Object[]> batchArgs = worker.getTypesOfWork().stream()
                .map(typeOfWork -> new Object[]{worker.getId(), typeOfWork.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(INSERT_TYPE_OF_WORK, batchArgs);
    }
}
