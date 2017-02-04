package ua.training.model.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.exception.ServiceException;
import ua.training.model.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestUserServiceImpl {

    private DaoFactory daoFactory = mock(DaoFactory.class);
    private UserService userService = new UserServiceImpl(daoFactory);

    @Test
    public void testGetUserById() {
        User userFromDao = new User();
        userFromDao.setId(3);
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3)).thenReturn(Optional.of(userFromDao));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);

        User actualUser = userService.getUserById(3);

        assertEquals(userFromDao, actualUser);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByIdDaoReturnsEmpty() {
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3))
                .thenReturn(Optional.empty());
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        userService.getUserById(3);
    }

    @Test
    public void testGetUserByEmail() {
        User userFromDao = new User();
        userFromDao.setEmail("a@a.com");
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3))
                .thenReturn(Optional.of(userFromDao));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        User actualUser = userService.getUserById(3);

        assertEquals(userFromDao, actualUser);
    }

    @Test
    public void testLoginEmail() {
        User userFromDao = new User();
        String email = "a@a.com";
        String password = "qwerty";
        userFromDao.setEmail(email);
        userFromDao.setPassword(DigestUtils.sha256Hex(password));
        UserDao userDao = mock(UserDao.class);
        when(userDao.getUserByEmail(email))
                .thenReturn(Optional.of(userFromDao));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        User actualUser = userService.loginEmail(email, password);

        assertEquals(userFromDao, actualUser);
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailDaoReturnsEmpty() {
        String email = "a1@a.com";
        UserDao userDao = mock(UserDao.class);
        when(userDao.getUserByEmail(email))
                .thenReturn(Optional.empty());
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        userService.loginEmail(email, "qwerty");
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailIncorrectPassword() {
        User userFromDao = new User();
        String email = "a@a.com";
        String password = "qwerty";
        userFromDao.setEmail(email);
        userFromDao.setPassword(password);
        UserDao userDao = mock(UserDao.class);
        when(userDao.getUserByEmail(email))
                .thenReturn(Optional.of(userFromDao));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        userService.loginEmail(email, "wrong");
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailIncorrectEmailAndPassword() {
        String email = "a@a.com";
        UserDao userDao = mock(UserDao.class);
        when(userDao.getUserByEmail(email))
                .thenReturn(Optional.empty());
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        userService.loginEmail(email, "wrong");
    }

    @Test
    public void testGetAllUsers() {
        List<User> usersFromDao = new ArrayList<>();
        usersFromDao.add(mock(User.class));
        usersFromDao.add(mock(User.class));
        usersFromDao.add(mock(User.class));
        UserDao userDao = mock(UserDao.class);
        when(userDao.getAll()).thenReturn(usersFromDao);
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(usersFromDao, actualUsers);
    }

    @Test
    public void testUpdateUser() {
        User userFromDao = new User();
        String password = "qwerty";
        userFromDao.setId(3);
        userFromDao.setPassword(DigestUtils.sha256Hex(password));
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3))
                .thenReturn(Optional.of(userFromDao));
        when(daoFactory.getConnection())
                .thenReturn(mock(DaoConnection.class));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        User paramUser = new User();
        paramUser.setId(3);
        paramUser.setPassword("newPass");

        userService.updateUser(paramUser, password);

        verify(userDao).update(paramUser);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateUserIncorrectPassword() {
        User userFromDao = new User();
        userFromDao.setId(3);
        userFromDao.setPassword("qwerty");
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3))
                .thenReturn(Optional.of(userFromDao));
        when(daoFactory.getConnection())
                .thenReturn(mock(DaoConnection.class));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        User paramUser = new User();
        paramUser.setId(3);
        paramUser.setPassword("newPass");

        userService.updateUser(paramUser, "wrong");

        verify(userDao, never()).update(paramUser);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateUserIncorrectUser() {
        UserDao userDao = mock(UserDao.class);
        when(userDao.get(3))
                .thenReturn(Optional.empty());
        when(daoFactory.getConnection())
                .thenReturn(mock(DaoConnection.class));
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);
        User paramUser = new User();
        paramUser.setId(3);
        paramUser.setPassword("newPass");

        userService.updateUser(paramUser, "qwerty");

        verify(userDao, never()).update(paramUser);
    }

    @Test
    public void testCreateUser() {
        UserDao userDao = mock(UserDao.class);
        User paramUser = new User();
        paramUser.setId(3);
        paramUser.setPassword("qwerty");
        when(daoFactory.createUserDao(any()))
                .thenReturn(userDao);


        userService.createNewUser(paramUser);

        verify(userDao).add(paramUser);
    }
}
