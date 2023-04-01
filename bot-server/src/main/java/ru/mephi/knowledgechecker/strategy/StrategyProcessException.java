package ru.mephi.knowledgechecker.strategy;

public class StrategyProcessException extends Exception {
    private final Long userId;
    private final String callbackQueryId;

    public StrategyProcessException(Long userId, String message) {
        super(message);
        this.userId = userId;
        this.callbackQueryId = null;
    }

    public StrategyProcessException(Long userId, String message, String callbackQueryId) {
        super(message);
        this.userId = userId;
        this.callbackQueryId = callbackQueryId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCallbackQueryId() {
        return callbackQueryId;
    }
}
