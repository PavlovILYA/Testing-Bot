package ru.mephi.knowledgechecker.state.impl.menu;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.participate.ShowOutputCourseQueriesStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.search.AskForCourseSearchQueryStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

@Component
public class CoursesListState extends AbstractBotState {
    public CoursesListState(ToMainMenuStrategy toMainMenuStrategy,
                            AskForCourseSearchQueryStrategy askForCourseSearchQueryStrategy,
                            ShowOutputCourseQueriesStrategy showOutputCourseQueriesStrategy,
                            ManageCourseStrategy manageCourseStrategy,
                            TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(askForCourseSearchQueryStrategy);
        availableStrategies.add(showOutputCourseQueriesStrategy);
        availableStrategies.add(manageCourseStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
