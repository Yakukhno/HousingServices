package ua.training.model.service;

import ua.training.model.entities.person.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {
    Optional<Worker> getWorkerById(int id);
    List<Worker> getWorkersByTypeOfWork(int typeOfWorkId);
    List<Worker> getAllWorkers();
    void createNewWorker(Worker worker);
}
