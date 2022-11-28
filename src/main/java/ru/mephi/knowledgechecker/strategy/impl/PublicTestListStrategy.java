package ru.mephi.knowledgechecker.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.strategy.Constants.*;

@Component
@RequiredArgsConstructor
public class PublicTestListStrategy extends AbstractMessageStrategy {
    private final TelegramApiClient telegramApiClient;

    @Override
    public boolean apply(Update update) {
        return super.apply(update)
                && update.getMessage().getText().equals(PUBLIC_TEST_LIST);
    }

    @Override
    public void process(Update update) {
        InlineSendMessageParams params =
                wrapInlineSendMessageParams(update.getMessage().getChat().getId(),
                        "▶️ ГЛАВНАЯ ➡️ ПУБЛИЧНЫЕ ТЕСТЫ",
                        getInlineKeyboardMarkup());
        telegramApiClient.sendMessage(params);
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Создать тест")
                .callbackData(CREATE_PUBLIC_TEST)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Найти тест")
                .callbackData(FIND_PUBLIC_TEST)
                .build());
        markup.add(menu);

        // todo
//        List<InlineKeyboardButton> publicTests = new ArrayList<>();
//        for (test : tests) {
//            publicTests.add(InlineKeyboardButton.builder()
//                    .text(test.getName())
//                    .callbackData("public-test:" + test.getId())
//                    .build());
//        }
//        markup.add(two);
        return markup;
    }
}
