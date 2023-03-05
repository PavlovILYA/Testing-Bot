package ru.mephi.knowledgechecker.state.impl.course.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;

@Component
public class CreatingCourseState extends AbstractBotState {
    public CreatingCourseState(ToAdminMenuStrategy toAdminMenuStrategy) {
        availableStrategies.add(toAdminMenuStrategy);
    }
}
