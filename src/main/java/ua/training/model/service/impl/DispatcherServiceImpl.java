package ua.training.model.service.impl;

import ua.training.model.dao.DaoConnection;
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
        try (DaoConnection connection = daoFactory.getConnection()) {
            DispatcherDao dispatcherDao
                    = daoFactory.createDispatcherDao(connection);
            return dispatcherDao.get(id);
        }
    }

    @Override
    public List<Dispatcher> getOnlineDispatchers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DispatcherDao dispatcherDao
                    = daoFactory.createDispatcherDao(connection);
            return dispatcherDao.getOnlineDispatchers();
        }
    }

    @Override
    public List<Dispatcher> getAllDispatchers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DispatcherDao dispatcherDao
                    = daoFactory.createDispatcherDao(connection);
            return dispatcherDao.getAll();
        }
    }

    @Override
    public void setOffline(int dispatcherId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            daoFactory.createDispatcherDao(connection)
                    .setDispatcherOnline(dispatcherId, false);
        }
    }

    @Override
    public void createNewDispatcher(Dispatcher dispatcher) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DispatcherDao dispatcherDao
                    = daoFactory.createDispatcherDao(connection);
            dispatcherDao.add(dispatcher);
        }
    }
}
