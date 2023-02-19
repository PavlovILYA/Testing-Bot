package ru.mephi.knowledgechecker.state.impl.test.create.question;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.open.AddOpenQuestionStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.variable.AddVariableQuestionStrategy;

@Component
public class QuestionAddingState extends AbstractBotState {
    public QuestionAddingState(ToPublicTestListStrategy toPublicTestListStrategy,
                               AddOpenQuestionStrategy addOpenQuestionStrategy,
                               AddVariableQuestionStrategy addVariableQuestionStrategy) {
        availableStrategies.add(toPublicTestListStrategy);
        availableStrategies.add(addOpenQuestionStrategy);
        availableStrategies.add(addVariableQuestionStrategy);
    }
}
