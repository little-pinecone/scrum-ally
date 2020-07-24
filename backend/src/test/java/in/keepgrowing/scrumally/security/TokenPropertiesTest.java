package in.keepgrowing.scrumally.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenPropertiesTest {

    private TokenProperties tokenProperties;

    @BeforeEach
    void setUp() {
        tokenProperties = new TokenProperties();
    }

    @Test
    void shouldReturnDefaultProperties() {
        assertAll(
                () -> assertEquals("/api/login", tokenProperties.getLoginPath()),
                () -> assertEquals("Authorization", tokenProperties.getHeader()),
                () -> assertEquals("Bearer ", tokenProperties.getPrefix()),
                () -> assertEquals(86400, tokenProperties.getExpiration()),
                () -> assertEquals("JwtSecretKey", tokenProperties.getSecret())
        );
    }
}