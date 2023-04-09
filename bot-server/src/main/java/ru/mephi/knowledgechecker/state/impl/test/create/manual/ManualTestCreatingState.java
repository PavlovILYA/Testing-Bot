package ru.mephi.knowledgechecker.state.impl.test.create.manual;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ChooseCreatingMethodStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.manual.SaveManualTestToDbStrategy;

@Component
public class ManualTestCreatingState extends AbstractBotState {
    public ManualTestCreatingState(ChooseCreatingMethodStrategy chooseCreatingMethodStrategy,
                                   SaveManualTestToDbStrategy saveManualTestToDbStrategy) {
        availableStrategies.add(chooseCreatingMethodStrategy);
        availableStrategies.add(saveManualTestToDbStrategy);
    }
}
