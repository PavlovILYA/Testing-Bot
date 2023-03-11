package ru.mephi.knowledgechecker.state.impl.course.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;

@Component
public class CourseSearchResultState extends AbstractBotState {
    public CourseSearchResultState(ToCoursesListStrategy toCoursesListStrategy,
                                   TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toCoursesListStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
