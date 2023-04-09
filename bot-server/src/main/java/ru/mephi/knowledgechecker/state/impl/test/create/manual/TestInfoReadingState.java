package ru.mephi.knowledgechecker.state.impl.test.create.manual;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.ReadTestInfoStrategy;

@Component
public class TestInfoReadingState extends AbstractBotState {
    public TestInfoReadingState(ReadTestInfoStrategy readTestInfoStrategy) {
        availableStrategies.add(readTestInfoStrategy);
    }
}
