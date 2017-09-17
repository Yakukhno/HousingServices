package ua.training.model.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.training.exception.ResourceNotFoundException;
import ua.training.exception.ServiceException;
import ua.training.model.dao.DaoConnection;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.util.ServiceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TestUserServiceImpl {

    private static final int USER_ID = 3;
    private static final String EMAIL = "a@a.com";
    private static final String NOT_EXISTING_EMAIL = "a1@a.com";
    private static final String PASSWORD = "qwerty";
    private static final String WRONG_PASSWORD = "wrong";
    private static final String NEW_PASSWORD = "newPass";

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private ServiceHelper serviceHelper;
    @Mock
    private UserDao userDao;

    private UserServiceImpl userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(daoFactory.createUserDao(any())).thenReturn(userDao);
        when(serviceHelper.getResourceNotFoundExceptionSupplier(any(), anyInt()))
                .thenReturn(ResourceNotFoundException::new);
        when(serviceHelper.getServiceExceptionSupplier(any()))
                .thenReturn(ServiceException::new);
        userService = new UserServiceImpl(daoFactory);
        userService.setServiceHelper(serviceHelper);
    }

    @Test
    public void testGetUserById() {
        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        when(userDao.get(USER_ID)).thenReturn(Optional.of(userFromDao));

        User actualUser = userService.getUserById(USER_ID);

        verify(userDao, times(1)).get(USER_ID);
        assertEquals(userFromDao, actualUser);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testGetUserByIdDaoReturnsEmpty() {
        when(userDao.get(USER_ID)).thenReturn(Optional.empty());

        userService.getUserById(USER_ID);

        verify(userDao, times(1)).get(USER_ID);
    }

    @Test
    public void testLoginEmail() {
        User userFromDao = new User();
        userFromDao.setEmail(EMAIL);
        userFromDao.setPassword(DigestUtils.sha256Hex(PASSWORD));
        when(userDao.getUserByEmail(EMAIL)).thenReturn(Optional.of(userFromDao));
        User actualUser = userService.loginEmail(EMAIL, PASSWORD);

        verify(userDao, times(1)).getUserByEmail(EMAIL);
        assertEquals(userFromDao, actualUser);
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailDaoReturnsEmpty() {
        when(userDao.getUserByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        userService.loginEmail(NOT_EXISTING_EMAIL, PASSWORD);

        verify(userDao, times(1)).getUserByEmail(NOT_EXISTING_EMAIL);
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailIncorrectPassword() {
        User userFromDao = new User();
        userFromDao.setEmail(EMAIL);
        userFromDao.setPassword(PASSWORD);
        when(userDao.getUserByEmail(EMAIL)).thenReturn(Optional.of(userFromDao));

        userService.loginEmail(EMAIL, WRONG_PASSWORD);
    }

    @Test(expected = ServiceException.class)
    public void testLoginEmailIncorrectEmailAndPassword() {
        when(userDao.getUserByEmail(NOT_EXISTING_EMAIL)).thenReturn(Optional.empty());

        userService.loginEmail(NOT_EXISTING_EMAIL, WRONG_PASSWORD);

        verify(userDao, times(1)).getUserByEmail(NOT_EXISTING_EMAIL);
    }

    @Test
    public void testGetAllUsers() {
        List<User> usersFromDao = new ArrayList<>();
        usersFromDao.add(mock(User.class));
        usersFromDao.add(mock(User.class));
        usersFromDao.add(mock(User.class));
        when(userDao.getAll()).thenReturn(usersFromDao);

        List<User> actualUsers = userService.getAllUsers();

        verify(userDao, times(1)).getAll();
        assertEquals(usersFromDao, actualUsers);
    }

    @Test
    public void testUpdateUser() {
        when(daoFactory.getConnection()).thenReturn(mock(DaoConnection.class));

        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        userFromDao.setPassword(DigestUtils.sha256Hex(PASSWORD));
        when(userDao.get(USER_ID)).thenReturn(Optional.of(userFromDao));

        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(NEW_PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(NEW_PASSWORD));

        userService.updateUser(paramUser, PASSWORD);

        verify(userDao).update(userCopy);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateUserIncorrectPassword() {
        when(daoFactory.getConnection()).thenReturn(mock(DaoConnection.class));

        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        userFromDao.setPassword(PASSWORD);
        when(userDao.get(USER_ID)).thenReturn(Optional.of(userFromDao));

        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(NEW_PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(NEW_PASSWORD));

        userService.updateUser(paramUser, WRONG_PASSWORD);

        verify(userDao, never()).update(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateUserIncorrectUser() {
        when(daoFactory.getConnection()).thenReturn(mock(DaoConnection.class));
        when(userDao.get(USER_ID)).thenReturn(Optional.empty());
        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(NEW_PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(NEW_PASSWORD));

        userService.updateUser(paramUser, PASSWORD);

        verify(userDao, never()).update(any());
    }

    @Test
    public void testCreateUser() {
        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(PASSWORD));

        userService.createNewUser(paramUser);

        verify(userDao, times(1)).add(userCopy);
    }

    private User getUserCopy(User user) {
        return new User.Builder()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setRole(user.getRole())
                .build();
    }
}
