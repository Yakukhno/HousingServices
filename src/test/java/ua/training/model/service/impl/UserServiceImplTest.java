package ua.training.model.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import ua.training.exception.ResourceNotFoundException;
import ua.training.exception.ServiceException;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.person.User;
import ua.training.model.util.ServiceHelper;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int USER_ID = 3;
    private static final String PASSWORD = "qwerty";
    private static final String WRONG_PASSWORD = "wrong";
    private static final String NEW_PASSWORD = "newPass";

    @Mock
    private UserDao userDaoMock;
    @Mock
    private ServiceHelper serviceHelperMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldGetUserById() {
        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        when(userDaoMock.get(USER_ID)).thenReturn(Optional.of(userFromDao));

        User actualUser = userService.getUserById(USER_ID);

        verify(userDaoMock).get(USER_ID);
        assertEquals(userFromDao, actualUser);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowExceptionWhenGetUserByIdIfUserIsNotFound() {
        when(userDaoMock.get(USER_ID)).thenReturn(Optional.empty());
        when(serviceHelperMock.getResourceNotFoundExceptionSupplier(any(), anyInt()))
                .thenReturn(ResourceNotFoundException::new);

        userService.getUserById(USER_ID);

        verify(userDaoMock).get(USER_ID);
    }

    @Test
    public void shouldGetAllUsers() {
        ArrayList<User> usersFromDao = Lists.newArrayList(mock(User.class), mock(User.class), mock(User.class));
        when(userDaoMock.getAll()).thenReturn(usersFromDao);

        List<User> actualUsers = userService.getAllUsers();

        verify(userDaoMock).getAll();
        assertEquals(usersFromDao, actualUsers);
    }

    @Test
    public void shouldUpdateUser() {
        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        userFromDao.setPassword(DigestUtils.sha256Hex(PASSWORD));

        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(NEW_PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(NEW_PASSWORD));
        when(userDaoMock.get(USER_ID)).thenReturn(Optional.of(userFromDao));

        userService.updateUser(paramUser, PASSWORD);

        verify(userDaoMock).update(userCopy);
    }

    @Test
    public void shouldThrowExceptionWhenUpdateUserIfPasswordIsIncorrect() {
        User userFromDao = new User();
        userFromDao.setId(USER_ID);
        userFromDao.setPassword(PASSWORD);

        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(NEW_PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(NEW_PASSWORD));
        when(userDaoMock.get(USER_ID)).thenReturn(Optional.of(userFromDao));
        when(serviceHelperMock.getServiceExceptionSupplier(any())).thenReturn(ServiceException::new);

        try {
            userService.updateUser(paramUser, WRONG_PASSWORD);
            fail();
        } catch (ServiceException exc) {
            verify(userDaoMock, never()).update(any());
        }
    }

    @Test
    public void shouldThrowExceptionWhenUpdateUserIfUserIsNotFound() {
        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(PASSWORD);
        when(userDaoMock.get(USER_ID)).thenReturn(Optional.empty());
        when(serviceHelperMock.getResourceNotFoundExceptionSupplier(any(), anyInt()))
                .thenReturn(ResourceNotFoundException::new);

        try {
            userService.updateUser(paramUser, PASSWORD);
            fail();
        } catch (ResourceNotFoundException exc) {
            verify(userDaoMock, never()).update(any());
        }
    }

    @Test
    public void shouldCreateUser() {
        User paramUser = new User();
        paramUser.setId(USER_ID);
        paramUser.setPassword(PASSWORD);
        User userCopy = getUserCopy(paramUser);
        userCopy.setPassword(DigestUtils.sha256Hex(PASSWORD));

        userService.createNewUser(paramUser);

        verify(userDaoMock).add(userCopy);
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
