package ua.training.model.service;

import java.util.List;

import ua.training.model.entities.Application;

public interface ApplicationService {
    List<Application> getApplicationsByUserId(int userId);

    List<Application> getAllApplications();

    void createNewApplication(Application application);

    void deleteApplication(int applicationId, int userId);
}
