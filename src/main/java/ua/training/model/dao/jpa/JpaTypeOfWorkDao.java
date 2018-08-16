package ua.training.model.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.util.JpaConstants;

@Repository("typeOfWorkDao")
@Transactional
public class JpaTypeOfWorkDao extends AbstractJpaDao<TypeOfWork> implements TypeOfWorkDao {

    private static final String ID_PARAMETER = "id";
    private static final String DESCRIPTION_PARAMETER = "description";

    public JpaTypeOfWorkDao() {
        super(TypeOfWork.class);
    }

    @Override
    public List<TypeOfWork> getByDescription(final String description) {
        return entityManager.createNamedQuery(JpaConstants.TYPE_OF_WORK_FIND_BY_DESCRIPTION, TypeOfWork.class)
                .setParameter(DESCRIPTION_PARAMETER, description)
                .getResultList();
    }

    @Override
    public List<TypeOfWork> getAll() {
        return entityManager.createNamedQuery(JpaConstants.TYPE_OF_WORK_FIND_ALL, TypeOfWork.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.TYPE_OF_WORK_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
