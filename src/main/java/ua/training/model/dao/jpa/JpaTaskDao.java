package ua.training.model.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;
import ua.training.util.JpaConstants;

@Repository("taskDao")
@Transactional
public class JpaTaskDao extends AbstractJpaDao<Task> implements TaskDao {

    private static final String ID_PARAMETER = "id";

    public JpaTaskDao() {
        super(Task.class);
    }

    @Override
    public List<Task> getActiveTasks() {
        return entityManager.createNamedQuery(JpaConstants.TASK_FIND_ACTIVE, Task.class).getResultList();
    }

    @Override
    public List<Task> getAll() {
        return entityManager.createNamedQuery(JpaConstants.TASK_FIND_ALL, Task.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.TASK_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
