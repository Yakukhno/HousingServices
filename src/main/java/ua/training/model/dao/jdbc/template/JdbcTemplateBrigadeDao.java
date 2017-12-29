package ua.training.model.dao.jdbc.template;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.entities.Brigade;

@Repository("brigadeDao")
@Transactional
public class JdbcTemplateBrigadeDao extends AbstractJdbcTemplateDao implements BrigadeDao {

    private static final String SELECT = "SELECT * FROM brigade " +
            "LEFT JOIN brigade_has_worker USING (id_brigade) " +
            "LEFT JOIN worker USING (id_worker) " +
            "LEFT JOIN worker_has_type_of_work USING (id_worker) " +
            "LEFT JOIN type_of_work USING (id_type_of_work) " +
            "LEFT JOIN " +
            "(SELECT id_worker as manager, name as manager_name, " +
            "id_type_of_work as manager_type_id, " +
            "description as manager_type_description FROM worker " +
            "JOIN worker_has_type_of_work USING (id_worker) " +
            "JOIN type_of_work USING (id_type_of_work)) as table1 " +
            "USING (manager) ";
    private static final String ORDER_BY = "ORDER BY id_brigade, id_worker, manager_type_id";

    private static final String SELECT_ALL = SELECT + ORDER_BY;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_brigade = ? " + ORDER_BY;

    private static final String INSERT = "INSERT INTO brigade (manager) VALUES (?);";
    private static final String INSERT_WORKER = "INSERT INTO brigade_has_worker (id_brigade, id_worker) VALUES (?, ?);";
    private static final String DELETE_BY_ID = "DELETE FROM brigade WHERE id_brigade = ?";
    private static final String UPDATE = "UPDATE brigade SET manager = ? WHERE id_brigade = ?; " +
            "DELETE FROM brigade_has_worker WHERE id_brigade = ?";

    @Override
    public Optional<Brigade> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getBrigadeRowMapper(), id));
    }

    @Override
    public List<Brigade> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getBrigadeRowMapper());
    }

    @Override
    public void add(Brigade brigade) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setInt(1, brigade.getManager().getId());
            return statement;
        }, keyHolder);
        brigade.setId(keyHolder.getKey().intValue());

        if (!brigade.getWorkers().isEmpty()) {
            addConnectionToWorkers(brigade);
        }
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(Brigade brigade) {
        jdbcTemplate.update(UPDATE, brigade.getManager().getId(), brigade.getId(), brigade.getId());
        addConnectionToWorkers(brigade);
    }

    private RowMapper<Brigade> getBrigadeRowMapper() {
        return (resultSet, i) -> jdbcHelper.getBrigadeFromResultSet(resultSet);
    }

    private void addConnectionToWorkers(Brigade brigade) {
        List<Object[]> batchArgs = brigade.getWorkers().stream()
                .map(worker -> new Object[]{brigade.getId(), worker.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(INSERT_WORKER, batchArgs);
    }
}
