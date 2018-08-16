package ua.training.model.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.entities.Application;
import ua.training.util.JpaConstants;

@Repository("applicationDao")
@Transactional
public class JpaApplicationDao extends AbstractJpaDao<Application> implements ApplicationDao {

    private static final String ID_PARAMETER = "id";
    private static final String TYPE_OF_WORK_PARAMETER = "typeOfWork";
    private static final String USER_ID_PARAMETER = "userId";
    private static final String STATUS_PARAMETER = "status";

    public JpaApplicationDao() {
        super(Application.class);
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(final String typeOfWork) {
        return entityManager.createNamedQuery(JpaConstants.APPLICATION_FIND_BY_TYPE_OF_WORK, Application.class)
                .setParameter(TYPE_OF_WORK_PARAMETER, typeOfWork)
                .getResultList();
    }

    @Override
    public List<Application> getApplicationsByUserId(final int userId) {
        return entityManager.createNamedQuery(JpaConstants.APPLICATION_FIND_BY_USER, Application.class)
                .setParameter(USER_ID_PARAMETER, userId)
                .getResultList();
    }

    @Override
    public List<Application> getApplicationsByStatus(final Application.Status status) {
        return entityManager.createNamedQuery(JpaConstants.APPLICATION_FIND_BY_STATUS, Application.class)
                .setParameter(STATUS_PARAMETER, status)
                .getResultList();
    }

    @Override
    public List<Application> getAll() {
        return entityManager.createNamedQuery(JpaConstants.APPLICATION_FIND_ALL, Application.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.APPLICATION_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
