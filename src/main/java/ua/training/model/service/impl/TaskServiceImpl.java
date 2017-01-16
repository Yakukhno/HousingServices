package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.TaskDao;
import ua.training.model.entities.Task;
import ua.training.model.service.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private TaskDao taskDao = DaoFactory.getInstance().createTaskDao();

    @Override
    public Task getTaskById(int id) {
        return taskDao.get(id);
    }

    @Override
    public List<Task> getActiveTasks() {
        return taskDao.getActiveTasks();
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.getAll();
    }

    @Override
    public void createNewTask(Task task) {
        taskDao.add(task);
    }
}
