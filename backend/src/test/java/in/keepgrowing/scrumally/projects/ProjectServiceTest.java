package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.security.websecurityexpression.UserUnauthorisedException;
import in.keepgrowing.scrumally.user.UserService;
import in.keepgrowing.scrumally.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository, userService);
    }

    @Test
    @WithMockUser(username = "current", password = "pwd", roles = "USER")
    void savesProject() throws UserUnauthorisedException {
        Project project = createTestProject();
        when(userService.findByUsername("current"))
                .thenReturn(Optional.of(User.fromUserName("current")));
        projectService.saveProject(project);

        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void getsPagedProjectsForCurrentUser() {
        Pageable pageable = getPageable();
        when(projectRepository.findAllForCurrentUser(pageable))
                .thenReturn(getExpectedPage(createTestProject()));
        Page<Project> requestedPage = projectService.findAllForCurrentUser(pageable);

        assertEquals(1, requestedPage.getNumberOfElements());
        verify(projectRepository, times(1)).findAllForCurrentUser(pageable);
    }

    @Test
    void getsOneProjectForCurrentUser() {
        Project project = createTestProject();
        when(projectRepository.findOneForCurrentUser(1L))
                .thenReturn(Optional.of(project));
        Optional<Project> optionalProject = projectService.findOneForCurrentUser(1L);

        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get(), project);
        verify(projectRepository, times(1)).findOneForCurrentUser(1L);
    }

    @Test
    void returnsEmptyOptionalWhenGettingNotExistingProject() {
        Optional<Project> optionalProject = projectService.findOneForCurrentUser(1L);

        assertFalse(optionalProject.isPresent());
        verify(projectRepository, times(1)).findOneForCurrentUser(1L);
    }

    @Test
    void updatesProject() {
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
    void returnsEmptyOptionalWhenUpdatingNotExistingProject() {
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
    void deletesProjectOwnedByCurrentUser() {
        projectService.deleteProjectOwnedByCurrentUser(1L);

        verify(projectRepository, times(1)).deleteProjectOwnedByCurrentUser(1L);
    }

    @Test
    void failsToDeleteNonExistingProjectById() {
        doThrow(EmptyResultDataAccessException.class)
                .when(projectRepository).deleteProjectOwnedByCurrentUser(1L);

        assertThrows(EmptyResultDataAccessException.class,
                () -> projectService.deleteProjectOwnedByCurrentUser(1L));
    }
}