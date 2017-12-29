package ua.training.model.service.impl;

import static ua.training.util.RoleConstants.ROLE_DISPATCHER;
import static ua.training.util.RoleConstants.ROLE_TENANT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.training.exception.AccessForbiddenException;
import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.Application;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.util.ServiceHelper;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND = "User with id = %d not found";
    private static final String EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND = "Type of work with id = %d not found";

    private ApplicationDao applicationDao;
    private UserDao userDao;
    private TypeOfWorkDao typeOfWorkDao;
    private ServiceHelper serviceHelper;

    @Override
    @Secured(ROLE_TENANT)
    public List<Application> getApplicationsByUserId(int userId) {
        return applicationDao.getApplicationsByUserId(userId);
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<Application> getAllApplications() {
        return applicationDao.getAll();
    }

    @Override
    @Secured(ROLE_TENANT)
    @Transactional
    public void createNewApplication(Application application) {
        User user = getUser(application.getUser().getId());
        TypeOfWork typeOfWork = getTypeOfWork(application.getTypeOfWork().getId());
        application.setUser(user);
        application.setTypeOfWork(typeOfWork);
        application.setStatus(Application.Status.NEW);
        applicationDao.add(application);
    }

    @Override
    @Secured(ROLE_TENANT)
    @Transactional
    public void deleteApplication(int applicationId, int userId) {
        applicationDao.get(applicationId)
                .filter(application -> application.getUser().getId() == userId)
                .orElseThrow(AccessForbiddenException::new);
        applicationDao.delete(applicationId);
    }

    private User getUser(int id) {
        return userDao.get(id)
                .orElseThrow(serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND, id));
    }

    private TypeOfWork getTypeOfWork(int id) {
        return typeOfWorkDao.get(id).orElseThrow(
                serviceHelper.getResourceNotFoundExceptionSupplier(EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND, id));
    }

    public ApplicationDao getApplicationDao() {
        return applicationDao;
    }

    @Autowired
    public void setApplicationDao(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public TypeOfWorkDao getTypeOfWorkDao() {
        return typeOfWorkDao;
    }

    @Autowired
    public void setTypeOfWorkDao(TypeOfWorkDao typeOfWorkDao) {
        this.typeOfWorkDao = typeOfWorkDao;
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
