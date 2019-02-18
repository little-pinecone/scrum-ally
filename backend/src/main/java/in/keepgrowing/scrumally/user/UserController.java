package in.keepgrowing.scrumally.user;

import in.keepgrowing.scrumally.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/users", produces = "application/json")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("{userId}")
    public ResponseEntity<User> update(@RequestBody User userDetails, @PathVariable Long userId) {
        Optional<User> user = userService.update(userDetails, userId);

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
