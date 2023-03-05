package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;

public abstract class AbstractBotCommandStrategy extends AbstractMessageStrategy {
    public static final String BOT_COMMAND = "bot_command"; // todo перенести в enum?

    @Override
    public boolean apply(CurrentData data, Update update) {
        return super.apply(data, update)
                && update.getMessage().getEntities() != null
                && update.getMessage().getEntities().stream()
                .anyMatch(e -> e.getType().equals(BOT_COMMAND));
    }
}
