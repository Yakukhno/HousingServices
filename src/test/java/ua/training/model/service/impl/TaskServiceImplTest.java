package ua.training.model.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import ua.training.model.dao.ApplicationDao;
import ua.training.model.dao.BrigadeDao;
import ua.training.model.dao.TaskDao;
import ua.training.model.dao.WorkerDao;
import ua.training.model.dto.TaskDto;
import ua.training.model.entities.Application;
import ua.training.model.entities.Brigade;
import ua.training.model.entities.Task;
import ua.training.model.entities.person.Worker;
import ua.training.model.util.ServiceHelper;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

    private static final String WORKER_1_NAME = "worker1";
    private static final String WORKER_2_NAME = "worker2";
    private static final int MANAGER_ID = 10;
    private static final int WORKER_1_ID = 21;
    private static final int WORKER_2_ID = 22;
    private static final int APPLICATION_ID = 30;

    @Captor
    private ArgumentCaptor<Brigade> brigadeArgumentCaptor;

    @Mock
    private TaskDao taskDaoMock;
    @Mock
    private ApplicationDao applicationDaoMock;
    @Mock
    private WorkerDao workerDaoMock;
    @Mock
    private BrigadeDao brigadeDaoMock;
    @Mock
    private ServiceHelper serviceHelperMock;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void shouldGetActiveTasks() {
        List<Task> tasks = Lists.newArrayList(new Task(), new Task(), new Task());
        when(taskDaoMock.getActiveTasks()).thenReturn(tasks);

        List<Task> result = taskService.getActiveTasks();

        verify(taskDaoMock).getActiveTasks();
        assertEquals(tasks, result);
    }

    @Test
    public void shouldCreateNewTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setManagerId(MANAGER_ID);
        taskDto.setWorkersIds(Lists.newArrayList(WORKER_1_ID, WORKER_2_ID));
        taskDto.setApplicationId(APPLICATION_ID);

        Worker manager = new Worker();
        manager.setId(MANAGER_ID);
        Worker worker1 = new Worker();
        worker1.setId(WORKER_1_ID);
        worker1.setName(WORKER_1_NAME);
        Worker worker2 = new Worker();
        worker2.setId(WORKER_2_ID);
        worker2.setName(WORKER_2_NAME);
        when(workerDaoMock.get(MANAGER_ID)).thenReturn(Optional.of(manager));
        when(workerDaoMock.get(WORKER_1_ID)).thenReturn(Optional.of(worker1));
        when(workerDaoMock.get(WORKER_2_ID)).thenReturn(Optional.of(worker2));

        when(applicationDaoMock.get(APPLICATION_ID)).thenReturn(Optional.of(new Application()));

        taskService.createNewTask(taskDto);

        verify(taskDaoMock).add(any(Task.class));
        verify(applicationDaoMock).update(any(Application.class));
        verify(brigadeDaoMock).add(brigadeArgumentCaptor.capture());

        Brigade brigade = brigadeArgumentCaptor.getValue();
        assertEquals(manager, brigade.getManager());
        assertEquals(2, brigade.getWorkers().size());
    }
}