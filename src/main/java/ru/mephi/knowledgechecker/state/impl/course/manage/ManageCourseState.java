package ru.mephi.knowledgechecker.state.impl.course.manage;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;

@Component
public class ManageCourseState extends AbstractBotState {
    public ManageCourseState(ToAdminMenuStrategy toAdminMenuStrategy,
                             ToTestListStrategy toTestListStrategy) {
        availableStrategies.add(toAdminMenuStrategy);
        availableStrategies.add(toTestListStrategy);
    }
}
