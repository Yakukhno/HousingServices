package ua.training.model.dao;

import java.util.List;

public interface GenericDao<T> {
    T get(int id);
    List<T> getAll();
    void add(T t);
    void delete(int id);
    void update(T t);
}
