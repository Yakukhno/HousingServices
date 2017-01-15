package ua.training.model.dao;

import ua.training.model.entities.person.Dispatcher;

import java.util.List;

public interface DispatcherDao extends GenericDao<Dispatcher> {
    List<Dispatcher> getOnlineDispatchers();
}
