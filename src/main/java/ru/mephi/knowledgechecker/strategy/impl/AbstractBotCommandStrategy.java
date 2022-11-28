package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

public abstract class AbstractBotCommandStrategy extends AbstractMessageStrategy {
    @SuppressWarnings("checkstyle:MemberName")
    private final String BOT_COMMAND = "bot_command";

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getMessage().getEntities() != null
                && update.getMessage().getEntities().stream()
                .anyMatch(e -> e.getType().equals(BOT_COMMAND));
    }
}
