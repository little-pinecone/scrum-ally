package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import in.keepgrowing.scrumally.user.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SecurityEvaluationContextConfig.class)
class ProjectRepositoryIT {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User currentUser;
    private User otherUser;

    @BeforeEach
    void setUsers() {
        currentUser = createUser("current");
        userRepository.save(currentUser);
        otherUser = createUser("other");
        userRepository.save(otherUser);
    }

    private User createUser(String username) {
        UserCredentials credentials = new UserCredentials(username, "password", "USER");
        return new User(credentials);
    }

    @Test
    @WithMockUser(username = "current")
    void findsAllForCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        createProjectWithMember(otherUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageable());

        assertEquals(getExpectedPage(project).getContent(), requestedPage.getContent());
        assertEquals(1, requestedPage.getNumberOfElements());
    }

    @Test
    @WithMockUser("current")
    void findsAllSortedForCurrentUser() {
        List<Project> expectedPage = Arrays.asList(
                createProjectWithMember(currentUser),
                createProjectWithMember(currentUser));
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageableDescendingById());

        assertEquals(expectedPage.get(0), requestedPage.getContent().get(1));
    }

    @Test
    @WithMockUser("current")
    void returnsEmptySecondPageWhenSizeExceedsProjectsAmount() {
        createProjectWithMember(currentUser);
        createProjectWithMember(currentUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getSecondPage());

        assertEquals(new ArrayList<>(), requestedPage.getContent());
    }

    @Test
    @WithMockUser(username = "current")
    void findsNoneIfCurrentUserDoesNotOwnAny() {
        createProjectWithMember(otherUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageable());

        assertEquals(new ArrayList<>(), requestedPage.getContent());
    }

    @Test
    @WithMockUser(username = "current")
    void findsOneForCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        createProjectWithMember(otherUser);
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());

        assertTrue(requestedProject.isPresent());
        assertEquals(project, requestedProject.get());
    }

    @Test
    @WithMockUser(username = "current")
    void findsNoneForCurrentUserWhenAskedForOtherUserProject() {
        Project project = createProjectWithMember(otherUser);
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());

        assertFalse(requestedProject.isPresent());
    }

    @Test
    @WithMockUser(username = "current")
    void updatesProjectOwnedByCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        project.updateFrom(new Project("name", "description"));
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());
        assertTrue(requestedProject.isPresent());
        assertEquals("name", requestedProject.get().getName());
    }

    @Test
    @WithMockUser(username = "current")
    void deletesProjectOwnedByCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        assertTrue(projectRepository.findOneForCurrentUser(project.getId()).isPresent());
        projectRepository.deleteProjectOwnedByCurrentUser(project.getId());
        entityManager.clear();

        assertFalse(projectRepository.findOneForCurrentUser(project.getId()).isPresent());
        assertFalse(projectRepository.findById(project.getId()).isPresent());
    }

    private Project createProjectWithMember(User user) {
        Project project = new Project("test_project", "");
        Set<ProjectMember> members = getMembers(user, project);
        project.setMembers(members);
        projectRepository.save(project);
        return project;
    }

    private Set<ProjectMember> getMembers(User user, Project project) {
        ProjectMember projectMember = new ProjectMember(user, project, ProjectRole.OWNER);
        Set<ProjectMember> members = new HashSet<>();
        members.add(projectMember);
        return members;
    }

    private Page<Project> getExpectedPage(Project project) {
        return new PageImpl<>(Collections.singletonList(project));
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 1);
    }

    private Pageable getPageableDescendingById() {
        return PageRequest.of(0, 2, Sort.Direction.DESC, "id");
    }

    private Pageable getSecondPage() {
        return PageRequest.of(1, 2);
    }
}