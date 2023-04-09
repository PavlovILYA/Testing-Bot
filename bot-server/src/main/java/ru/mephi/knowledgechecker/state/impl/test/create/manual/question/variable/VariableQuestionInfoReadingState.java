package ru.mephi.knowledgechecker.state.impl.test.create.manual.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable.ReadVariableQuestionStrategy;

@Component
public class VariableQuestionInfoReadingState extends AbstractBotState {
    public VariableQuestionInfoReadingState(ReadVariableQuestionStrategy readVariableQuestionStrategy) {
        availableStrategies.add(readVariableQuestionStrategy);
    }
}
