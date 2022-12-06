package ru.mephi.knowledgechecker.state.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

@Component
public class QuestionAddingState extends AbstractBotState {
    public QuestionAddingState(ToMainMenuStrategy toMainMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
    }
}
