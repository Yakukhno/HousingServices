package ua.training.model.service.impl;

import static ua.training.util.RoleConstants.ROLE_DISPATCHER;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TaskDao;
import ua.training.model.dao.WorkerDao;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TaskService;
import ua.training.model.util.ServiceHelper;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private static final String EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND = "Application with id = %d not found";
    private static final String EXCEPTION_WORKER_WITH_ID_NOT_FOUND = "Worker with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private ServiceHelper serviceHelper;

    @Override
    public List<Task> getActiveTasks() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            return taskDao.getActiveTasks();
        }
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public void createNewTask(TaskDto taskDto) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            BrigadeDao brigadeDao = daoFactory.createBrigadeDao(connection);
            WorkerDao workerDao = daoFactory.createWorkerDao(connection);
            TaskDao taskDao = daoFactory.createTaskDao(connection);
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);

            connection.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
            connection.begin();

            Worker manager = getWorker(workerDao, taskDto.getManagerId());
            Set<Worker> workers = getWorkers(workerDao, taskDto.getWorkersIds());
            Brigade brigade = getBrigade(manager, workers);
            brigadeDao.add(brigade);

            Application application = getAndUpdateApplication(applicationDao, taskDto.getApplicationId());

            taskDao.add(new Task.Builder()
                    .setBrigade(brigade)
                    .setApplication(application)
                    .setScheduledTime(taskDto.getDateTime())
                    .setActive(true)
                    .build());

            connection.commit();
        }
    }

    private Application getAndUpdateApplication(ApplicationDao applicationDao, int id) {
        Application application = applicationDao.get(id).orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND, id));
        application.setStatus(Application.Status.CONSIDERED);
        applicationDao.update(application);
        return application;
    }

    private Brigade getBrigade(Worker manager, Set<Worker> workers) {
        workers.removeIf(manager::equals);
        return new Brigade.Builder()
                .setManager(manager)
                .setWorkers(workers)
                .build();
    }

    private Worker getWorker(WorkerDao workerDao, int id) {
        return workerDao.get(id).orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_WORKER_WITH_ID_NOT_FOUND, id));
    }

    private Set<Worker> getWorkers(WorkerDao workerDao, List<Integer> workersIds) {
        return workersIds.stream().map(workerId -> getWorker(workerDao, workerId)).collect(Collectors.toSet());
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
