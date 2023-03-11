package ru.mephi.knowledgechecker.state.impl.test.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.search.ShowTestSearchResultStrategy;

@Component
public class TestSearchAttemptState extends AbstractBotState {
    public TestSearchAttemptState(ShowTestSearchResultStrategy showTestSearchResultStrategy) {
        availableStrategies.add(showTestSearchResultStrategy);
    }
}
