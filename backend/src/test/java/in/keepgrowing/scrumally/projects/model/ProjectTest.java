package in.keepgrowing.scrumally.projects.model;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Id;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProjectTest {

    @Test
    void equalsContract() {
        User user = new User(new UserCredentials("username", "pass", "role"));
        EqualsVerifier.forClass(Project.class)
                .withPrefabValues(ProjectMember.class, new ProjectMember(user, getProject(), ProjectRole.GUEST),
                        new ProjectMember(user, getProject(), ProjectRole.OWNER))
                .usingGetClass()
                .withIgnoredAnnotations(Id.class)
                .verify();
    }

    @Test
    void convertsToString() {
        String expected = "Project{id=null, name='name', description='', members=[]}";
        assertEquals(expected, getProject().toString());
    }

    private Project getProject() {
        return new Project("name", "");
    }
}