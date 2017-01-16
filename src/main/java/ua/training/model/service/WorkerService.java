package ua.training.model.service;

import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Worker;

import java.util.List;

public interface WorkerService {
    Worker getWorkerById(int id);
    List<Worker> getWorkersByTypeOfWork(TypeOfWork typeOfWork);
    List<Worker> getAllWorkers();
    void createNewWorker(Worker worker);
}
