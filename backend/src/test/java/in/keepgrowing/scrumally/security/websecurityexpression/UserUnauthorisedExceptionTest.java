package in.keepgrowing.scrumally.security.websecurityexpression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserUnauthorisedExceptionTest {

    @Test
    void shouldCreateException() {
        assertNull(new UserUnauthorisedException().getMessage());
    }
}