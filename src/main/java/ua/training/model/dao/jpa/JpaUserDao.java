package ua.training.model.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.util.JpaConstants;

@Repository("userDao")
@Transactional
public class JpaUserDao extends AbstractJpaDao<User> implements UserDao {

    private static final String ID_PARAMETER = "id";
    private static final String EMAIL_PARAMETER = "email";
    private static final String ROLE_PARAMETER = "role";

    public JpaUserDao() {
        super(User.class);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        User user = entityManager.createNamedQuery(JpaConstants.USER_FIND_BY_EMAIL, User.class)
                .setParameter(EMAIL_PARAMETER, email)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getUsersByRole(final User.Role role) {
        return entityManager.createNamedQuery(JpaConstants.USER_FIND_BY_ROLE, User.class)
                .setParameter(ROLE_PARAMETER, role)
                .getResultList();
    }

    @Override
    public List<User> getAll() {
        return entityManager.createNamedQuery(JpaConstants.USER_FIND_ALL, User.class).getResultList();
    }

    @Override
    public void delete(final int id) {
        entityManager.createNamedQuery(JpaConstants.USER_DELETE_BY_ID)
                .setParameter(ID_PARAMETER, id)
                .executeUpdate();
    }
}
