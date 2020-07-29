package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectDto;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectEntityDtoConverter;
import in.keepgrowing.scrumally.security.websecurityexpression.UserUnauthorisedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "api/projects", produces = "application/json")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectEntityDtoConverter entityDtoConverter;

    public ProjectController(ProjectService projectService, ProjectEntityDtoConverter entityDtoConverter) {
        this.projectService = projectService;
        this.entityDtoConverter = entityDtoConverter;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> saveProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = entityDtoConverter.toEntity(projectDto);
        try {
            project = projectService.saveProject(project);
            return ResponseEntity.ok(entityDtoConverter.toDto(project));
        } catch (UserUnauthorisedException e) {
            log.info("Unauthorised operation", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public Page<Project> findAllForCurrentUser(Pageable pageable) {
        return projectService.findAllForCurrentUser(pageable);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDto> findOneForCurrentUser(@PathVariable Long projectId) {
        return projectService.findOneForCurrentUser(projectId)
                .map(entityDtoConverter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{projectId}")
    public ResponseEntity<ProjectDto> updateProject(@RequestBody @Valid ProjectDto projectDto,
                                                    @PathVariable Long projectId) {
        var project = entityDtoConverter.toEntity(projectDto);

        return projectService.updateProject(project, projectId)
                .map(entityDtoConverter::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Project> deleteProjectOwnedByCurrentUser(@PathVariable Long projectId) {
        try {
            projectService.deleteProjectOwnedByCurrentUser(projectId);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException ex) {
            log.info(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }
}
