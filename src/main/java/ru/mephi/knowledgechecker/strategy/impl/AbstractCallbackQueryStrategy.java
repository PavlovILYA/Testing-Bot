package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.strategy.UpdateStrategy;

public abstract class AbstractCallbackQueryStrategy extends AbstractParamsWrapper implements UpdateStrategy {
    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null;
    }
}
