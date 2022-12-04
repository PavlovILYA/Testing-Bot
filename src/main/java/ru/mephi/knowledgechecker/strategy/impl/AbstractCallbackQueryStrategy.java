package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

public abstract class AbstractCallbackQueryStrategy extends AbstractActionStrategy {
    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null;
    }
}
