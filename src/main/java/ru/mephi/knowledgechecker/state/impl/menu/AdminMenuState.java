package ru.mephi.knowledgechecker.state.impl.menu;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.course.create.CreatingCourseStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.TurnPageStrategy;

@Component
public class AdminMenuState extends AbstractBotState {
    public AdminMenuState(ToMainMenuStrategy toMainMenuStrategy,
                          CreatingCourseStrategy creatingCourseStrategy,
                          TurnPageStrategy turnPageStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(creatingCourseStrategy);
        availableStrategies.add(turnPageStrategy);
    }
}
