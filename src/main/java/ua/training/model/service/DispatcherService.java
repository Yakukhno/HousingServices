package ua.training.model.service;

import ua.training.model.entities.person.Dispatcher;

import java.util.List;

public interface DispatcherService {
    Dispatcher getDispatcherById(int id);
    List<Dispatcher> getOnlineDispatchers();
    List<Dispatcher> getAllDispatchers();
    void createNewDispatcher(Dispatcher dispatcher);
}
