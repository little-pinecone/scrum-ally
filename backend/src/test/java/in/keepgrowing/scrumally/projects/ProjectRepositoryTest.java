package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import in.keepgrowing.scrumally.user.model.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(SecurityEvaluationContextConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    private User currentUser;
    private User otherUser;

    @Before
    public void setUsers() {
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
    public void findsAllForCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        createProjectWithMember(otherUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageable());

        assertEquals(getExpectedPage(project).getContent(), requestedPage.getContent());
        assertEquals(requestedPage.getNumberOfElements(), 1);
    }

    @Test
    @WithMockUser("current")
    public void findsAllSortedForCurrentUser() {
        List<Project> expectedPage = Arrays.asList(
                createProjectWithMember(currentUser),
                createProjectWithMember(currentUser));
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageableDescendingById());

        assertEquals(expectedPage.get(0), requestedPage.getContent().get(1));
    }

    @Test
    @WithMockUser("current")
    public void returnsEmptySecondPageWhenSizeExceedsProjectsAmount() {
        createProjectWithMember(currentUser);
        createProjectWithMember(currentUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getSecondPage());

        assertEquals(new ArrayList<>(), requestedPage.getContent());
    }

    @Test
    @WithMockUser(username = "current")
    public void findsNoneIfCurrentUserDoesNotOwnAny() {
        createProjectWithMember(otherUser);
        Page<Project> requestedPage = projectRepository.findAllForCurrentUser(getPageable());

        assertEquals(new ArrayList<>(), requestedPage.getContent());
    }

    @Test
    @WithMockUser(username = "current")
    public void findsOneForCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        createProjectWithMember(otherUser);
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());

        assertTrue(requestedProject.isPresent());
        assertEquals(project, requestedProject.get());
    }

    @Test
    @WithMockUser(username = "current")
    public void findsNoneForCurrentUserWhenAskedForOtherUserProject() {
        Project project = createProjectWithMember(otherUser);
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());

        assertFalse(requestedProject.isPresent());
    }

    @Test
    @WithMockUser(username = "current")
    public void updatesProjectOwnedByCurrentUser() {
        Project project = createProjectWithMember(currentUser);
        project.updateFrom(new Project("name", "description"));
        Optional<Project> requestedProject = projectRepository.findOneForCurrentUser(project.getId());
        assertTrue(requestedProject.isPresent());
        assertEquals(requestedProject.get().getName(), "name");
    }

    @Test
    @WithMockUser(username = "current")
    public void deletesProjectOwnedByCurrentUser() {
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