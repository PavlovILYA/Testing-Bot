package ru.mephi.knowledgechecker.state.impl.test.create.manual.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable.ReadWrongVariableAnswerStrategy;

@Component
public class WrongVariableAnswerInfoReadingState extends AbstractBotState {
    public WrongVariableAnswerInfoReadingState(ReadWrongVariableAnswerStrategy readWrongVariableAnswerStrategy) {
        availableStrategies.add(readWrongVariableAnswerStrategy);
    }
}
