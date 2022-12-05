package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.repository.TestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public Test get(String uniqueTitle) {
        Test test = testRepository.findByUniqueTitle(uniqueTitle);
        log.info("Get test: {}", test);
        return test;
    }

    public Test save(Test test) {
        Test savedTest = testRepository.save(test);
        log.info("Saved test: {}", savedTest);
        return savedTest;
    }

    // todo: update smth
}
