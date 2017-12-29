package ua.training.model.dao.jdbc.template;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.training.model.dao.jdbc.util.JdbcHelper;

public abstract class AbstractJdbcTemplateDao {

    protected JdbcHelper jdbcHelper;
    protected JdbcTemplate jdbcTemplate;

    protected Logger logger = Logger.getLogger(this.getClass());

    public JdbcHelper getJdbcHelper() {
        return jdbcHelper;
    }

    @Autowired
    public void setJdbcHelper(JdbcHelper jdbcHelper) {
        this.jdbcHelper = jdbcHelper;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
