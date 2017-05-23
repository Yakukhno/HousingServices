package ua.training.model.service.impl;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.WorkerService;

import java.util.List;

import static ua.training.controller.Roles.ROLE_DISPATCHER;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<Worker> getAllWorkers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            return workerDao.getAll();
        }
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public void addNewWorker(Worker worker) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            workerDao.add(worker);
        }
    }
}
