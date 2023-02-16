package ru.mephi.knowledgechecker.strategy;

public class StrategyProcessException extends Exception {
    private final Long userId;

    public StrategyProcessException(Long userId, String message) {
        super(message);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
