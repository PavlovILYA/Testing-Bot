package ru.mephi.knowledgechecker.strategy.impl;

import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.KeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplyKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;

import java.util.List;

public abstract class AbstractParamsWrapper {
    // Сообщение с встроенной клавиатурой! 🐸
    protected InlineSendMessageParams wrapInlineSendMessageParams(Long chatId,
                                                                  String text,
                                                                  List<List<InlineKeyboardButton>> inlineKeyboard) {
        return InlineSendMessageParams.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .inlineKeyboard(inlineKeyboard)
                        .build())
                .build();
    }

    // Сообщение с кастомной клавиатурой! 🦋
    protected ReplySendMessageParams wrapReplySendMessageParams(Long chatId,
                                                                String text,
                                                                List<List<KeyboardButton>> keyboard,
                                                                String placeholder) {
        return ReplySendMessageParams.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboard(keyboard)
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)
                        .inputFieldPlaceholder(placeholder)
                        .build())
                .build();
    }
}
