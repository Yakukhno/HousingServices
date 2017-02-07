package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.*;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.User;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TaskService;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class TaskServiceImpl implements TaskService {

    private static final String EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND
            = "Application with id = %d not found";
    private static final String EXCEPTION_WORKER_WITH_ID_NOT_FOUND
            = "Worker with id = %d not found";

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
    public List<Task> getActiveTasks() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            return taskDao.getActiveTasks();
        }
    }

    @Override
    public void createNewTask(TaskDto taskDto, User.Role role) {
        if (role.equals(User.Role.DISPATCHER)) {
            try (DaoConnection connection = daoFactory.getConnection()) {
                BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
                WorkerDao workerDao = daoFactory.createWorkerDao(connection);
                TaskDao taskDao = daoFactory.createTaskDao(connection);
                ApplicationDao applicationDao
                        = daoFactory.createApplicationDao(connection);

                connection.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
                connection.begin();
                Worker manager = getWorker(workerDao, taskDto.getManagerId());
                Set<Worker> workers = getWorkers(workerDao,
                        taskDto.getWorkersIds());
                Brigade brigade = getBrigade(manager, workers);
                brigadeDao.add(brigade);

                Application application = getAndUpdateApplication(applicationDao,
                        taskDto.getApplicationId());

                taskDao.add(new Task.Builder()
                        .setBrigade(brigade)
                        .setApplication(application)
                        .setScheduledTime(taskDto.getDateTime())
                        .setActive(true)
                        .build());
                connection.commit();
            }
        } else {
            AccessForbiddenException e = new AccessForbiddenException();
            logger.warn(e.getMessage(), e);
            throw e;
        }
    }

    private Application getAndUpdateApplication(ApplicationDao applicationDao,
                                       int id) {
        Application application = applicationDao.get(id)
                .orElseThrow(
                        getResourceNotFoundExceptionSupplier(
                                EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND, id
                        )
                );
        application.setStatus(Application.Status.CONSIDERED);
        applicationDao.update(application);
        return application;
    }

    private Brigade getBrigade(Worker manager, Set<Worker> workers) {
        workers.removeIf(worker -> worker.equals(manager));
        return new Brigade.Builder()
                .setManager(manager)
                .setWorkers(workers)
                .build();
    }

    private Worker getWorker(WorkerDao workerDao, int id) {
        return workerDao.get(id)
                .orElseThrow(
                        getResourceNotFoundExceptionSupplier(
                                EXCEPTION_WORKER_WITH_ID_NOT_FOUND, id
                        )
                );
    }

    private Set<Worker> getWorkers(WorkerDao workerDao,
                                    List<Integer> workersIds) {
        Set<Worker> workers = new HashSet<>();
        for (int workerId : workersIds) {
            workers.add(getWorker(workerDao, workerId));
        }
        return workers;
    }

    private Supplier<ResourceNotFoundException>
                getResourceNotFoundExceptionSupplier(String blankMessage,
                                                     int id) {
        return () -> {
            ResourceNotFoundException e = new ResourceNotFoundException();
            String message = String.format(blankMessage, id);
            logger.info(message, e);
            return e;
        };
    }
}
