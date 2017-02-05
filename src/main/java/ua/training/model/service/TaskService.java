package ua.training.model.service;

import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.User;

import java.util.List;

public interface TaskService {
    List<Task> getActiveTasks();
    void createNewTask(TaskDto taskDto, User.Role role);
}
