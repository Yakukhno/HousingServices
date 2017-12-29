package ua.training.model.service.impl;

import static ua.training.util.RoleConstants.ROLE_DISPATCHER;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.BrigadeDao;
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

    private TaskDao taskDao;
    private ApplicationDao applicationDao;
    private WorkerDao workerDao;
    private BrigadeDao brigadeDao;

    private ServiceHelper serviceHelper;

    @Override
    public List<Task> getActiveTasks() {
        return taskDao.getActiveTasks();
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    @Transactional
    public void createNewTask(TaskDto taskDto) {
        Worker manager = getWorker(taskDto.getManagerId());
        Set<Worker> workers = getWorkers(taskDto.getWorkersIds());
        Brigade brigade = getBrigade(manager, workers);
        brigadeDao.add(brigade);

        Application application = getAndUpdateApplication(taskDto.getApplicationId());

        taskDao.add(new Task.Builder()
                .setBrigade(brigade)
                .setApplication(application)
                .setScheduledTime(taskDto.getDateTime())
                .setActive(true)
                .build());
    }

    private Application getAndUpdateApplication(int id) {
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

    private Worker getWorker(int id) {
        return workerDao.get(id).orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_WORKER_WITH_ID_NOT_FOUND, id));
    }

    private Set<Worker> getWorkers(List<Integer> workersIds) {
        return workersIds.stream().map(this::getWorker).collect(Collectors.toSet());
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    @Autowired
    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public ApplicationDao getApplicationDao() {
        return applicationDao;
    }

    @Autowired
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    public WorkerDao getWorkerDao() {
        return workerDao;
    }

    @Autowired
    public void setWorkerDao(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }

    public BrigadeDao getBrigadeDao() {
        return brigadeDao;
    }

    @Autowired
    public void setBrigadeDao(BrigadeDao brigadeDao) {
        this.brigadeDao = brigadeDao;
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
