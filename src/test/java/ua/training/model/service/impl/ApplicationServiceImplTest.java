package ua.training.model.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.*;
import ua.training.model.entities.Application;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.util.ServiceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ApplicationServiceImplTest {

    private static final int USER_ID = 3;
    private static final int WRONG_USER_ID = -1;
    private static final int APPLICATION_ID = 101;
    private static final int TYPE_OF_WORK_ID = 98;

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private ApplicationDao applicationDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TypeOfWorkDao typeOfWorkDao;
    @Mock
    private ServiceHelper serviceHelper;
    @Mock
    private DaoConnection daoConnection;

    private ApplicationServiceImpl applicationService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(daoFactory.getConnection()).thenReturn(daoConnection);
        when(daoFactory.createApplicationDao(daoConnection)).thenReturn(applicationDao);
        when(serviceHelper.getResourceNotFoundExceptionSupplier(any(), anyInt()))
                .thenReturn(ResourceNotFoundException::new);
        applicationService = new ApplicationServiceImpl(daoFactory);
        applicationService.setServiceHelper(serviceHelper);
    }

    @Test
    public void getApplicationsByUserId() throws Exception {
        List<Application> applications = new ArrayList<>(2);
        Collections.fill(applications, mock(Application.class));
        when(applicationDao.getApplicationsByUserId(USER_ID)).thenReturn(applications);

        List<Application> applicationsFromService
                = applicationService.getApplicationsByUserId(USER_ID);

        verify(applicationDao, times(1)).getApplicationsByUserId(USER_ID);
        assertEquals(applications, applicationsFromService);
    }

    @Test
    public void getAllApplications() throws Exception {
        List<Application> applications = new ArrayList<>(4);
        Collections.fill(applications, mock(Application.class));
        when(applicationDao.getAll()).thenReturn(applications);

        List<Application> applicationsFromService = applicationService.getAllApplications();

        verify(applicationDao, times(1)).getAll();
        assertEquals(applications, applicationsFromService);
    }

    @Test
    public void createNewApplication() throws Exception {
        createNewApplication(USER_ID, User.Role.TENANT);

        verify(applicationDao, times(1)).add(any());
    }

    @Test(expected = AccessForbiddenException.class)
    public void createNewApplicationByDispatcher() throws Exception {
        createNewApplication(USER_ID, User.Role.DISPATCHER);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void createNewApplicationByNotExistingUser() throws Exception {
        createNewApplication(WRONG_USER_ID, User.Role.TENANT);
    }

    private void createNewApplication(int userId, User.Role role) {
        when(daoFactory.createUserDao(daoConnection)).thenReturn(userDao);
        when(daoFactory.createTypeOfWorkDao(daoConnection)).thenReturn(typeOfWorkDao);
        User userFromDao = (userId == WRONG_USER_ID) ? null : createUser(userId, role);
        when(userDao.get(userId)).thenReturn(Optional.ofNullable(userFromDao));

        TypeOfWork typeOfWork = createTypeOfWork();
        when(typeOfWorkDao.get(TYPE_OF_WORK_ID)).thenReturn(Optional.of(typeOfWork));

        Application application = new Application();
        application.setUser(createUser(userId));
        application.setTypeOfWork(typeOfWork);

        applicationService.createNewApplication(application);
    }

    private User createUser(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private User createUser(int id, User.Role role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        return user;
    }

    private TypeOfWork createTypeOfWork() {
        TypeOfWork typeOfWork = new TypeOfWork();
        typeOfWork.setId(TYPE_OF_WORK_ID);
        return typeOfWork;
    }

    @Test
    public void deleteApplication() throws Exception {
        prepareAndInvokeDeleteApplication(USER_ID);

        verify(applicationDao, times(1)).delete(APPLICATION_ID);
    }

    @Test(expected = AccessForbiddenException.class)
    public void deleteApplicationByWrongUser() throws Exception {
        prepareAndInvokeDeleteApplication(WRONG_USER_ID);

        verify(applicationDao, never()).delete(APPLICATION_ID);
    }

    private void prepareAndInvokeDeleteApplication(int userId) {
        Application application = new Application();
        application.setId(APPLICATION_ID);
        application.setUser(createUser(userId));
        when(applicationDao.get(APPLICATION_ID)).thenReturn(Optional.of(application));

        applicationService.deleteApplication(APPLICATION_ID, USER_ID);
    }
}