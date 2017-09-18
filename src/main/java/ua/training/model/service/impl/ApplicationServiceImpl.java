package ua.training.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ua.training.exception.AccessForbiddenException;
import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.util.ServiceHelper;

import java.util.List;

import static ua.training.controller.util.Roles.ROLE_DISPATCHER;
import static ua.training.controller.util.Roles.ROLE_TENANT;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND = "User with id = %d not found";
    private static final String EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND
            = "Type of work with id = %d not found";

    private ServiceHelper serviceHelper;

    private DaoFactory daoFactory;

    public ApplicationServiceImpl() {
        daoFactory = DaoFactory.getInstance();
    }

    ApplicationServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    @Secured(ROLE_TENANT)
    public List<Application> getApplicationsByUserId(int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByUserId(userId);
        }
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<Application> getAllApplications() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);
            return applicationDao.getAll();
        }
    }

    @Override
    @Secured(ROLE_TENANT)
    public void createNewApplication(Application application) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            TypeOfWorkDao typeOfWorkDao = daoFactory.createTypeOfWorkDao(connection);
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);

            connection.begin();
            User user = getUser(userDao, application.getUser().getId());
            TypeOfWork typeOfWork = getTypeOfWork(typeOfWorkDao,
                    application.getTypeOfWork().getId());
            application.setUser(user);
            application.setTypeOfWork(typeOfWork);
            application.setStatus(Application.Status.NEW);
            applicationDao.add(application);
            connection.commit();
        }
    }

    @Override
    @Secured(ROLE_TENANT)
    public void deleteApplication(int applicationId, int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao = daoFactory.createApplicationDao(connection);

            connection.begin();
            applicationDao.get(applicationId)
                    .filter(application -> application.getUser().getId() == userId)
                    .orElseThrow(AccessForbiddenException::new);
            applicationDao.delete(applicationId);
            connection.commit();
        }
    }

    private User getUser(UserDao userDao, int id) {
        User user = userDao.get(id).orElseThrow(serviceHelper
                .getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND, id));

        if (!user.getRole().equals(User.Role.TENANT)) {
            throw new AccessForbiddenException();
        }

        return user;
    }

    private TypeOfWork getTypeOfWork(TypeOfWorkDao typeOfWorkDao, int id) {
        return typeOfWorkDao.get(id)
                .orElseThrow(serviceHelper.getResourceNotFoundExceptionSupplier(
                        EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND, id));
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
