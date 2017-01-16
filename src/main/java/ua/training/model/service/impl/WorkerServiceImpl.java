package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.WorkerService;

import java.util.List;

public class WorkerServiceImpl implements WorkerService {

    private WorkerDao workerDao = DaoFactory.getInstance().createWorkerDao();

    @Override
    public Worker getWorkerById(int id) {
        return workerDao.get(id);
    }

    @Override
    public List<Worker> getWorkersByTypeOfWork(TypeOfWork typeOfWork) {
        return workerDao.getWorkersByTypeOfWork(typeOfWork);
    }

    @Override
    public List<Worker> getAllWorkers() {
        return workerDao.getAll();
    }

    @Override
    public void createNewWorker(Worker worker) {
        workerDao.add(worker);
    }
}
