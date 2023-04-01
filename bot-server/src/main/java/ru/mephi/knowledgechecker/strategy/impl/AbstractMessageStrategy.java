package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;

public abstract class AbstractMessageStrategy extends AbstractActionStrategy {
    @Override
    public boolean apply(CurrentData data, Update update) {
        return update.getMessage() != null;
    }
}
