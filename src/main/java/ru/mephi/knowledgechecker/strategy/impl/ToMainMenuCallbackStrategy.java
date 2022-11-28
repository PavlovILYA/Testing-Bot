package ru.mephi.knowledgechecker.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;

import static ru.mephi.knowledgechecker.strategy.Constants.TO_MAIN_MENU;
import static ru.mephi.knowledgechecker.strategy.KeyboardMarkups.getStartReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class ToMainMenuCallbackStrategy extends AbstractCallbackQueryStrategy {
    private final TelegramApiClient telegramApiClient;

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getCallbackQuery().getData().equals(TO_MAIN_MENU);
    }

    @Override
    public void process(Update update) {
        sendStartMenu(update.getCallbackQuery().getFrom().getId(), "▶️ ГЛАВНАЯ");
    }

    private void sendStartMenu(Long chatId, String text) {
        ReplySendMessageParams params =
                wrapReplySendMessageParams(chatId, text, getStartReplyKeyboardMarkup(), "Основное меню");
        telegramApiClient.sendMessage(params);
    }
}
