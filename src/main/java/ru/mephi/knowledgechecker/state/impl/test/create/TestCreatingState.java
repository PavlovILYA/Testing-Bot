package ru.mephi.knowledgechecker.state.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ReadUniqueTestNameStrategy;

@Component
public class TestCreatingState extends AbstractBotState {
    public TestCreatingState(ToPublicTestListStrategy toPublicTestListStrategy,
                             ReadUniqueTestNameStrategy readUniqueTestNameStrategy) {
        availableStrategies.add(toPublicTestListStrategy);
        availableStrategies.add(readUniqueTestNameStrategy);
    }
}
