package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;

import java.util.List;
import java.util.function.Supplier;

import static ua.training.controller.util.Roles.ROLE_DISPATCHER;
import static ua.training.controller.util.Roles.ROLE_TENANT;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND
            = "User with id = %d not found";
    private static final String EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND
            = "Type of work with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private Logger logger = Logger.getLogger(ApplicationServiceImpl.class);

    @Override
    @Secured(ROLE_TENANT)
    public List<Application> getApplicationsByUserId(int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByUserId(userId);
        }
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<Application> getAllApplications() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getAll();
        }
    }

    @Override
    @Secured(ROLE_TENANT)
    public void createNewApplication(Application application) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);

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
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);

            connection.begin();
            applicationDao.get(applicationId)
                    .filter(application
                            -> application.getUser().getId() == userId)
                    .orElseThrow(
                            () -> {
                                AccessForbiddenException e
                                        = new AccessForbiddenException();
                                logger.info(e.getMessage(), e);
                                return e;
                            }
                    );
            applicationDao.delete(applicationId);
            connection.commit();
        }
    }

    private User getUser(UserDao userDao, int id) {
        return userDao.get(id)
                .filter(user -> user.getRole().equals(User.Role.TENANT))
                .orElseThrow(
                        getResourceNotFoundExceptionSupplier(
                                EXCEPTION_USER_WITH_ID_NOT_FOUND, id
                        )
                );
    }

    private TypeOfWork getTypeOfWork(TypeOfWorkDao typeOfWorkDao,
                                     int id) {
        return typeOfWorkDao.get(id)
                .orElseThrow(
                        getResourceNotFoundExceptionSupplier(
                                EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND, id
                        )
                );
    }

    private Supplier<ResourceNotFoundException>
                getResourceNotFoundExceptionSupplier(String blankMessage,
                                                     int id) {
        return () -> {
            ResourceNotFoundException e = new ResourceNotFoundException();
            String message = String.format(blankMessage, id);
            logger.info(message, e);
            return e;
        };
    }
}
