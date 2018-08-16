package ua.training.model.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;
import ua.training.util.JpaConstants;

@Repository("workerDao")
@Transactional
public class JpaWorkerDao extends AbstractJpaDao<Worker> implements WorkerDao {

    private static final String ID_PARAMETER = "id";

    public JpaWorkerDao() {
        super(Worker.class);
    }

    @Override
    public List<Worker> getWorkersByTypeOfWork(final int typeOfWorkId) {
        return entityManager.createNamedQuery(JpaConstants.WORKER_FIND_BY_TYPE_OF_WORK, Worker.class).getResultList();
    }

    @Override
    public List<Worker> getAll() {
        return entityManager.createNamedQuery(JpaConstants.WORKER_FIND_ALL, Worker.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.WORKER_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
