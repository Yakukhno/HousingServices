package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

public class ApplicationServiceImpl implements ApplicationService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND
            = "User with id = %d not found";
    private static final String EXCEPTION_INVALID_ACCESS
            = "Invalid access";
    private static final String EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND
            = "Type of work with id = %d not found";
    private static final String EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND
            = "Application with id = %d not found";

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private Logger logger = Logger.getLogger(ApplicationServiceImpl.class);

    private ApplicationServiceImpl() {}

    private static class InstanceHolder {
        static final ApplicationService INSTANCE = new ApplicationServiceImpl();
    }

    public static ApplicationService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Application getApplicationById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.get(id).orElseThrow(
                    () -> {
                        String message = String.format(
                                EXCEPTION_APPLICATION_WITH_ID_NOT_FOUND, id
                        );
                        ServiceException e = new ServiceException(message);
                        logger.error(message, e);
                        return e;
                    }
            );
        }
    }

    @Override
    public List<Application> getApplicationsByTypeOfWork(String typeOfWork) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByTypeOfWork(typeOfWork);
        }
    }

    @Override
    public List<Application> getApplicationsByUserId(int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByUserId(userId);
        }
    }

    @Override
    public List<Application> getApplicationsByStatus(Application.Status status) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getApplicationsByStatus(status);
        }
    }

    @Override
    public List<Application> getAllApplications() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.getAll();
        }
    }

    @Override
    public void createNewApplication(Application application) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            applicationDao.add(application);
        }
    }

    @Override
    public void createNewApplication(int userId, int typeOfWorkId,
                                     ProblemScale problemScale,
                                     LocalDateTime localDateTime) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            TypeOfWorkDao typeOfWorkDao
                    = daoFactory.createTypeOfWorkDao(connection);
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);

            connection.begin();
            User user = getUser(userDao, userId);
            TypeOfWork typeOfWork = getTypeOfWork(typeOfWorkDao, typeOfWorkId);
            Application application = new Application.Builder()
                    .setTenant(user)
                    .setTypeOfWork(typeOfWork)
                    .setScaleOfProblem(problemScale)
                    .setDesiredTime(localDateTime)
                    .setStatus(Application.Status.NEW)
                    .build();
            applicationDao.add(application);
            connection.commit();
        }
    }

    @Override
    public void deleteApplication(int applicationId, int userId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            connection.begin();
            applicationDao.get(applicationId)
                    .filter(application
                            -> application.getTenant().getId() == userId)
                    .orElseThrow(
                            getServiceExceptionSupplier(EXCEPTION_INVALID_ACCESS)
                    );
            applicationDao.delete(applicationId);
            connection.commit();
        }
    }

    private User getUser(UserDao userDao, int userId) {
        return userDao.get(userId)
                .filter(user -> user.getRole().equals(User.Role.TENANT))
                .orElseThrow(
                        getServiceExceptionSupplier(userId,
                                EXCEPTION_USER_WITH_ID_NOT_FOUND)
                );
    }

    private TypeOfWork getTypeOfWork(TypeOfWorkDao typeOfWorkDao,
                                     int typeOfWorkId) {
        return typeOfWorkDao.get(typeOfWorkId)
                .orElseThrow(
                        getServiceExceptionSupplier(typeOfWorkId,
                                EXCEPTION_TYPE_OF_WORK_WITH_ID_NOT_FOUND)
                );
    }

    private Supplier<ServiceException> getServiceExceptionSupplier(String message) {
        return () -> {
            logger.error(message);
            return new ServiceException(message);
        };
    }

    private Supplier<ServiceException> getServiceExceptionSupplier(int id,
            String messageBlank) {
        return () -> {
            String message = String.format(messageBlank, id);
            logger.error(message);
            return new ServiceException(message);
        };
    }
}
