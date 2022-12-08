package ru.mephi.knowledgechecker.state.impl.test.create.question;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.open.AddOpenQuestionStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable.AddVariableQuestionStrategy;

@Component
public class QuestionAddingState extends AbstractBotState {
    public QuestionAddingState(ToMainMenuStrategy toMainMenuStrategy,
                               AddOpenQuestionStrategy addOpenQuestionStrategy,
                               AddVariableQuestionStrategy addVariableQuestionStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(addOpenQuestionStrategy);
        availableStrategies.add(addVariableQuestionStrategy);
    }
}
