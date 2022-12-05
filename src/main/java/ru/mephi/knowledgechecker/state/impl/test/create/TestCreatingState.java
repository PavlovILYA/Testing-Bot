package ru.mephi.knowledgechecker.state.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;

@Component
public class TestCreatingState extends AbstractBotState {
    public TestCreatingState(ToPublicTestListStrategy toPublicTestListStrategy) {
        availableStrategies.add(toPublicTestListStrategy);
        // todo: add new variate state
    }
}
