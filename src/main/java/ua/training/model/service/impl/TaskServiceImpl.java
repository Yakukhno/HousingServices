package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;
import ua.training.model.service.TaskService;

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
        TaskDao taskDao = daoFactory.createTaskDao();
        return taskDao.get(id);
    }

    @Override
    public List<Task> getActiveTasks() {
        TaskDao taskDao = daoFactory.createTaskDao();
        return taskDao.getActiveTasks();
    }

    @Override
    public List<Task> getAllTasks() {
        TaskDao taskDao = daoFactory.createTaskDao();
        return taskDao.getAll();
    }

    @Override
    public void createNewTask(Task task) {
        TaskDao taskDao = daoFactory.createTaskDao();
        taskDao.add(task);
    }
}
