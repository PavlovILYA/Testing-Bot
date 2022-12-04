package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.strategy.impl.ToAdminMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.ToCoursesListStrategy;
import ru.mephi.knowledgechecker.strategy.impl.ToPublicTestListStrategy;

@Slf4j
@Component
public class MainMenuState extends AbstractBotState {
    public MainMenuState(ToPublicTestListStrategy toPublicTestListStrategy,
                         ToCoursesListStrategy toCoursesListStrategy,
                         ToAdminMenuStrategy toAdminMenuStrategy) {
        availableStrategies.add(toPublicTestListStrategy);
        availableStrategies.add(toCoursesListStrategy);
        availableStrategies.add(toAdminMenuStrategy);
    }
}
