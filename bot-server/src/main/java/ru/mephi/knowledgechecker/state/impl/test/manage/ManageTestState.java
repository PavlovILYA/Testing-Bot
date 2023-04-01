package ru.mephi.knowledgechecker.state.impl.test.manage;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.check.ShowWorksForCheckStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.edit.EditTestStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.manage.ManageTestVisibilityStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.ChooseSolvingTypeStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.StartSolvingTestStrategy;

@Component
public class ManageTestState extends AbstractBotState {
    public ManageTestState(StartSolvingTestStrategy startSolvingTestStrategy,
                           ToTestListStrategy toTestListStrategy,
                           EditTestStrategy editTestStrategy,
                           ToAdminMenuStrategy adminMenuStrategy,
                           ManageCourseStrategy manageCourseStrategy,
                           ManageTestVisibilityStrategy manageTestVisibilityStrategy,
                           ChooseSolvingTypeStrategy chooseSolvingTypeStrategy,
                           ManageTestStrategy manageTestStrategy,
                           ShowWorksForCheckStrategy showWorksForCheckStrategy) {
        availableStrategies.add(startSolvingTestStrategy);
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(editTestStrategy);
        availableStrategies.add(adminMenuStrategy);
        availableStrategies.add(manageCourseStrategy);
        availableStrategies.add(manageTestVisibilityStrategy);
        availableStrategies.add(chooseSolvingTypeStrategy);
        availableStrategies.add(manageTestStrategy);
        availableStrategies.add(showWorksForCheckStrategy);
    }
}
