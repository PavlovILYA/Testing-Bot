package ru.mephi.knowledgechecker.state.impl.test.check;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.check.MarkOpenAnswerStrategy;

@Component
public class WorkCheckState extends AbstractBotState {
    public WorkCheckState(MarkOpenAnswerStrategy markOpenAnswerStrategy) {
        availableStrategies.add(markOpenAnswerStrategy);
    }
}
