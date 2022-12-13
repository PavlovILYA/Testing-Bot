package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.*;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapInlineKeyboardMarkup;
import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapReplyKeyboardMarkup;

public class KeyboardMarkups {
    public static KeyboardMarkup getStartReplyKeyboardMarkup() {
        List<List<KeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(KeyboardButton.builder()
                .text(PUBLIC_TEST_LIST)
                .build()));
        markup.add(List.of(KeyboardButton.builder()
                .text(COURSES_LIST)
                .build()));
        markup.add(List.of(KeyboardButton.builder()
                .text(ADMIN_MENU)
                .build()));
        return wrapReplyKeyboardMarkup(markup, MAIN_MENU);
    }

    public static KeyboardMarkup getAddQuestionInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("С вариантами")
                .callbackData(ADD_VARIABLE_QUESTION)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("Открытый")
                .callbackData(ADD_OPEN_QUESTION)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("✅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        markup.add(menu);
        return wrapInlineKeyboardMarkup(markup);
    }

    public static KeyboardMarkup getAddWrongVariableAnswerInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> menu = new ArrayList<>();
        menu.add(InlineKeyboardButton.builder()
                .text("Добавить")
                .callbackData(ADD_WRONG_VARIABLE_ANSWER)
                .build());
        menu.add(InlineKeyboardButton.builder()
                .text("✅️")
                .callbackData(TO_MAIN_MENU)
                .build());
        markup.add(menu);
        return wrapInlineKeyboardMarkup(markup);
    }
}
