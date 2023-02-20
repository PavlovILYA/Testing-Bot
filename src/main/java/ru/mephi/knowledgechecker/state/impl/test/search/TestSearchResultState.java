package ru.mephi.knowledgechecker.state.impl.test.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToPublicTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.ToChooseSolvingTypeStrategy;

@Component
public class TestSearchResultState extends AbstractBotState {
    public TestSearchResultState(ToChooseSolvingTypeStrategy toChooseSolvingTypeStrategy,
                                 ToPublicTestListStrategy toPublicTestListStrategy,
                                 TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toChooseSolvingTypeStrategy);
        availableStrategies.add(toPublicTestListStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
