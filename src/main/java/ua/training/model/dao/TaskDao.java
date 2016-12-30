package ua.training.model.dao;

import ua.training.model.entities.Application;
import ua.training.model.entities.Task;

public interface TaskDao extends GenericDao<Task> {
    Task getByApplication(Application application);
}
