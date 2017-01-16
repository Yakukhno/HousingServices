package ua.training.model.dao;

import ua.training.model.entities.Task;

import java.util.List;

public interface TaskDao extends GenericDao<Task> {
    List<Task> getActiveTasks();
}
