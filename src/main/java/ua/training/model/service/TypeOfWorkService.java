package ua.training.model.service;

import ua.training.model.entities.TypeOfWork;

import java.util.List;
import java.util.Optional;

public interface TypeOfWorkService {
    Optional<TypeOfWork> getTypeOfWorkById(int id);
    List<TypeOfWork> getTypeOfWorkByDescription(String string);
    List<TypeOfWork> getAllTypesOfWork();
    void createNewTypeOfWork(TypeOfWork typeOfWork);
}
