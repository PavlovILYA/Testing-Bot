package ru.mephi.knowledgechecker.state.impl.test.create.file;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.file.ImportTestStrategy;

@Component
public class ImportTestState extends AbstractBotState {
    public ImportTestState(ImportTestStrategy importTestStrategy) {
        availableStrategies.add(importTestStrategy);
    }
}
