package ua.training.model.dao;

import java.util.List;

import ua.training.model.entities.Task;

public interface TaskDao extends GenericDao<Task> {
    List<Task> getActiveTasks();
}
