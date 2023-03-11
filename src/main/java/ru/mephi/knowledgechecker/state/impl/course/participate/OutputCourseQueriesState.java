package ru.mephi.knowledgechecker.state.impl.course.participate;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.course.manage.ManageCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;

@Component
public class OutputCourseQueriesState extends AbstractBotState {
    public OutputCourseQueriesState(ManageCourseStrategy manageCourseStrategy,
                                    TurnPageStrategy turnPageStrategy,
                                    ToCoursesListStrategy toCoursesListStrategy) {
        availableStrategies.add(manageCourseStrategy);
        availableStrategies.add(turnPageStrategy);
        availableStrategies.add(toCoursesListStrategy);
    }
}
