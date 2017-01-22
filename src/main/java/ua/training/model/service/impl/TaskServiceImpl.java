package ua.training.model.service.impl;

import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private TaskServiceImpl() {}

    private static class InstanceHolder {
        static final TaskService INSTANCE = new TaskServiceImpl();
    }

    public static TaskService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            return taskDao.get(id);
        }
    }

    @Override
    public List<Task> getActiveTasks() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            return taskDao.getActiveTasks();
        }
    }

    @Override
    public List<Task> getAllTasks() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            return taskDao.getAll();
        }
    }

    @Override
    public void createNewTask(Task task) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            taskDao.add(task);
        }
    }

    @Override
    public void createNewTask(int applicationId, int managerId, List<Integer> workersIds) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.begin();
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);
            TaskDao taskDao = daoFactory.createTaskDao(connection);

            Worker manager = workerDao.get(managerId)
                    .orElseThrow(
                            () -> new RuntimeException("Invalid worker id")
                    );
            List<Worker> workers = new ArrayList<>();
            for (int workerId : workersIds) {
                workers.add(workerDao.get(workerId)
                        .orElseThrow(
                                () -> new RuntimeException("Invalid worker id")
                        ));
            }
            Brigade brigade = new Brigade.Builder()
                    .setManager(manager)
                    .setWorkers(workers)
                    .build();
            brigadeDao.add(brigade);

            Application application = applicationDao.get(applicationId)
                    .orElseThrow(
                            () -> new RuntimeException("Invalid worker id")
                    );

            taskDao.add(new Task.Builder()
                    .setBrigade(brigade)
                    .setApplication(application)
                    .build());
            connection.commit();
        }
    }
}
