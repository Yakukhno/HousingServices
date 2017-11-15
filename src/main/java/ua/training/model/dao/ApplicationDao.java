package ua.training.model.dao;

import java.util.List;

import ua.training.model.entities.Application;

public interface ApplicationDao extends GenericDao<Application> {
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);

    List<Application> getApplicationsByUserId(int tenantId);

    List<Application> getApplicationsByStatus(Application.Status status);
}
