package in.keepgrowing.scrumally.projects.model;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectMemberTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ProjectMember.class)
                .withPrefabValues(User.class, getUser("name1"), getUser("name2"))
                .withPrefabValues(Project.class, getProject("name1"), getProject("name2"))
                .withIgnoredAnnotations(Id.class)
                .withIgnoredFields("project", "user")
                .usingGetClass()
                .verify();
    }

    private User getUser(String name) {
        UserCredentials credentials = new UserCredentials(name, "admin", "role");
        return new User(credentials);
    }

    private Project getProject(String name) {
        return new Project(name, "");
    }

    @Test
    void convertsToString() {
        String expected = "ProjectMember{id=null, projectRole=GUEST}";
        ProjectMember member = getProjectMember();
        String actual = member.toString();
        assertEquals(expected, actual);
    }

    private ProjectMember getProjectMember() {
        return new ProjectMember(getUser("name"), getProject("name"), ProjectRole.GUEST);
    }

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
}