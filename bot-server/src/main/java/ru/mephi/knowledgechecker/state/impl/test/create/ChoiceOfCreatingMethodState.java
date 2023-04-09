package ru.mephi.knowledgechecker.state.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.AskTestUniqueTitleStrategy;

@Component
public class ChoiceOfCreatingMethodState extends AbstractBotState {
    public ChoiceOfCreatingMethodState(AskTestUniqueTitleStrategy askTestUniqueTitleStrategy,
                                       ToTestListStrategy toTestListStrategy) {
        availableStrategies.add(askTestUniqueTitleStrategy);
        availableStrategies.add(toTestListStrategy);
    }
}
