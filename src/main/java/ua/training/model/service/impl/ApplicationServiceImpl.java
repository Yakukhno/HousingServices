package ua.training.model.service.impl;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.entities.Application;
import ua.training.model.service.ApplicationService;

import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationDao applicationDao = DaoFactory
            .getInstance().createApplicationDao();

    @Override
    public Application getApplicationById(int id) {
        return applicationDao.get(id);
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        return applicationDao.getApplicationsByTypeOfWork(typeOfWork);
    }

    @Override
    public List<Application> getApplicationsByTenantId(int tenantId) {
        return applicationDao.getApplicationsByTenantId(tenantId);
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationDao.getAll();
    }

    @Override
    public void createNewApplication(Application application) {
        applicationDao.add(application);
    }
}
