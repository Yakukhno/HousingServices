package ua.training.model.service.impl;

import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.WorkerService;

import java.util.List;

public class WorkerServiceImpl implements WorkerService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private WorkerServiceImpl() {}

    private static class InstanceHolder {
        static final WorkerService INSTANCE = new WorkerServiceImpl();
    }

    public static WorkerService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public List<Worker> getAllWorkers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            return workerDao.getAll();
        }
    }
}
