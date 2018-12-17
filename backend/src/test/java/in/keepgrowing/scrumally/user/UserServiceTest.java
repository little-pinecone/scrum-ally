package in.keepgrowing.scrumally.user;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import in.keepgrowing.scrumally.user.model.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, encoder);
    }

    @Test
    public void registersNewUser() {
        User user = createTestUser("");
        when(userRepository.save(user))
                .thenReturn(user);
        userService.register(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updates() {
        User user = createTestUser("");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        User updatedUser = createTestUser("ROLE_ADMIN");
        when(userRepository.save(user))
                .thenReturn(updatedUser);
        Optional<User> optionalUser= userService.update(updatedUser, 1L);

        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get(), updatedUser);
        verify(userRepository, times(1)).save(user);
    }

    private User createTestUser(String role) {
        UserCredentials credentials = new UserCredentials("user", "start", role);
        return new User(credentials);
    }

    @Test
    public void returnsEmptyOptionalWhenGettingNonExistingUser() {
        Optional<User> user = userRepository.findById(1L);

        assertFalse(user.isPresent());
    }

    @Test
    public void returnsEmptyOptionalWhenSearchingForNullUsername() {
        Optional<User> optionalUser = userService.findByUsername(null);

        assertFalse(optionalUser.isPresent());
    }
}