package ua.training.model.service;

import ua.training.model.entities.Application;

import java.util.List;

public interface ApplicationService {
    List<Application> getApplicationsByUserId(int userId);
    List<Application> getAllApplications();
    void createNewApplication(Application application);
    void deleteApplication(int applicationId, int userId);
}
