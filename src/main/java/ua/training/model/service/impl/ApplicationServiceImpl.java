package ua.training.model.service.impl;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Application;
import ua.training.model.service.ApplicationService;

import java.util.List;
import java.util.Optional;

public class ApplicationServiceImpl implements ApplicationService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private ApplicationServiceImpl() {}

    private static class InstanceHolder {
        static final ApplicationService INSTANCE = new ApplicationServiceImpl();
    }

    public static ApplicationService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Application> getApplicationById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.get(id);
        }
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByTypeOfWork(typeOfWork);
        }
    }

    @Override
    public List<Application> getApplicationsByTenantId(int tenantId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByTenantId(tenantId);
        }
    }

    @Override
    public List<Application> getAllApplications() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getAll();
        }
    }

    @Override
    public void createNewApplication(Application application) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            applicationDao.add(application);
        }
    }
}
