package ru.mephi.knowledgechecker.state.impl.test.solve;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.solve.StartSolvingTestStrategy;

@Component
public class ChooseSolvingTypeState extends AbstractBotState {
    public ChooseSolvingTypeState(StartSolvingTestStrategy startSolvingTestStrategy) {
        availableStrategies.add(startSolvingTestStrategy);
    }
}
