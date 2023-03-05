package ru.mephi.knowledgechecker.state.impl.test.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.ManageTestStrategy;

@Component
public class TestSearchResultState extends AbstractBotState {
    public TestSearchResultState(ManageTestStrategy manageTestStrategy,
                                 ToPublicTestListStrategy toPublicTestListStrategy,
                                 TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(toPublicTestListStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
