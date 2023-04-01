package ru.mephi.knowledgechecker.state.impl.test.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;

@Component
public class TestSearchResultState extends AbstractBotState {
    public TestSearchResultState(ManageTestStrategy manageTestStrategy,
                                 ToTestListStrategy toTestListStrategy,
                                 TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
