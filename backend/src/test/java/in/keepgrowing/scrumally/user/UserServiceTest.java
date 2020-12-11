package in.keepgrowing.scrumally.user;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import in.keepgrowing.scrumally.user.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, encoder);
    }

    @Test
    void registersNewUser() {
        User user = createTestUser("");
        when(userRepository.save(user))
                .thenReturn(user);
        userService.register(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updates() {
        User user = createTestUser("");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        User updatedUser = createTestUser("ROLE_ADMIN");
        when(userRepository.save(user))
                .thenReturn(updatedUser);
        Optional<User> optionalUser = userService.update(updatedUser, 1L);

        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get(), updatedUser);
        verify(userRepository, times(1)).save(user);
    }

    private User createTestUser(String role) {
        UserCredentials credentials = new UserCredentials("user", "start", role);
        return new User(credentials);
    }

    @Test
    void returnsEmptyOptionalWhenGettingNonExistingUser() {
        Optional<User> user = userRepository.findById(1L);

        assertFalse(user.isPresent());
    }

    @Test
    void returnsEmptyOptionalWhenSearchingForNullUsername() {
        Optional<User> optionalUser = userService.findByUsername(null);

        assertFalse(optionalUser.isPresent());
    }
}