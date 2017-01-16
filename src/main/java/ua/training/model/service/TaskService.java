package ua.training.model.service;

import ua.training.model.entities.Task;

import java.util.List;

public interface TaskService {
    Task getTaskById(int id);
    List<Task> getActiveTasks();
    List<Task> getAllTasks();
    void createNewTask(Task task);
}
