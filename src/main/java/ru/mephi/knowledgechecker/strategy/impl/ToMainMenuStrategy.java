package ru.mephi.knowledgechecker.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.state.impl.MainMenuState;
import ru.mephi.knowledgechecker.strategy.Constants;

import static ru.mephi.knowledgechecker.state.ParamsWrapper.wrapReplySendMessageParams;
import static ru.mephi.knowledgechecker.strategy.Constants.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.strategy.KeyboardMarkups.getStartReplyKeyboardMarkup;
import static ru.mephi.knowledgechecker.strategy.impl.AbstractBotCommandStrategy.BOT_COMMAND;

@Slf4j
@Component
public class ToMainMenuStrategy extends AbstractActionStrategy {
    public ToMainMenuStrategy(@Lazy MainMenuState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(TO_MAIN_MENU)
                ||
                update.getMessage() != null
                && update.getMessage().getEntities() != null
                && update.getMessage().getEntities().stream()
                .anyMatch(e -> e.getType().equals(BOT_COMMAND))
                && update.getMessage().getText().equals(Constants.START_COMMAND);
    }

    @Override
    public void process(Update update) {
        Long userId = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        putStateToContext(userId, nextState);
        sendStartMenu(userId, "▶️ ГЛАВНАЯ");
    }

    private void sendStartMenu(Long chatId, String text) {
        ReplySendMessageParams params =
                wrapReplySendMessageParams(chatId, text, getStartReplyKeyboardMarkup(), "Основное меню");
        telegramApiClient.sendMessage(params);
    }
}