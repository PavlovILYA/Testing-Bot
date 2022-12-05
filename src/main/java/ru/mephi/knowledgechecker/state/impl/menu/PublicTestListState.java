package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToTestCreatingStrategy;

@Slf4j
@Component
public class PublicTestListState extends AbstractBotState {
    public PublicTestListState(ToMainMenuStrategy toMainMenuStrategy,
                               ToTestCreatingStrategy toTestCreatingStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(toTestCreatingStrategy);
    }
}
