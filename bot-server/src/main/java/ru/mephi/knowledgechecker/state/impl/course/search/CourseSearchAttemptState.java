package ru.mephi.knowledgechecker.state.impl.course.search;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.search.ShowCourseSearchResultStrategy;

@Component
public class CourseSearchAttemptState extends AbstractBotState {
    public CourseSearchAttemptState(ShowCourseSearchResultStrategy showCourseSearchResultStrategy) {
        availableStrategies.add(showCourseSearchResultStrategy);
    }
}
