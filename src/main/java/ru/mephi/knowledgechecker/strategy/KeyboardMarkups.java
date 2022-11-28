package ru.mephi.knowledgechecker.strategy;

import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.KeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMarkups {
    public static List<List<KeyboardButton>> getStartReplyKeyboardMarkup() {
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
        return markup;
    }
}
