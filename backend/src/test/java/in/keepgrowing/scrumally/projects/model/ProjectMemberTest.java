package in.keepgrowing.scrumally.projects.model;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectMemberTest {

    @Test
    void createsValidProjectMember() {
        ProjectMember member = getProjectMember();
        assertAll(
                () -> assertEquals(getProject("name"), member.getProject()),
                () -> assertEquals(getUser("name"), member.getUser()),
                () -> assertSame(ProjectRole.GUEST, member.getProjectRole()),
                () -> assertNull(member.getId())
        );
    }

    private User getUser(String name) {
        UserCredentials credentials = new UserCredentials(name, "admin", "role");
        return new User(credentials);
    }

    private Project getProject(String name) {
        return new Project(name, "");
    }

    private ProjectMember getProjectMember() {
        return new ProjectMember(getUser("name"), getProject("name"), ProjectRole.GUEST);
    }
}