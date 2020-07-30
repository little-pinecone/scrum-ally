package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectDto;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectEntityDtoConverter;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectWithMembersDto;
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
    private final ProjectEntityDtoConverter converter;

    public ProjectController(ProjectService projectService, ProjectEntityDtoConverter converter) {
        this.projectService = projectService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<ProjectDto> saveProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = converter.toEntity(projectDto);
        try {
            project = projectService.saveProject(project);
            return ResponseEntity.ok(converter.toDto(project));
        } catch (UserUnauthorisedException e) {
            log.info("Unauthorised operation", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public Page<ProjectDto> findAllForCurrentUser(Pageable pageable) {
        return projectService.findAllForCurrentUser(pageable)
                .map(converter::toDto);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectWithMembersDto> findOneForCurrentUser(@PathVariable Long projectId) {
        return projectService.findOneForCurrentUser(projectId)
                .map(converter::toDtoWithMembers)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{projectId}")
    public ResponseEntity<ProjectWithMembersDto> updateProject(@RequestBody @Valid ProjectWithMembersDto projectDto,
                                                    @PathVariable Long projectId) {
        var project = converter.toEntityWithMembers(projectDto);

        return projectService.updateProject(project, projectId)
                .map(converter::toDtoWithMembers)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<Void> deleteProjectOwnedByCurrentUser(@PathVariable Long projectId) {
        try {
            projectService.deleteProjectOwnedByCurrentUser(projectId);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException ex) {
            log.info(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }
}
