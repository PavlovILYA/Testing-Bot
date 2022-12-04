package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

public abstract class AbstractMessageStrategy extends AbstractActionStrategy {
    @Override
    public boolean apply(Update update) {
        return update.getMessage() != null;
    }
}
