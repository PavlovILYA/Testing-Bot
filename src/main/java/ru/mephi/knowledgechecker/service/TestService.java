package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.model.course.Course;
import ru.mephi.knowledgechecker.model.test.Test;
import ru.mephi.knowledgechecker.model.test.VisibilityType;
import ru.mephi.knowledgechecker.repository.TestRepository;

import static ru.mephi.knowledgechecker.common.Constants.PAGE_SIZE;

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

    public Page<String> findTests(String keyWords, Long userId) {
        return findTests(keyWords, userId, 0);
    }

    public Page<String> findTests(String keyWords, Long userId, int from) {
        return testRepository.getTestsByKeyWords(keyWords, userId,
                PageRequest.of(from, PAGE_SIZE, Sort.by("unique_title")));
    }

    public Page<String> getCreatedTests(Long userId) {
        return getCreatedTests(userId, 0);
    }

    public Page<String> getCreatedTests(Long userId, int from) {
        return testRepository.getCreatedPublicTests(userId,
                PageRequest.of(from, PAGE_SIZE, Sort.by("unique_title")));
    }

    public void delete(Long testId) {
        testRepository.deleteById(testId);
        log.info("Deleted test: {}", testId);
    }

    public Page<String> getTestsByCourse(Course course) {
        return getTestsByCourse(course, 0);
    }

    public Page<String> getTestsByCourse(Course course, int from) {
        return testRepository.getTestsByCourse(course,
                PageRequest.of(from, PAGE_SIZE, Sort.by("uniqueTitle")));
    }

    public Page<String> getTestsByCourseAndVisibility(Course course, VisibilityType visibilityType) {
        return getTestsByCourseAndVisibility(course, visibilityType, 0);
    }

    public Page<String> getTestsByCourseAndVisibility(Course course, VisibilityType visibilityType, int from) {
        return testRepository.getTestsByCourseAndVisibility(course, visibilityType,
                PageRequest.of(from, PAGE_SIZE, Sort.by("uniqueTitle")));
    }

    public long getTestCount(Course course, VisibilityType visibilityType) {
        return testRepository.getTestCount(course, visibilityType);
    }
}
