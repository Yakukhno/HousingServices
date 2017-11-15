package ua.training.model.service;

import java.util.List;

import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Task;

public interface TaskService {
    List<Task> getActiveTasks();

    void createNewTask(TaskDto taskDto);
}
