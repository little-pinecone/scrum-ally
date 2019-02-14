package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.security.WebSecurityExpression.UserUnauthorisedException;
import in.keepgrowing.scrumally.user.UserService;
import in.keepgrowing.scrumally.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserService userService;

    private ProjectService projectService;

    @Before
    public void setUp() {
        projectService = new ProjectService(projectRepository, userService);
    }

    @Test
    @WithMockUser(username = "current", password = "pwd", roles = "USER")
    public void savesProject() throws UserUnauthorisedException {
        Project project = createTestProject();
        when(userService.findByUsername("current"))
                .thenReturn(Optional.of(User.fromUserName("current")));
        projectService.saveProject(project);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void getsPagedProjectsForCurrentUser() {
        Pageable pageable = getPageable();
        when(projectRepository.findAllForCurrentUser(pageable))
                .thenReturn(getExpectedPage(createTestProject()));
        Page<Project> requestedPage = projectService.findAllForCurrentUser(pageable);

        assertEquals(requestedPage.getNumberOfElements(), 1);
        verify(projectRepository, times(1)).findAllForCurrentUser(pageable);
    }

    @Test
    public void getsOneProjectForCurrentUser() {
        Project project = createTestProject();
        when(projectRepository.findOneForCurrentUser(1L))
                .thenReturn(Optional.of(project));
        Optional<Project> optionalProject = projectService.findOneForCurrentUser(1L);

        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get(), project);
        verify(projectRepository, times(1)).findOneForCurrentUser(1L);
    }

    @Test
    public void returnsEmptyOptionalWhenGettingNotExistingProject() {
        Optional<Project> optionalProject = projectService.findOneForCurrentUser(1L);

        assertFalse(optionalProject.isPresent());
        verify(projectRepository, times(1)).findOneForCurrentUser(1L);
    }

    @Test
    public void updatesProject() {
        Project project = createTestProject();
        when(projectRepository.findOneForCurrentUser(1L))
                .thenReturn(Optional.of(project));
        Project updatedProject = createTestProject();
        when(projectRepository.save(project))
                .thenReturn(updatedProject);
        Optional<Project> optionalProject = projectService.updateProject(updatedProject, 1L);

        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get(), updatedProject);
        verify(projectRepository, times(1)).findOneForCurrentUser(1L);
        verify(projectRepository, times(1)).save(updatedProject);
    }

    @Test
    public void returnsEmptyOptionalWhenUpdatingNotExistingProject() {
        Optional<Project> optionalProject = projectService.updateProject(createTestProject(), 1L);

        assertFalse(optionalProject.isPresent());
    }

    private Project createTestProject() {
        return new Project("test_project", "");
    }

    private Page<Project> getExpectedPage(Project project) {
        return new PageImpl<>(Collections.singletonList(project));
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 1);
    }

    @Test
    public void deletesProjectOwnedByCurrentUser() {
        projectService.deleteProjectOwnedByCurrentUser(1L);

        verify(projectRepository, times(1)).deleteProjectOwnedByCurrentUser(1L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void failsToDeleteNonExistingProjectById() {
        doThrow(EmptyResultDataAccessException.class)
                .when(projectRepository).deleteProjectOwnedByCurrentUser(1L);

        projectService.deleteProjectOwnedByCurrentUser(1L);
    }
}