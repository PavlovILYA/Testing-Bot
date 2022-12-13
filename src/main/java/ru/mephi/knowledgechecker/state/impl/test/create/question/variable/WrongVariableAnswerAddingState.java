package ru.mephi.knowledgechecker.state.impl.test.create.question.variable;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable.wrong.AddWrongVariableAnswerStrategy;

@Component
public class WrongVariableAnswerAddingState extends AbstractBotState {
    public WrongVariableAnswerAddingState(ToMainMenuStrategy toMainMenuStrategy,
                                          AddWrongVariableAnswerStrategy addWrongVariableAnswerStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(addWrongVariableAnswerStrategy);
    }
}
