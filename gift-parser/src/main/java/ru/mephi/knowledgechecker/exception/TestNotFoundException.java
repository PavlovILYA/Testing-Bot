package ru.mephi.knowledgechecker.exception;

public class TestNotFoundException extends RuntimeException {
    public TestNotFoundException(Long userId, Long testId) {
        super("Тест " + testId + " для пользователя " + userId + " не найден!");
    }
}
