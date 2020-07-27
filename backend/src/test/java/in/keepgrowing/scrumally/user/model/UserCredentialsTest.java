package in.keepgrowing.scrumally.user.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class})
class UserCredentialsTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(UserCredentials.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void convertsToString() {
        String expected = "UserCredentials{username='admin', password='admin', role='role'}";
        String actual = new UserCredentials("admin", "admin", "role").toString();
        assertEquals(expected, actual);
    }
}