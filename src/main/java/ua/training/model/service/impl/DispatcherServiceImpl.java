package ua.training.model.service.impl;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.DispatcherDao;
import ua.training.model.entities.person.Dispatcher;
import ua.training.model.service.DispatcherService;

import java.util.List;
import java.util.Optional;

public class DispatcherServiceImpl implements DispatcherService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private DispatcherServiceImpl() {}

    private static class InstanceHolder {
        static final DispatcherService INSTANCE = new DispatcherServiceImpl();
    }

    public static DispatcherService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Dispatcher> getDispatcherById(int id) {
        DispatcherDao dispatcherDao = daoFactory.createDispatcherDao();
        return dispatcherDao.get(id);
    }

    @Override
    public List<Dispatcher> getOnlineDispatchers() {
        DispatcherDao dispatcherDao = daoFactory.createDispatcherDao();
        return dispatcherDao.getOnlineDispatchers();
    }

    @Override
    public List<Dispatcher> getAllDispatchers() {
        DispatcherDao dispatcherDao = daoFactory.createDispatcherDao();
        return dispatcherDao.getAll();
    }

    @Override
    public void createNewDispatcher(Dispatcher dispatcher) {
        DispatcherDao dispatcherDao = daoFactory.createDispatcherDao();
        dispatcherDao.add(dispatcher);
    }
}
