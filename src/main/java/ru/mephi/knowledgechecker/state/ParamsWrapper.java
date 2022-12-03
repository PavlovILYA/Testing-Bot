package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.KeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplyKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;

import java.util.List;

public class ParamsWrapper {
    public static MessageParams wrapMessageParams(Long chatId,
                                                  String text) {
        return MessageParams.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    // Сообщение с встроенной клавиатурой! 🐸
    public static InlineSendMessageParams wrapInlineSendMessageParams(Long chatId,
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
    public static ReplySendMessageParams wrapReplySendMessageParams(Long chatId,
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
