package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.security.WebSecurityExpression.UserUnauthorisedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/projects", produces = "application/json")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        try {
            return ResponseEntity.ok().body(projectService.saveProject(project));
        } catch (UserUnauthorisedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public Page<Project> findAllForCurrentUser(Pageable pageable) {
        return projectService.findAllForCurrentUser(pageable);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<Project> findOneForCurrentUser(@PathVariable Long projectId) {
        Optional<Project> project = projectService.findOneForCurrentUser(projectId);

        return project.map((p) -> ResponseEntity.ok().body(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{projectId}")
    public ResponseEntity<Project> updateProject(@RequestBody Project projectDetails, @PathVariable Long projectId) {
        Optional<Project> project = projectService.updateProject(projectDetails, projectId);

        return project.map((p) -> ResponseEntity.ok().body(p))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Project> deleteProjectOwnedByCurrentUser(@PathVariable Long projectId) {
        try{
            projectService.deleteProjectOwnedByCurrentUser(projectId);
            return ResponseEntity.noContent().build();
        } catch(EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
