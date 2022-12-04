package ru.mephi.knowledgechecker.state.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.strategy.impl.ToMainMenuStrategy;

@Component
public class CoursesListState extends AbstractBotState {
    @Autowired
    public CoursesListState(ToMainMenuStrategy toMainMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
    }
}
