package ru.mephi.knowledgechecker.state.impl.test.create.question.open;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.test.create.question.open.ReadOpenQuestionInfoStrategy;

@Component
public class OpenQuestionInfoReadingState extends AbstractBotState {
    public OpenQuestionInfoReadingState(ReadOpenQuestionInfoStrategy readOpenQuestionInfoStrategy) {
        availableStrategies.add(readOpenQuestionInfoStrategy);
    }
}
