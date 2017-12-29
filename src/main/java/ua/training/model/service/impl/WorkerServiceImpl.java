package ua.training.model.service.impl;

import static ua.training.util.RoleConstants.ROLE_DISPATCHER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.WorkerService;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService {

    private WorkerDao workerDao;

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<Worker> getAllWorkers() {
        return workerDao.getAll();
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public void addNewWorker(Worker worker) {
        workerDao.add(worker);
    }

    public WorkerDao getWorkerDao() {
        return workerDao;
    }

    @Autowired
    public void setWorkerDao(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }
}
