package ru.mephi.knowledgechecker.state.impl.test.solve;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.ShowQuestionStrategy;

@Component
public class SolvingTestState extends AbstractBotState {
    public SolvingTestState(ShowQuestionStrategy showQuestionStrategy) {
        availableStrategies.add(showQuestionStrategy);
    }
}
