package ru.mephi.knowledgechecker.state.impl.test.manage;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestVisibilityStrategy;

@Component
public class ManageTestVisibilityState extends AbstractBotState {
    public ManageTestVisibilityState(ManageTestStrategy manageTestStrategy,
                                     ManageTestVisibilityStrategy manageTestVisibilityStrategy) {
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(manageTestVisibilityStrategy);
    }
}
