package ru.mephi.knowledgechecker.state.impl.test.create.manual.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable.ToQuestionAddingStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.question.variable.AddWrongVariableAnswerStrategy;

@Component
public class WrongVariableAnswerAddingState extends AbstractBotState {
    public WrongVariableAnswerAddingState(ToQuestionAddingStrategy toQuestionAddingStrategy,
                                          AddWrongVariableAnswerStrategy addWrongVariableAnswerStrategy) {
        availableStrategies.add(toQuestionAddingStrategy);
        availableStrategies.add(addWrongVariableAnswerStrategy);
    }
}
