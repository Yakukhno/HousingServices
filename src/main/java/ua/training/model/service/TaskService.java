package ua.training.model.service;

import ua.training.model.entities.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<Task> getTaskById(int id);
    List<Task> getActiveTasks();
    List<Task> getAllTasks();
    void createNewTask(Task task);
    void createNewTask(int applicationId, int managerId,
                       LocalDateTime dateTime, List<Integer> workersIds);
}
