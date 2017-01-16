package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.DispatcherDao;
import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;

import java.util.List;

public class DispatcherServiceImpl implements DispatcherService {

    private DispatcherDao dispatcherDao = DaoFactory
            .getInstance().createDispatcherDao();

    @Override
    public Dispatcher getDispatcherById(int id) {
        return dispatcherDao.get(id);
    }

    @Override
    public List<Dispatcher> getOnlineDispatchers() {
        return dispatcherDao.getOnlineDispatchers();
    }

    @Override
    public List<Dispatcher> getAllDispatchers() {
        return dispatcherDao.getAll();
    }

    @Override
    public void createNewDispatcher(Dispatcher dispatcher) {
        dispatcherDao.add(dispatcher);
    }
}
