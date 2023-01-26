package ru.mephi.knowledgechecker.state.impl.test.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.search.ShowSearchResultStrategy;

@Component
public class TestSearchAttemptState extends AbstractBotState {
    public TestSearchAttemptState(ShowSearchResultStrategy showSearchResultStrategy) {
        availableStrategies.add(showSearchResultStrategy);
    }
}
