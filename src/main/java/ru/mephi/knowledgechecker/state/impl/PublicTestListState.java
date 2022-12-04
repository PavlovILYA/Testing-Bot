package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.strategy.impl.ToMainMenuStrategy;

@Slf4j
@Component
public class PublicTestListState extends AbstractBotState {
    @Autowired
    public PublicTestListState(ToMainMenuStrategy toMainMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
    }
}
