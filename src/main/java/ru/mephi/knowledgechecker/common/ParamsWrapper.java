package ru.mephi.knowledgechecker.common;

import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.KeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.reply.ReplyKeyboardMarkup;

import java.util.List;

public class ParamsWrapper {
    public static MessageParams wrapMessageParams(Long chatId,
                                                  String text,
                                                  KeyboardMarkup keyboardMarkup) {
        return MessageParams.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    // Встроенная клавиатура! 🐸
    public static InlineKeyboardMarkup wrapInlineKeyboardMarkup(List<List<InlineKeyboardButton>> inlineKeyboard) {
        return InlineKeyboardMarkup.builder()
                .inlineKeyboard(inlineKeyboard)
                .build();
    }

    // Кастомная клавиатура! 🦋
    public static ReplyKeyboardMarkup wrapReplyKeyboardMarkup(List<List<KeyboardButton>> keyboard,
                                                              String placeholder) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .inputFieldPlaceholder(placeholder)
                .build();
    }
}
