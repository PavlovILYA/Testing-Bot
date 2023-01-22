package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.create.ToTestCreatingStrategy;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.ToChooseSolvingTypeStrategy;

@Slf4j
@Component
public class PublicTestListState extends AbstractBotState {
    public PublicTestListState(ToMainMenuStrategy toMainMenuStrategy,
                               ToTestCreatingStrategy toTestCreatingStrategy,
                               ToChooseSolvingTypeStrategy toChooseSolvingTypeStrategy) {
        availableStrategies.add(toMainMenuStrategy);
        availableStrategies.add(toTestCreatingStrategy);
        availableStrategies.add(toChooseSolvingTypeStrategy);
    }
}
