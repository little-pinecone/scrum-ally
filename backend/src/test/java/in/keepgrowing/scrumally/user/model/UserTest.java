package in.keepgrowing.scrumally.user.model;

import in.keepgrowing.scrumally.projects.model.Project;
import in.keepgrowing.scrumally.projects.model.ProjectMember;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(User.class)
                .withPrefabValues(ProjectMember.class, new ProjectMember(getUser(), getProject(), ProjectRole.GUEST),
                        new ProjectMember(getUser(), getProject(), ProjectRole.OWNER))
                .withIgnoredAnnotations(Id.class)
                .usingGetClass()
                .verify();
    }

    private User getUser() {
        UserCredentials credentials = new UserCredentials("admin", "admin", "role");
        return new User(credentials);
    }

    private Project getProject() {
        return new Project("name", "");
    }

    @Test
    void convertsToString() {
        String expected = "User{id=null, userCredentials=UserCredentials{username='admin', password='admin', role='role'}, projects=null}";
        String actual = getUser().toString();
        assertEquals(expected, actual);
    }
}