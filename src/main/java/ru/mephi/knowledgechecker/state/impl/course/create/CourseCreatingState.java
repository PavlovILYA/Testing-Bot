package ru.mephi.knowledgechecker.state.impl.course.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;

@Component
public class CourseCreatingState extends AbstractBotState {
    public CourseCreatingState(ToAdminMenuStrategy toAdminMenuStrategy) {
        availableStrategies.add(toAdminMenuStrategy);
    }
}
