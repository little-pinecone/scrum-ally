package in.keepgrowing.scrumally.tasks;

import in.keepgrowing.scrumally.tasks.viewmodel.TaskDto;
import in.keepgrowing.scrumally.tasks.viewmodel.TaskEntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "api/tasks", produces = "application/json")
public class TaskController {

    private final TaskEntityDtoConverter converter;
    private final TaskService taskService;

    public TaskController(TaskEntityDtoConverter converter, TaskService taskService) {
        this.converter = converter;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> saveTask(@RequestBody @Valid TaskDto taskDto) {
        var task = converter.toEntity(taskDto);
        task = taskService.saveTask(task);
        return ResponseEntity.ok(converter.toDto(task));
    }

    @GetMapping
    public Page<TaskDto> getTasks(@RequestParam("project-id") Long projectId, Pageable pageable) {
        return taskService.getTasksByProjectId(projectId, pageable).map(converter::toDto);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{taskId}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid TaskDto taskDetails, @PathVariable Long taskId) {
        var task = converter.toEntity(taskDetails);

        return taskService.updateTask(task, taskId)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}
