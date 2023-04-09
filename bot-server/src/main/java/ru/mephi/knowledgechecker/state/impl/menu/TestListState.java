package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ChooseCreatingMethodStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.search.AskForTestSearchQueryStrategy;

@Slf4j
@Component
public class TestListState extends AbstractBotState {
    public TestListState(ToMainMenuStrategy toMainMenuStrategy,
                         ChooseCreatingMethodStrategy chooseCreatingMethodStrategy,
                         ManageTestStrategy manageTestStrategy,
                         AskForTestSearchQueryStrategy askForTestSearchQueryStrategy,
                         TurnPageStrategy turnPageStrategy,
                         ToAdminMenuStrategy toAdminMenuStrategy,
                         ManageCourseStrategy manageCourseStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(chooseCreatingMethodStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(askForTestSearchQueryStrategy);
        availableStrategies.add(turnPageStrategy);
        availableStrategies.add(toAdminMenuStrategy);
        availableStrategies.add(manageCourseStrategy);
    }
}
