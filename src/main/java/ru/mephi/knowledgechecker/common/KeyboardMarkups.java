package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapReplyKeyboardMarkup;

public class KeyboardMarkups {
    public static KeyboardMarkup getStartReplyKeyboardMarkup() {
        List<List<KeyboardButton>> markup = new ArrayList<>();
        markup.add(List.of(KeyboardButton.builder()
                .text("Список публичных тестов")
                .build()));
        markup.add(List.of(KeyboardButton.builder()
                .text("Список курсов")
                .build()));
        markup.add(List.of(KeyboardButton.builder()
                .text("Администраторское меню")
                .build()));
        return wrapReplyKeyboardMarkup(markup, "Главное меню");
    }
}
