package ru.mephi.knowledgechecker.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.state.BotState;
import ru.mephi.knowledgechecker.state.StateContext;
import ru.mephi.knowledgechecker.strategy.ActionStrategy;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractActionStrategy implements ActionStrategy {
    @Lazy
    @Autowired
    private StateContext stateContext;
    @Autowired
    protected TelegramApiClient telegramApiClient;
    protected BotState nextState;
    protected final Queue<BotState> availableStates = new ConcurrentLinkedQueue<>();

    protected void putStateToContext(Long userId, BotState state, Map<String, Object> data) {
        stateContext.putState(userId, state, data);
    }

    protected void putStateToContext(Long userId, Map<String, Object> data) {
        stateContext.putState(userId, data);
    }
}
