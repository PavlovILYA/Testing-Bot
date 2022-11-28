package ru.mephi.knowledgechecker.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;

import static ru.mephi.knowledgechecker.strategy.Constants.*;

@Component
@RequiredArgsConstructor
public class UnknownCallbackStrategy extends AbstractCallbackQueryStrategy {
    private final TelegramApiClient telegramApiClient;

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && (update.getCallbackQuery().getData().equals(CREATE_PUBLIC_TEST)
                || update.getCallbackQuery().getData().equals(FIND_PUBLIC_TEST)
                || update.getCallbackQuery().getData().equals(ATTEND_COURSE)
                || update.getCallbackQuery().getData().equals(CREATE_COURSE)); // todo
    }

    @Override
    public void process(Update update) {
        telegramApiClient.sendMessage(ReplySendMessageParams.builder()
                .chatId(update.getCallbackQuery().getFrom().getId())
                .text("🥴 Функция еще не реализована 🆘")
                .build());
    }
}
