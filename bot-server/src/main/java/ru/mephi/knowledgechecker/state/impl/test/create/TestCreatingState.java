package ru.mephi.knowledgechecker.state.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ReadUniqueTestNameStrategy;

@Component
public class TestCreatingState extends AbstractBotState {
    public TestCreatingState(ToTestListStrategy toTestListStrategy,
                             ReadUniqueTestNameStrategy readUniqueTestNameStrategy) {
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(readUniqueTestNameStrategy);
    }
}
