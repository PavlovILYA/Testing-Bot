package ru.mephi.knowledgechecker.dto.telegram.income.mapper;

import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.model.user.User;

// todo: use mapstruct
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .build();
    }

    public static String mapStateToBeanName(Class stateClass) {
        String className = stateClass.getSimpleName().split("\\$")[0];
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
