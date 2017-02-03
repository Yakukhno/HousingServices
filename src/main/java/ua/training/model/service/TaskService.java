package ua.training.model.service;

import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<Task> getTaskById(int id);
    List<Task> getActiveTasks();
    List<Task> getAllTasks();
    void createNewTask(Task task);
    void createNewTask(TaskDto taskDto, User.Role role);
}
