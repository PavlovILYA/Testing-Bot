package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.TestCreatingStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.search.AskForTestSearchQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;

@Slf4j
@Component
public class TestListState extends AbstractBotState {
    public TestListState(ToMainMenuStrategy toMainMenuStrategy,
                         TestCreatingStrategy testCreatingStrategy,
                         ManageTestStrategy manageTestStrategy,
                         AskForTestSearchQueryStrategy askForTestSearchQueryStrategy,
                         TurnPageStrategy turnPageStrategy,
                         ToAdminMenuStrategy toAdminMenuStrategy,
                         ManageCourseStrategy manageCourseStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(testCreatingStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(askForTestSearchQueryStrategy);
        availableStrategies.add(turnPageStrategy);
        availableStrategies.add(toAdminMenuStrategy);
        availableStrategies.add(manageCourseStrategy);
    }
}
