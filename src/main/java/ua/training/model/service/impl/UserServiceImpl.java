package ua.training.model.service.impl;

import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoException;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    private UserServiceImpl() {}

    private static class InstanceHolder {
        static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Optional<User> getUserById(int id) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.get(id);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getUserByEmail(email);
        }
    }

    @Override
    public Optional<User> loginEmail(String email, String password) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getUserByEmail(email)
                    .filter(user -> password.equals(user.getPassword()));
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
            if (userFromDao.isPresent()) {
                if (userDao.getUserByEmail(user.getEmail())
                        .filter(user1 -> !user1.getEmail()
                                        .equals(user.getPassword())
                        )
                        .isPresent()) {
                    throw new DaoException("This email is already exists");
                }
                if (password.equals(userFromDao.get().getPassword())) {
                    userDao.update(user);
                    connection.commit();
                } else {
                    throw new DaoException("Incorrect password");
                }
            } else {
                throw new DaoException("Invalid user id");
            }
        }
    }

    @Override
    public void createNewUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            connection.begin();
            if (!userDao.getUserByEmail(user.getEmail()).isPresent()) {
                userDao.add(user);
                connection.commit();
            } else {
                throw new DaoException("This email is already exists");
            }
        }
    }
}
