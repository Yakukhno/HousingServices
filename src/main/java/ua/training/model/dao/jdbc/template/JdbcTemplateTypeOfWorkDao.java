package ua.training.model.dao.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;

@Repository("typeOfWorkDao")
@Transactional
public class JdbcTemplateTypeOfWorkDao extends AbstractJdbcTemplateDao implements TypeOfWorkDao {

    private static final String SELECT = "SELECT * FROM type_of_work ";

    private static final String SELECT_ALL = SELECT;
    private static final String SELECT_BY_ID = SELECT + "WHERE id_type_of_work = ?";
    private static final String SELECT_BY_DESCRIPTION = SELECT + "WHERE description LIKE ?";

    private static final String INSERT = "INSERT INTO type_of_work (description) VALUES (?)";
    private static final String DELETE_BY_ID = "DELETE FROM type_of_work WHERE id_type_of_work = ?";
    private static final String UPDATE = "UPDATE type_of_work SET description = ? WHERE id_type_of_work = ?";

    @Override
    public Optional<TypeOfWork> get(int id) {
        return Optional.of(jdbcTemplate.queryForObject(SELECT_BY_ID, getTypeOfWorkRowMapper(), id));
    }

    @Override
    public List<TypeOfWork> getAll() {
        return jdbcTemplate.query(SELECT_ALL, getTypeOfWorkRowMapper());
    }

    @Override
    public void add(TypeOfWork typeOfWork) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, typeOfWork.getDescription());
            return statement;
        }, keyHolder);
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
        return jdbcTemplate
                .query(SELECT_BY_DESCRIPTION, getTypeOfWorkRowMapper(), '%' + description + '%');
    }

    private RowMapper<TypeOfWork> getTypeOfWorkRowMapper() {
        return (resultSet, i) -> jdbcHelper.getTypeOfWorkFromResultSet(resultSet);
    }
}
