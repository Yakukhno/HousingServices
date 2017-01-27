package ua.training.model.service.impl;

import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ApplicationServiceImpl implements ApplicationService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private ApplicationServiceImpl() {}

    private static class InstanceHolder {
        static final ApplicationService INSTANCE = new ApplicationServiceImpl();
    }

    public static ApplicationService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<Application> getApplicationById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            ApplicationDao applicationDao
                    = daoFactory.createApplicationDao(connection);
            return applicationDao.get(id);
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

    private User getUser(UserDao userDao, int userId) {
        return userDao.get(userId)
                .filter(user -> user.getRole().equals(User.Role.TENANT))
                .orElseThrow(
                        () -> new ServiceException("Invalid user id")
                );
    }

    private TypeOfWork getTypeOfWork(TypeOfWorkDao typeOfWorkDao,
                                     int typeOfWorkId) {
        return typeOfWorkDao.get(typeOfWorkId)
                .orElseThrow(
                        () -> new ServiceException("Invalid typeOfWork id")
                );
    }
}
