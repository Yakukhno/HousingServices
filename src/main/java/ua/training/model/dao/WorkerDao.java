package ua.training.model.dao;

import java.util.List;

import ua.training.model.entities.person.Worker;

public interface WorkerDao extends GenericDao<Worker> {
    List<Worker> getWorkersByTypeOfWork(int typeOfWorkId);
}
