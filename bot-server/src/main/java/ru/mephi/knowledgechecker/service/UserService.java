package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.dto.telegram.income.mapper.UserMapper;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.repository.UserRepository;

import static ru.mephi.knowledgechecker.common.Constants.PAGE_SIZE;

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

    public User save(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info("Saved user: {}", user);
        return user;
    }

    public User save(User user) {
        user = userRepository.save(user);
        log.info("Saved user: {}", user);
        return user;
    }

    public Page<User> getParticipantsByCourseId(Long courseId, boolean approved, int from) {
        return userRepository.getParticipantsByCourseId(courseId, approved,
                PageRequest.of(from, PAGE_SIZE, Sort.by("username")));
    }

    public Page<User> getParticipantsByCourseId(Long courseId, boolean approved) {
        return getParticipantsByCourseId(courseId, approved, 0);
    }

    public Page<User> getStudentsForCheck(Long testId, int from) {
        return userRepository.findStudentsForCheck(testId, VisibilityType.ESTIMATED,
                PageRequest.of(from, PAGE_SIZE, Sort.by("user.username")));
    }

    public Page<User> getStudentsForCheck(Long testId) {
        return getStudentsForCheck(testId, 0);
    }
}
