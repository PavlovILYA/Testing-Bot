package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ToTestCreatingStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.search.AskForSearchQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.ManageTestStrategy;

@Slf4j
@Component
public class PublicTestListState extends AbstractBotState {
    public PublicTestListState(ToMainMenuStrategy toMainMenuStrategy,
                               ToTestCreatingStrategy toTestCreatingStrategy,
                               ManageTestStrategy manageTestStrategy,
                               AskForSearchQueryStrategy askForSearchQueryStrategy,
                               TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(toTestCreatingStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(askForSearchQueryStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
