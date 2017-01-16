package ua.training.model.service;

import ua.training.model.entities.Application;

import java.util.List;

public interface ApplicationService {
    Application getApplicationById(int id);
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);
    List<Application> getApplicationsByTenantId(int tenantId);
    List<Application> getAllApplications();
    void createNewApplication(Application application);
}
