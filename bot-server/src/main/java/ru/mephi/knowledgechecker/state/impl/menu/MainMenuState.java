package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToCoursesListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestListStrategy;

@Slf4j
@Component
public class MainMenuState extends AbstractBotState {
    public MainMenuState(ToTestListStrategy toTestListStrategy,
                         ToCoursesListStrategy toCoursesListStrategy,
                         ToAdminMenuStrategy toAdminMenuStrategy) {
        availableStrategies.add(toTestListStrategy);
        availableStrategies.add(toCoursesListStrategy);
        availableStrategies.add(toAdminMenuStrategy);
    }
}
