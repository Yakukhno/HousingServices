package ua.training.model.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;
import ua.training.model.util.ServiceHelper;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static ua.training.controller.util.Roles.ROLE_DISPATCHER;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String EXCEPTION_USER_WITH_ID_NOT_FOUND = "User with id = %d not found";
    private static final String INCORRECT_EMAIL_OR_PASSWORD = "exception.email_password";
    private static final String INCORRECT_PASSWORD = "exception.password";
    private static final int PASS_MAX_LENGTH = 64;

    private DaoFactory daoFactory;
    private ServiceHelper serviceHelper;

    public UserServiceImpl() {
        daoFactory = DaoFactory.getInstance();
    }

    UserServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    @Secured("IS_AUTHENTICATED_FULLY")
    @PreAuthorize("#id == principal.user.id")
    public User getUserById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.get(id).orElseThrow(serviceHelper
                    .getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND, id));
        }
    }

    @Override
    public User loginEmail(String email, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            String hashedPassword = DigestUtils.sha256Hex(password);
            return userDao.getUserByEmail(email)
                    .filter(user -> hashedPassword.equals(user.getPassword()))
                    .orElseThrow(
                            serviceHelper.getServiceExceptionSupplier(INCORRECT_EMAIL_OR_PASSWORD));
        }
    }

    @Override
    @Secured(ROLE_DISPATCHER)
    public List<User> getAllUsers() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getAll();
        }
    }

    @Override
    @Secured("IS_AUTHENTICATED_FULLY")
    @PreAuthorize("#user.id == principal.user.id")
    public void updateUser(User user, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            String hashedPassword = DigestUtils.sha256Hex(password);
            hashNewPassword(user);

            connection.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
            connection.begin();
            Optional<User> userFromDao = userDao.get(user.getId());
            userFromDao.orElseThrow(serviceHelper
                    .getResourceNotFoundExceptionSupplier(EXCEPTION_USER_WITH_ID_NOT_FOUND,
                            user.getId()));
            userFromDao.filter(user1 -> hashedPassword.equals(user1.getPassword()))
                    .orElseThrow(serviceHelper.getServiceExceptionSupplier(INCORRECT_PASSWORD));
            userDao.update(user);
            connection.commit();
        }
    }

    private void hashNewPassword(User user) {
        if (user.getPassword().length() < PASS_MAX_LENGTH) {
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        }
    }

    @Override
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public void createNewUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
            userDao.add(user);
        }
    }

    public ServiceHelper getServiceHelper() {
        return serviceHelper;
    }

    @Autowired
    public void setServiceHelper(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }
}
