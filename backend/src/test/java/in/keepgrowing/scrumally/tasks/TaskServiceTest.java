package in.keepgrowing.scrumally.tasks;

import in.keepgrowing.scrumally.projects.ProjectRepository;
import in.keepgrowing.scrumally.projects.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    void savesTask() {
        Project project = new Project("test_project", "");
        Long projectId = 1L;
        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        Task task = new Task("test_task", "", TaskPriority.LOW);
        task.setProjectFromId(projectId);
        when(taskRepository.save(task))
                .thenReturn(task);
        taskService.saveTask(task);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getsTasksByProjectId() {
        Task task = new Task("test_task", "", TaskPriority.LOW);
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        Pageable pageable = this.createPageable();
        when(taskRepository.findByProjectId(1L, pageable))
                .thenReturn(taskPage);
        Page<Task> tasksByProjectId = taskService.getTasksByProjectId(1L, pageable);

        assertEquals(1, tasksByProjectId.getNumberOfElements());

    }

    @Test
    void throwsExceptionWhenGettingTasksForNonExistingProject() {
        Pageable pageable = this.createPageable();
        doThrow(EntityNotFoundException.class)
                .when(taskRepository).findByProjectId(1L, pageable);

        assertThrows(EntityNotFoundException.class, () -> taskService.getTasksByProjectId(1L, pageable));
    }

    @Test
    void getsPagedTasks() {
        Task task = new Task("test_task", "", TaskPriority.LOW);
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        Pageable pageable = this.createPageable();
        when(taskRepository.findAll(pageable))
                .thenReturn(taskPage);
        Page<Task> tasks = taskRepository.findAll(pageable);

        assertEquals(1, tasks.getNumberOfElements());
    }

    @Test
    void getsTaskById() {
        Task task = new Task("test_task", "", TaskPriority.LOW);
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Optional<Task> optionalTask = taskService.getTaskById(1L);

        assertTrue(optionalTask.isPresent());
        assertEquals(optionalTask.get(), task);
    }

    @Test
    void returnsEmptyOptionalWhenGettingNonExistingTaskById() {
        Optional<Task> optionalTask = taskRepository.findById(1L);

        assertFalse(optionalTask.isPresent());
    }

    @Test
    void updatesTask() {
        Task task = new Task("test_task", "test_description", TaskPriority.CRITICAL);
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Task updatedTask = new Task("updated_task", "", TaskPriority.NORMAL);
        when(taskRepository.save(task))
                .thenReturn(updatedTask);
        Optional<Task> optionalTask = taskService.updateTask(updatedTask, 1L);

        assertTrue(optionalTask.isPresent());
        assertEquals(optionalTask.get(), updatedTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void returnsEmptyOptionalWhenUpdatingNonExistingTask() {
        Task updatedTask = new Task("test_task", "", TaskPriority.LOW);
        Optional<Task> optionalTask = taskService.updateTask(updatedTask, 1L);

        assertFalse(optionalTask.isPresent());
    }

    @Test
    void deletesTaskById() {
        taskService.deleteTaskById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void throwsExceptionWhenDeletingNonExistingTask() {
        doThrow(EmptyResultDataAccessException.class)
                .when(taskRepository).deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> taskService.deleteTaskById(1L));
    }

    private Pageable createPageable() {
        int pageSize = 1;
        int firstPage = 0;
        return PageRequest.of(firstPage, pageSize);
    }
}