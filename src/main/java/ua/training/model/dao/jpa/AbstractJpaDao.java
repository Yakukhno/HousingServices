package ua.training.model.dao.jpa;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.GenericDao;

@Transactional
public abstract class AbstractJpaDao<T> implements GenericDao<T> {

    private Class<T> genericType;

    @PersistenceContext
    protected EntityManager entityManager;

    protected AbstractJpaDao(final Class<T> genericType) {
        this.genericType = genericType;
    }

    @Override
    public Optional<T> get(final int id) {
        return Optional.ofNullable(entityManager.find(genericType, id));
    }

    @Override
    public void add(final T t) {
        entityManager.persist(t);
    }

    @Override
    public void update(final T t) {
        entityManager.merge(t);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
