package in.keepgrowing.scrumally.tasks;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    private TaskService taskService;

    @Before
    public void setUp() {
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    public void savesTask() {
        Project project = new Project("test_project", "");
        Long projectId = 1L;
        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        Task task = new Task("test_task", "");
        task.setProjectFromId(projectId);
        when(taskRepository.save(task))
                .thenReturn(task);
        taskService.saveTask(task);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void getsTasksByProjectId() {
        Task task = new Task("test_task", "");
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        Pageable pageable = this.createPageable();
        when(taskRepository.findByProjectId(1L, pageable))
                .thenReturn(taskPage);
        Page<Task> tasksByProjectId = taskService.getTasksByProjectId(1L, pageable);

        assertEquals(tasksByProjectId.getNumberOfElements(), 1);

    }

    @Test(expected = EntityNotFoundException.class)
    public void throwsExceptionWhenGettingTasksForNonExistingProject() {
        Pageable pageable = this.createPageable();
        doThrow(EntityNotFoundException.class)
                .when(taskRepository).findByProjectId(1L, pageable);

        taskService.getTasksByProjectId(1L, pageable);
    }

    @Test
    public void getsPagedTasks() {
        Task task = new Task("test_task", "");
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        Pageable pageable = this.createPageable();
        when(taskRepository.findAll(pageable))
                .thenReturn(taskPage);
        Page<Task> tasks = taskRepository.findAll(pageable);

        assertEquals(tasks.getNumberOfElements(), 1);
    }

    @Test
    public void getsTaskById() {
        Task task = new Task("test_task", "");
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Optional<Task> optionalTask = taskService.getTaskById(1L);

        assertTrue(optionalTask.isPresent());
        assertEquals(optionalTask.get(), task);
    }

    @Test
    public void returnsEmptyOptionalWhenGettingNonExistingTaskById() {
        Optional<Task> optionalTask = taskRepository.findById(1L);

        assertFalse(optionalTask.isPresent());
    }

    @Test
    public void updatesTask() {
        Task task = new Task("test_task", "test_description");
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Task updatedTask = new Task("updated_task", "");
        when(taskRepository.save(task))
                .thenReturn(updatedTask);
        Optional<Task> optionalTask = taskService.updateTask(updatedTask, 1L);

        assertTrue(optionalTask.isPresent());
        assertEquals(optionalTask.get(), updatedTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void returnsEmptyOptionalWhenUpdatingNonExistingTask() {
        Task updatedTask = new Task("test_task", "");
        Optional<Task> optionalTask = taskService.updateTask(updatedTask, 1L);

        assertFalse(optionalTask.isPresent());
    }

    @Test
    public void deletesTaskById() {
        taskService.deleteTaskById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void throwsExceptionWhenDeletingNonExistingTask() {
        doThrow(EmptyResultDataAccessException.class)
                .when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L);
    }

    private Pageable createPageable() {
        int pageSize = 1;
        int firstPage = 0;
        return PageRequest.of(firstPage, pageSize);
    }
}