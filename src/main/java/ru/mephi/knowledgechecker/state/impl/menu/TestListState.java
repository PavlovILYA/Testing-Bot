package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.TestCreatingStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.search.AskForSearchQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;

@Slf4j
@Component
public class TestListState extends AbstractBotState {
    public TestListState(ToMainMenuStrategy toMainMenuStrategy,
                         TestCreatingStrategy testCreatingStrategy,
                         ManageTestStrategy manageTestStrategy,
                         AskForSearchQueryStrategy askForSearchQueryStrategy,
                         TurnPageStrategy turnPageStrategy,
                         ToAdminMenuStrategy toAdminMenuStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(testCreatingStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(askForSearchQueryStrategy);
        availableStrategies.add(turnPageStrategy);
        availableStrategies.add(toAdminMenuStrategy);
    }
}
