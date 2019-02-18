package in.keepgrowing.scrumally.tasks;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/tasks", produces = "application/json")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task saveTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @GetMapping
    public Page<Task> getTasks(@RequestParam("project-id") Long projectId, Pageable pageable) {
        return taskService.getTasksByProjectId(projectId, pageable);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);

        return task.map((t) -> ResponseEntity.ok().body(t))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{taskId}")
    public ResponseEntity<Task> updateTask(@RequestBody Task taskDetails, @PathVariable Long taskId) {
        Optional<Task> task = taskService.updateTask(taskDetails, taskId);

        return task.map((t) -> ResponseEntity.ok().body(t))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long taskId) {
        try{
            taskService.deleteTaskById(taskId);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
