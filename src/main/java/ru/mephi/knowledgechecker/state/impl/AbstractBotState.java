package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.state.BotState;
import ru.mephi.knowledgechecker.strategy.ActionStrategy;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public abstract class AbstractBotState implements BotState {
    @Lazy
    @Autowired
    @Qualifier("unknownStrategy")
    protected ActionStrategy unknownStrategy;
    protected final Queue<ActionStrategy> availableStrategies = new ConcurrentLinkedQueue<>();

    @Override
    public void process(Update update) {
        for (ActionStrategy strategy : availableStrategies) {
            if (strategy.apply(update)) {
                log.info("USE STRATEGY: {}", strategy.getClass().getName());
                strategy.process(update);
                return;
            }
        }
        log.info("USE STRATEGY: {}", unknownStrategy.getClass().getName());
        unknownStrategy.process(update);
    }
}
