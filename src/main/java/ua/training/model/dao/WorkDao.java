package ua.training.model.dao;

import ua.training.model.entities.Application;
import ua.training.model.entities.Work;

public interface WorkDao extends GenericDao<Work> {
    Work getByApplication(Application application);
}
