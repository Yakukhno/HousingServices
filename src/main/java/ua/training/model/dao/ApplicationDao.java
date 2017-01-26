package ua.training.model.dao;

import ua.training.model.entities.Application;

import java.util.List;

public interface ApplicationDao extends GenericDao<Application> {
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);
    List<Application> getApplicationsByUserId(int tenantId);
    List<Application> getApplicationsByStatus(Application.Status status);
}
