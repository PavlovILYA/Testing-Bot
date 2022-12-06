package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.model.user.mapper.UserMapper;
import ru.mephi.knowledgechecker.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User get(Long id) {
        User user = userRepository.findById(id).orElse(null);
        log.info("Get user: {}", user);
        return user;
    }

    public UserDto save(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Saved user: {}", user);
        return UserMapper.toDto(user);
    }
}
