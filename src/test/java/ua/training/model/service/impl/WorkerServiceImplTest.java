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

import ua.training.model.dao.WorkerDao;
import ua.training.model.entities.person.Worker;

@RunWith(MockitoJUnitRunner.class)
public class WorkerServiceImplTest {

    @Mock
    private WorkerDao workerDaoMock;

    @InjectMocks
    private WorkerServiceImpl workerService;

    @Test
    public void shouldGetAllWorkers() {
        List<Worker> workers = Lists.newArrayList(new Worker(), new Worker(), new Worker());
        when(workerDaoMock.getAll()).thenReturn(workers);

        List<Worker> result = workerService.getAllWorkers();

        verify(workerDaoMock).getAll();
        assertEquals(workers, result);
    }

    @Test
    public void shouldAddNewWorker() {
        Worker worker = new Worker();

        workerService.addNewWorker(worker);

        verify(workerDaoMock).add(worker);
    }
}