package in.keepgrowing.scrumally.user.viewmodel;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectMemberDto;
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
class UserEntityDtoConverterTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    private UserEntityDtoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserEntityDtoConverter(modelMapper);
    }

    @Test
    void shouldConvertToDto() {
        var user = getUser();
        var project = getProject();
        var members = getEntityMembers(project);
        project.setMembers(members);
        user.setProjects(members);
        var dto = converter.toDto(user);

        assertAll(
                () -> assertEquals(user.getId(), dto.getId()),
                () -> assertEquals(getDtoMembers(), dto.getProjects()),
                () -> assertEquals(user.getUserCredentials().getUsername(), dto.getUserCredentials().getUsername()),
                () -> assertEquals(user.getUserCredentials().getPassword(), dto.getUserCredentials().getPassword()),
                () -> assertEquals(user.getUserCredentials().getRole(), dto.getUserCredentials().getRole())
        );
    }

    private User getUser() {
        var credentials = new UserCredentials("username", "password", "role");
        return new User(1L, credentials, null);
    }

    private Project getProject() {
        var project = new Project("name", "description");
        project.setId(2L);
        return project;
    }

    private Set<ProjectMember> getEntityMembers(Project project) {
        User user = getUser();
        return Set.of(new ProjectMember(user, project, ProjectRole.GUEST));
    }

    private Set<ProjectMemberDto> getDtoMembers() {
        return Set.of(new ProjectMemberDto(null, 1L, ProjectRole.GUEST));
    }

    @Test
    void shouldConvertToEntity() {
        var dto = getUserDto();
        var user = converter.toEntity(dto);
        var project = getProject();
        var members = getEntityMembers(project);
        project.setMembers(members);

        assertAll(
                () -> assertEquals(dto.getId(), user.getId()),
                () -> assertEquals(getEntityMembers(project), user.getProjects()),
                () -> assertEquals(dto.getUserCredentials().getUsername(), user.getUserCredentials().getUsername()),
                () -> assertEquals(dto.getUserCredentials().getPassword(), user.getUserCredentials().getPassword()),
                () -> assertEquals(dto.getUserCredentials().getRole(), user.getUserCredentials().getRole())
        );
    }

    private UserDto getUserDto() {
        var credentials = new UserCredentialsDto("username", "password", "role");
        return new UserDto(1L, credentials, getDtoMembers());
    }
}