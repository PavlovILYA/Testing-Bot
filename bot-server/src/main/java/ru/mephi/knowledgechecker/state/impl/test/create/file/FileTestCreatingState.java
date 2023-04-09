package ru.mephi.knowledgechecker.state.impl.test.create.file;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ChooseCreatingMethodStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.file.SaveFileTestToDbStrategy;

@Component
public class FileTestCreatingState extends AbstractBotState {
    public FileTestCreatingState(ChooseCreatingMethodStrategy chooseCreatingMethodStrategy,
                                 SaveFileTestToDbStrategy saveFileTestToDbStrategy) {
        availableStrategies.add(chooseCreatingMethodStrategy);
        availableStrategies.add(saveFileTestToDbStrategy);
    }
}
