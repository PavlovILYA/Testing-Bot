package ru.mephi.knowledgechecker.state.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import ru.mephi.knowledgechecker.state.BotState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractState implements BotState, ApplicationContextAware {
    protected ApplicationContext applicationContext;
    protected final Queue<BotState> availableStates = new ConcurrentLinkedQueue<>();
    protected BotState unknownState;

    public abstract void initializeAvailableStates();

    @Override
    @Autowired
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected BotState getState(Class clazz) {
        for (BotState state : availableStates) {
            if (clazz.isInstance(state)) {
                return state;
            }
        }
        return unknownState;
    }
}
