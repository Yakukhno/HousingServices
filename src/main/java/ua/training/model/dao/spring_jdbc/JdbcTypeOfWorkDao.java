package ua.training.model.dao.spring_jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.dao.jdbc.JdbcHelper;
import ua.training.model.entities.TypeOfWork;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTypeOfWorkDao implements TypeOfWorkDao {

    private static final String SELECT = "SELECT * FROM type_of_work ";

    private static final String SELECT_ALL = SELECT;
    private static final String SELECT_BY_ID = SELECT +
            "WHERE id_type_of_work = ?";
    private static final String SELECT_BY_DESCRIPTION = SELECT +
            "WHERE description LIKE ?";

    private static final String INSERT =
            "INSERT INTO type_of_work (description) VALUES (?)";
    private static final String DELETE_BY_ID =
            "DELETE FROM type_of_work WHERE id_type_of_work = ?";
    private static final String UPDATE =
            "UPDATE type_of_work SET description = ? WHERE id_type_of_work = ?";

    private JdbcHelper helper = new JdbcHelper();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTypeOfWorkDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<TypeOfWork> get(int id) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        SELECT_BY_ID, new Object[]{id}, getTypeOfWorkRowMapper()
                )
        );
    }

    @Override
    public List<TypeOfWork> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getTypeOfWorkRowMapper());
    }

    @Override
    public void add(TypeOfWork typeOfWork) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, typeOfWork.getDescription());
                    return ps;
                },
                keyHolder);
        typeOfWork.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public void update(TypeOfWork typeOfWork) {
        jdbcTemplate.update(UPDATE, typeOfWork.getDescription(), typeOfWork.getId());
    }

    @Override
    public List<TypeOfWork> getByDescription(String description) {
        return jdbcTemplate.query(
                SELECT_BY_DESCRIPTION,
                new Object[]{'%' + description + '%'},
                getTypeOfWorkRowMapper()
        );
    }

    private RowMapper<TypeOfWork> getTypeOfWorkRowMapper() {
        return (resultSet, i) -> helper.getTypeOfWorkFromResultSet(resultSet);
    }
}
