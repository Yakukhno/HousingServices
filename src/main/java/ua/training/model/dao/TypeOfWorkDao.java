package ua.training.model.dao;

import java.util.List;

import ua.training.model.entities.TypeOfWork;

public interface TypeOfWorkDao extends GenericDao<TypeOfWork> {
    List<TypeOfWork> getByDescription(String description);
}
