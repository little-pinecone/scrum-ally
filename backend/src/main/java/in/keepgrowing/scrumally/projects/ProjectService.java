package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.security.WebSecurityExpression.AccessPolicy;
import in.keepgrowing.scrumally.security.WebSecurityExpression.UserUnauthorisedException;
import in.keepgrowing.scrumally.user.UserService;
import in.keepgrowing.scrumally.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public Project saveProject(Project project) throws UserUnauthorisedException {
        setOwner(project);
        return projectRepository.save(project);
    }

    private void setOwner(Project project) throws UserUnauthorisedException {
        ProjectMember owner = new ProjectMember(getCurrentUser(), project, ProjectRole.OWNER);
        project.setMembers(new HashSet<>(Collections.singletonList(owner)));
    }

    private User getCurrentUser() throws UserUnauthorisedException {
        Optional<User> currentUserOpt = userService.findByUsername(AccessPolicy.getCurrentUser().getUsername());
        return currentUserOpt.orElseThrow(UserUnauthorisedException::new);
    }

    public Page<Project> findAllForCurrentUser(Pageable pageable) {
        return projectRepository.findAllForCurrentUser(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Project> findOneForCurrentUser(Long projectId) {
        return projectRepository.findOneForCurrentUser(projectId);
    }

    public Optional<Project> updateProject(Project projectDetails, Long projectId) {
        Optional<Project> project = projectRepository.findOneForCurrentUser(projectId);

        return project.flatMap(p -> {
            p.updateFrom(projectDetails);
            return Optional.of(projectRepository.save(p));
        });
    }

    public void deleteProjectOwnedByCurrentUser(Long projectId) {
        projectRepository.deleteProjectOwnedByCurrentUser(projectId);
    }
}
