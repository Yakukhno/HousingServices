package ua.training.model.service;

import java.util.List;

import ua.training.model.entities.person.Worker;

public interface WorkerService {
    List<Worker> getAllWorkers();

    void addNewWorker(Worker worker);
}
