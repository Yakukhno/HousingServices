package ua.training.model.service.impl;

import org.apache.log4j.Logger;
import ua.training.exception.ApplicationException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.exception.ServiceException;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class UserServiceImpl implements UserService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND
            = "User with id = %d not found";
    private static final String INCORRECT_EMAIL_OR_PASSWORD
            = "exception.email_password";
    private static final String INCORRECT_PASSWORD = "exception.password";

    private DaoFactory daoFactory;
    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    private UserServiceImpl() {
        daoFactory = DaoFactory.getInstance();
    }

    UserServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    private static class InstanceHolder {
        static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public User getUserById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.get(id)
                    .orElseThrow(
                            getResourceNotFoundExceptionSupplier(
                                    EXCEPTION_USER_WITH_ID_NOT_FOUND, id
                            )
                    );
        }
    }

    @Override
    public User loginEmail(String email, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getUserByEmail(email)
                    .filter(user -> password.equals(user.getPassword()))
                    .orElseThrow(
                            getServiceExceptionSupplier(
                                    INCORRECT_EMAIL_OR_PASSWORD
                            )
                    );
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getAll();
        }
    }

    @Override
    public void updateUser(User user, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);

            connection.begin();
            Optional<User> userFromDao = userDao.get(user.getId());
            userFromDao.orElseThrow(
                    getResourceNotFoundExceptionSupplier(
                            EXCEPTION_USER_WITH_ID_NOT_FOUND, user.getId()
                    )
            );
            userFromDao.filter(user1 -> user1.getPassword().equals(password))
                    .orElseThrow(
                            getServiceExceptionSupplier(INCORRECT_PASSWORD)
                    );
            userDao.update(user);
            connection.commit();
        }
    }

    @Override
    public void createNewUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.add(user);
        }
    }

    private Supplier<ApplicationException>
                    getServiceExceptionSupplier(String message) {
        return () -> {
            ApplicationException e
                    = new ServiceException().setUserMessage(message);
            logger.info(message, e);
            return e;
        };
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
