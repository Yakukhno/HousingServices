package ua.training.model.service;

import ua.training.model.entities.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Optional<Application> getApplicationById(int id);
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);
    List<Application> getApplicationsByUserId(int userId);
    List<Application> getAllApplications();
    void createNewApplication(Application application);
}
