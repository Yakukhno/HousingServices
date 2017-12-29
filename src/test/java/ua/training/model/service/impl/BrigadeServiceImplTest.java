package ua.training.model.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.method.P;

import ua.training.exception.ResourceNotFoundException;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.entities.Brigade;
import ua.training.model.util.ServiceHelper;

@RunWith(MockitoJUnitRunner.class)
public class BrigadeServiceImplTest {

    private static final int BRIGADE_ID = 1;

    @Mock
    private BrigadeDao brigadeDaoMock;

    @Mock
    private ServiceHelper serviceHelperMock;

    @InjectMocks
    private BrigadeServiceImpl brigadeService;

    @Test
    public void shouldGetBrigadeById() {
        Brigade brigade = new Brigade();
        when(brigadeDaoMock.get(BRIGADE_ID)).thenReturn(Optional.of(brigade));

        Brigade result = brigadeService.getBrigadeById(BRIGADE_ID);

        verify(brigadeDaoMock).get(BRIGADE_ID);
        assertEquals(result, brigade);
    }

    @Test
    public void shouldThrowExceptionWhenGetBrigadeByIdIfBrigadeIsNotFound() {
        when(brigadeDaoMock.get(BRIGADE_ID)).thenReturn(Optional.empty());
        when(serviceHelperMock.getResourceNotFoundExceptionSupplier(any(), eq(BRIGADE_ID)))
                .thenReturn(ResourceNotFoundException::new);

        try {
            brigadeService.getBrigadeById(BRIGADE_ID);
            fail();
        } catch (ResourceNotFoundException exc) {
            verify(brigadeDaoMock).get(BRIGADE_ID);
        }
    }
}