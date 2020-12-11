package in.keepgrowing.scrumally.security;

import in.keepgrowing.scrumally.user.UserService;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadsUserByUsername() {
        UserCredentials credentials = new UserCredentials("user", "start", "ROLE_ADMIN");
        User user = new User(credentials);
        given(userService.findByUsername("user"))
                .willReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");
        assertEquals("user", userDetails.getUsername());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void throwsExceptionWhenLoadingNonExistingUser() {
        assertThrows(UsernameNotFoundException.class, ()-> userDetailsService.loadUserByUsername("user"));
    }

    @Test
    void exceptionMessageContainsUsername() {
        try {
            userDetailsService.loadUserByUsername("user1");
        } catch (UsernameNotFoundException e) {
            assertEquals("Username: user1 not found", e.getMessage());
        }
    }
}