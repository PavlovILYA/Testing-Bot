package ru.mephi.knowledgechecker.strategy.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.state.impl.menu.PublicTestListState;
import ru.mephi.knowledgechecker.common.Constants;
import ru.mephi.knowledgechecker.strategy.impl.AbstractActionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.mephi.knowledgechecker.common.Constants.PUBLIC_TEST_LIST;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.*;

@Slf4j
@Component
public class ToPublicTestListStrategy extends AbstractActionStrategy {
    public ToPublicTestListStrategy(@Lazy PublicTestListState nextState) {
        this.nextState = nextState;
    }

    @Override
    public boolean apply(Update update) {
        return update.getCallbackQuery() != null
                && update.getCallbackQuery().getData().equals(PUBLIC_TEST_LIST)
                ||
                update.getMessage() != null
                && update.getMessage().getText().equals(PUBLIC_TEST_LIST);
    }

    @Override
    public void process(Update update, Map<String, Object> data) {
        Long userId;
        if (update.getCallbackQuery() != null) {
            userId = update.getCallbackQuery().getFrom().getId();
        } else {
            userId = update.getMessage().getChat().getId();
        }
        MessageParams params =
                wrapMessageParams(userId, "▶️ ГЛАВНАЯ ➡️ ПУБЛИЧНЫЕ ТЕСТЫ", getInlineKeyboardMarkup());
        putStateToContext(userId, nextState, data);
        telegramApiClient.sendMessage(params);
    }

    private KeyboardMarkup getInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("⬅️")
                .callbackData(Constants.TO_MAIN_MENU)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Создать тест")
                .callbackData(Constants.CREATE_PUBLIC_TEST)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Найти тест")
                .callbackData(Constants.FIND_PUBLIC_TEST)
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
        return wrapInlineKeyboardMarkup(markup);
    }
}
