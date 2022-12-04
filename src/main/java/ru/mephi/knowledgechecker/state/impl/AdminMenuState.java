package ru.mephi.knowledgechecker.state.impl;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.strategy.impl.ToMainMenuStrategy;

@Component
public class AdminMenuState extends AbstractBotState {
    public AdminMenuState(ToMainMenuStrategy toMainMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
    }
}
