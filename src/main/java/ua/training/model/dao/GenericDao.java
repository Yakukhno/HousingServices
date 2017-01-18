package ua.training.model.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    Optional<T> get(int id);
    List<T> getAll();
    void add(T t);
    void delete(int id);
    void update(T t);
}
