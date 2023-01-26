package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.repository.TestRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public Test getByUniqueTitle(String uniqueTitle) {
        Test test = testRepository.findByUniqueTitle(uniqueTitle);
        log.info("Get test: {}", test);
        return test;
    }

    public Test save(Test test) {
        Test savedTest = testRepository.save(test);
        log.info("Saved test: {}", savedTest);
        return savedTest;
    }

    public List<String> findTests(String keyWords, Long userId) {
        int from = 0;
        int size = 5;
        List<String> tests = testRepository.getTestsByKeyWords(keyWords, userId,
                PageRequest.of(from / size, size))
                .getContent();
        log.info("Found tests by key-words {}: {}", keyWords, tests);
        return tests;
    }
}
