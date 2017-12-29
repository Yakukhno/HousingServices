package ua.training.model.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.dao.UserDao;
import ua.training.model.entities.Application;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.util.ServiceHelper;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceImplTest {

    private static final int USER_ID = 3;
    private static final int WRONG_USER_ID = -1;
    private static final int APPLICATION_ID = 101;
    private static final int TYPE_OF_WORK_ID = 98;

    @Mock
    private ApplicationDao applicationDaoMock;
    @Mock
    private UserDao userDaoMock;
    @Mock
    private TypeOfWorkDao typeOfWorkDaoMock;
    @Mock
    private ServiceHelper serviceHelperMock;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    public void shouldGetApplicationsByUserId() {
        List<Application> applications = Lists.newArrayList(new Application(), new Application());
        when(applicationDaoMock.getApplicationsByUserId(USER_ID)).thenReturn(applications);

        List<Application> result = applicationService.getApplicationsByUserId(USER_ID);

        verify(applicationDaoMock).getApplicationsByUserId(USER_ID);
        assertEquals(applications, result);
    }

    @Test
    public void shouldGetAllApplications() {
        List<Application> applications = Lists.newArrayList(new Application(), new Application(), new Application());
        when(applicationDaoMock.getAll()).thenReturn(applications);

        List<Application> result = applicationService.getAllApplications();

        verify(applicationDaoMock).getAll();
        assertEquals(applications, result);
    }

    @Test
    public void shouldCreateNewApplication() {
        Application application = prepareApplication(USER_ID);

        applicationService.createNewApplication(application);

        verify(applicationDaoMock).add(application);
        assertEquals(Application.Status.NEW, application.getStatus());
    }

    @Test
    public void shouldThrowExceptionWhenCreateNewApplicationIfUserIsNotFound() {
        Application application = prepareApplication(WRONG_USER_ID);
        when(serviceHelperMock.getResourceNotFoundExceptionSupplier(any(), eq(WRONG_USER_ID)))
                .thenReturn(ResourceNotFoundException::new);

        try {
            applicationService.createNewApplication(application);
            fail();
        } catch (ResourceNotFoundException exc) {
            verify(userDaoMock).get(WRONG_USER_ID);
            verify(applicationDaoMock, never()).add(application);
        }
    }

    private Application prepareApplication(int userId) {
        User userFromDao = (userId == WRONG_USER_ID) ? null : createUser(userId);
        when(userDaoMock.get(userId)).thenReturn(Optional.ofNullable(userFromDao));

        TypeOfWork typeOfWork = createTypeOfWork();
        when(typeOfWorkDaoMock.get(TYPE_OF_WORK_ID)).thenReturn(Optional.of(typeOfWork));

        Application application = new Application();
        application.setUser(createUser(userId));
        application.setTypeOfWork(typeOfWork);
        return application;
    }

    private User createUser(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private TypeOfWork createTypeOfWork() {
        TypeOfWork typeOfWork = new TypeOfWork();
        typeOfWork.setId(TYPE_OF_WORK_ID);
        return typeOfWork;
    }

    @Test
    public void shouldDeleteApplication() {
        Application application = new Application();
        User user = new User();
        user.setId(USER_ID);
        application.setUser(user);
        when(applicationDaoMock.get(APPLICATION_ID)).thenReturn(Optional.of(application));

        applicationService.deleteApplication(APPLICATION_ID, USER_ID);

        verify(applicationDaoMock).delete(APPLICATION_ID);
    }

    @Test
    public void shouldThrowExceptionWhenDeleteApplicationIfApplicationDoesNotBelongToUser() {
        Application application = new Application();
        User user = new User();
        user.setId(WRONG_USER_ID);
        application.setUser(user);
        when(applicationDaoMock.get(APPLICATION_ID)).thenReturn(Optional.of(application));

        try {
            applicationService.deleteApplication(APPLICATION_ID, USER_ID);
            fail();
        } catch (AccessForbiddenException exc) {
            verify(applicationDaoMock, never()).delete(anyInt());
        }
    }
}