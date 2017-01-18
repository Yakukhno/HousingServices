package ua.training.model.service;

import ua.training.model.entities.person.Dispatcher;

import java.util.List;
import java.util.Optional;

public interface DispatcherService {
    Optional<Dispatcher> getDispatcherById(int id);
    List<Dispatcher> getOnlineDispatchers();
    List<Dispatcher> getAllDispatchers();
    void createNewDispatcher(Dispatcher dispatcher);
}
