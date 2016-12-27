package ua.training.model.dao;

import ua.training.model.entities.TypeOfWork;

import java.util.List;

public interface TypeOfWorkDao extends GenericDao<TypeOfWork> {
    List<TypeOfWork> getByDescription(String string);
}
