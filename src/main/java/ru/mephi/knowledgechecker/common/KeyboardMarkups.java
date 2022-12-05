package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.Constants.*;
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
}
