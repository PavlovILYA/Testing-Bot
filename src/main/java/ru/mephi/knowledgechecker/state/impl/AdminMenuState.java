package ru.mephi.knowledgechecker.state.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.strategy.impl.ToMainMenuStrategy;

@Component
public class AdminMenuState extends AbstractBotState {
    @Autowired
    public AdminMenuState(ToMainMenuStrategy toMainMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
    }
}
