package ua.training.model.service;

import ua.training.model.entities.Application;
import ua.training.model.entities.person.User;

import java.util.List;

public interface ApplicationService {
    Application getApplicationById(int id);
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);
    List<Application> getApplicationsByUserId(int userId);
    List<Application> getApplicationsByStatus(Application.Status status);
    List<Application> getAllApplications(User.Role role);
    void createNewApplication(Application application);
    void deleteApplication(int applicationId, int userId);
}
