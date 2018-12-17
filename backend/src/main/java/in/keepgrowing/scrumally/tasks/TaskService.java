package in.keepgrowing.scrumally.tasks;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Task saveTask(Task task) {
        return projectRepository.findById(task.getProject().getId())
                .map(project -> setTaskProperties(task, project))
                .orElseThrow(EntityNotFoundException::new);
    }

    private Task setTaskProperties(Task task, Project project) {
        task.setProject(project);

        return taskRepository.save(task);
    }

    public Page<Task> getTasksByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Optional<Task> updateTask(Task taskDetails, Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);

        return task.flatMap(t -> {
            t.updateFrom(taskDetails);
            return Optional.of(taskRepository.save(t));
        });
    }

    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
