package ua.training.model.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.BrigadeDao;
import ua.training.model.entities.Brigade;
import ua.training.util.JpaConstants;

@Repository("brigadeDao")
@Transactional
public class JpaBrigadeDao extends AbstractJpaDao<Brigade> implements BrigadeDao {

    private static final String ID_PARAMETER = "id";

    public JpaBrigadeDao() {
        super(Brigade.class);
    }

    @Override
    public List<Brigade> getAll() {
        return entityManager.createNamedQuery(JpaConstants.BRIGADE_FIND_ALL, Brigade.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.BRIGADE_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
