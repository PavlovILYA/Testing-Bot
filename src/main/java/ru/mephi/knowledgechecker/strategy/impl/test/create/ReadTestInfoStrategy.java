package ru.mephi.knowledgechecker.strategy.impl.test.create;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.impl.AbstractMessageStrategy;

public class ReadTestInfoStrategy extends AbstractMessageStrategy {
    @Override
    public boolean apply(Update update) {
        return super.apply(update);
    }

    @Override
    public void process(Update update) {
        // todo: if smth in DB that... else...
    }
}
