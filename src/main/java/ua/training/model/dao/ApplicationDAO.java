package ua.training.model.dao;

import ua.training.model.entities.Application;

import java.util.List;

public interface ApplicationDAO extends GenericDao<Application> {
    List<Application> getApplicationsByTypeOfWork(String typeOfWork);
    List<Application> getApplicationsByTenant(String typeOfWork);
}
