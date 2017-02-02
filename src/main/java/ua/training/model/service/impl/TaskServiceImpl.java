package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import ua.training.model.dao.*;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.ServiceException;
import ua.training.model.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    private static final String EXCEPTION_INVALID_APPLICATION_ID
            = "Application with id = %d doesn't exist";

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private Logger logger = Logger.getLogger(TaskServiceImpl.class);

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
    public void createNewTask(TaskDto taskDto) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);

            connection.begin();
            Worker manager = getWorker(workerDao, taskDto.getManagerId());
            List<Worker> workers = getWorkers(workerDao, taskDto.getWorkersIds());
            Brigade brigade = getBrigade(manager, workers);
            brigadeDao.add(brigade);

            Application application = getApplication(applicationDao,
                    taskDto.getApplicationId());
            applicationDao.update(application);

            taskDao.add(new Task.Builder()
                    .setBrigade(brigade)
                    .setApplication(application)
                    .setScheduledTime(taskDto.getDateTime())
                    .setActive(true)
                    .build());
            connection.commit();
        }
    }

    private Application getApplication(ApplicationDao applicationDao,
                                       int applicationId) {
        Application application = applicationDao.get(applicationId)
                .orElseThrow(
                        () -> new ServiceException("Invalid application id")
                );
        application.setStatus(Application.Status.CONSIDERED);
        return application;
    }

    private Brigade getBrigade(Worker manager, List<Worker> workers) {
        workers.removeIf(worker -> worker.equals(manager));
        return new Brigade.Builder()
                .setManager(manager)
                .setWorkers(workers)
                .build();
    }

    private Worker getWorker(WorkerDao workerDao, int managerId) {
        return workerDao.get(managerId)
                .orElseThrow(() -> {
            String message = String.format(EXCEPTION_INVALID_APPLICATION_ID,
                    managerId);
            logger.error(message);
            return new ServiceException(message);
        });
    }

    private List<Worker> getWorkers(WorkerDao workerDao,
                                    List<Integer> workersIds) {
        List<Worker> workers = new ArrayList<>();
        for (int workerId : workersIds) {
            workers.add(getWorker(workerDao, workerId));
        }
        return workers;
    }
}
