package ru.mephi.knowledgechecker.state.impl.test.check;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.check.StartCheckWorkStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;

@Component
public class WorksForCheckListState extends AbstractBotState {
    public WorksForCheckListState(ManageTestStrategy manageTestStrategy,
                                  TurnPageStrategy turnPageStrategy,
                                  StartCheckWorkStrategy startCheckWorkStrategy) {
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(turnPageStrategy);
        availableStrategies.add(startCheckWorkStrategy);
    }
}
