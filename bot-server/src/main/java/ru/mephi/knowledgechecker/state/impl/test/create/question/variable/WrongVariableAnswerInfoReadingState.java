package ru.mephi.knowledgechecker.state.impl.test.create.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable.ReadWrongVariableAnswerStrategy;

@Component
public class WrongVariableAnswerInfoReadingState extends AbstractBotState {
    public WrongVariableAnswerInfoReadingState(ReadWrongVariableAnswerStrategy readWrongVariableAnswerStrategy) {
        availableStrategies.add(readWrongVariableAnswerStrategy);
    }
}
