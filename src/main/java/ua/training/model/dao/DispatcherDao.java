package ua.training.model.dao;

import ua.training.model.entities.person.Dispatcher;

import java.util.List;
import java.util.Optional;

public interface DispatcherDao extends GenericDao<Dispatcher> {
    Optional<Dispatcher> getDispatcherByEmail(String email);
    List<Dispatcher> getOnlineDispatchers();
    void setDispatcherOnline(int dispatcherId, boolean isOnline);
}
