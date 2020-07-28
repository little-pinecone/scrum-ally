package in.keepgrowing.scrumally.projects.viewmodel;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProjectEntityDtoConverterTest {

    private final ModelMapper modelMapper = new ModelMapper();

    private ProjectEntityDtoConverter entityDtoConverter;

    @BeforeEach
    void setUp() {
        entityDtoConverter = new ProjectEntityDtoConverter(modelMapper);
    }

    @Test
    void shouldConvertEntityToDto() {
        var project = createTestProject();
        project.setMembers(getEntityMembers(project));
        var projectDto = entityDtoConverter.toDto(project);

        assertAll(
                () -> assertEquals("name", projectDto.getName()),
                () -> assertEquals("description", projectDto.getDescription()),
                () -> assertEquals(1L, projectDto.getId()),
                () -> assertEquals(getDtoMembers(), projectDto.getMembers())
        );
    }

    private Project createTestProject() {
        var project = new Project("name", "description");
        project.setId(1L);
        return project;
    }

    private Set<ProjectMember> getEntityMembers(Project project) {
        User user = getUser();
        return Set.of(new ProjectMember(user, project, ProjectRole.GUEST));
    }

    private User getUser() {
        var credentials = new UserCredentials("username", "password", "role");
        return new User(1L, credentials, null);
    }

    private Set<ProjectMemberDto> getDtoMembers() {
        return Set.of(new ProjectMemberDto(null, 1L, ProjectRole.GUEST));
    }

    @Test
    void shouldConvertDtoToEntity() {
        var projectDto = createTestProjectDto();
        var project = entityDtoConverter.toEntity(projectDto);

        assertAll(
                () -> assertEquals("dto_name", project.getName()),
                () -> assertEquals("dto_description", project.getDescription()),
                () -> assertEquals(3L, project.getId()),
                () -> assertEquals(getEntityMembers(project), project.getMembers())
        );
    }

    private ProjectDto createTestProjectDto() {
        var projectDto = new ProjectDto(3L, "dto_name", "dto_description", getDtoMembers());
        projectDto.setId(3L);
        projectDto.setMembers(getDtoMembers());
        return projectDto;
    }
}