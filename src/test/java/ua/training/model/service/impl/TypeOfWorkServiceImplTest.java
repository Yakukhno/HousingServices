package ua.training.model.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import ua.training.model.dao.TypeOfWorkDao;
import ua.training.model.entities.TypeOfWork;

@RunWith(MockitoJUnitRunner.class)
public class TypeOfWorkServiceImplTest {

    @Mock
    private TypeOfWorkDao typeOfWorkDaoMock;

    @InjectMocks
    private TypeOfWorkServiceImpl typeOfWorkService;

    @Test
    public void shouldGetAllTypesOfWork() {
        List<TypeOfWork> typesOfWork = Lists.newArrayList(new TypeOfWork(), new TypeOfWork(), new TypeOfWork());
        when(typeOfWorkDaoMock.getAll()).thenReturn(typesOfWork);

        List<TypeOfWork> result = typeOfWorkService.getAllTypesOfWork();

        verify(typeOfWorkDaoMock).getAll();
        assertEquals(typesOfWork, result);
    }
}