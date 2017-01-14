package ua.training.model.dao;

import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Worker;

import java.util.List;

public interface WorkerDao extends GenericDao<Worker> {
    List<Worker> getWorkersByTypeOfWork(TypeOfWork typeOfWork);
}
