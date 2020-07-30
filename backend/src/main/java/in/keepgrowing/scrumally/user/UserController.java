package in.keepgrowing.scrumally.user;

import in.keepgrowing.scrumally.user.viewmodel.UserDto;
import in.keepgrowing.scrumally.user.viewmodel.UserEntityDtoConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/users", produces = "application/json")
public class UserController {

    private final UserService userService;
    private final UserEntityDtoConverter converter;

    public UserController(UserService userService, UserEntityDtoConverter converter) {
        this.userService = userService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        var user = converter.toEntity(userDto);
        user = userService.register(user);
        return ResponseEntity.ok(converter.toDto(user));
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDetails, @PathVariable Long userId) {
        var user = converter.toEntity(userDetails);

        return userService.update(user, userId)
                .map(converter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
