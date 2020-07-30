package in.keepgrowing.scrumally.user.viewmodel;

import in.keepgrowing.scrumally.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserEntityDtoConverter {

    private final ModelMapper modelMapper;

    public UserEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
