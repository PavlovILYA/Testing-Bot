package ru.mephi.knowledgechecker.strategy.impl.test.create;

import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

@Component
public class ReadUniqueTestNameStrategy extends AbstractMessageStrategy {
    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update) {
        // todo: to new variate state
    }
}
