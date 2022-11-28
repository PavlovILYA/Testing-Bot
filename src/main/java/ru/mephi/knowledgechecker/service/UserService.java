package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.model.user.mapper.UserMapper;
import ru.mephi.knowledgechecker.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        log.info("Get user: {}", user);
        return user;
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Saved user: {}", user);
        return UserMapper.toDto(user);
    }
}
