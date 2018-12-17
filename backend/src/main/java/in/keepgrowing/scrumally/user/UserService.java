package in.keepgrowing.scrumally.user;

import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User register(User user) {
        setUserProperties(user);
        return userRepository.save(user);
    }

    private void setUserProperties(User user) {
        user.getUserCredentials()
                .setPassword(encoder.encode(user.getUserCredentials().getPassword()));
        user.getUserCredentials().setRole(DEFAULT_ROLE);
    }

    public Optional<User> update(User userDetails, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.flatMap(u -> {
            u.updateFrom(userDetails);
            return Optional.of(userRepository.save(u));
        });
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserCredentialsUsername(username);
    }
}
