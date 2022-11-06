package ru.mephi.knowledgechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.knowledgechecker.dto.telegram.income.CallbackQuery;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.inline.InlineSendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.KeyboardButton;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplyKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.reply.ReplySendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateService {
    private final TelegramApiClient telegramApiClient;

    public void processMessage(Message message) {
        log.info("processMessage: {}", message);
        sendMessageWithInlineKeyboardMarkup(message.getChat().getId());
    }

    public void processCallbackQuery(CallbackQuery callbackQuery) {
        log.info("processCallbackQuery: {}", callbackQuery);
        sendMessageWithReplyKeyboardMarkup(callbackQuery
                .getMessage().getChat().getId());
    }

    private void sendMessageWithInlineKeyboardMarkup(Long chatId) {
        InlineSendMessageParams params = InlineSendMessageParams.builder()
                .chatId(chatId)
                .text("Сообщение с встроенной клавиатурой! 🐸")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .inlineKeyboard(getDefaultInlineKeyboardMarkup())
                        .build())
                .build();
        telegramApiClient.sendMessage(params);
    }

    private void sendMessageWithReplyKeyboardMarkup(Long chatId) {
        ReplySendMessageParams params = ReplySendMessageParams.builder()
                .chatId(chatId)
                .text("Сообщение с кастомной клавиатурой! 🦋")
                .replyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboard(getDefaultReplyKeyboardMarkup())
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)
                        .inputFieldPlaceholder("input placeholder вот))")
                        .build())
                .build();
        telegramApiClient.sendMessage(params);
    }

    private List<List<InlineKeyboardButton>> getDefaultInlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> markup = new ArrayList<>();
        List<InlineKeyboardButton> one = new ArrayList<>();
        one.add(InlineKeyboardButton.builder()
                .text("Текст1.1")
                .callbackData("callbackData1.1")
                .build());
        one.add(InlineKeyboardButton.builder()
                .text("Текст1.2")
                .callbackData("callbackData1.2")
                .build());
        List<InlineKeyboardButton> two = new ArrayList<>();
        two.add(InlineKeyboardButton.builder()
                .text("Текст2.1")
                .callbackData("callbackData2.1")
                .build());
        two.add(InlineKeyboardButton.builder()
                .text("Текст2.2")
                .callbackData("callbackData2.2")
                .build());
        markup.add(one);
        markup.add(two);
        return markup;
    }

    private List<List<KeyboardButton>> getDefaultReplyKeyboardMarkup() {
        List<List<KeyboardButton>> markup = new ArrayList<>();
        List<KeyboardButton> one = new ArrayList<>();
        one.add(KeyboardButton.builder()
                .text("Текст1.1")
                .build());
        one.add(KeyboardButton.builder()
                .text("Текст1.2")
                .build());
        List<KeyboardButton> two = new ArrayList<>();
        two.add(KeyboardButton.builder()
                .text("Геолокация")
                .requestLocation(true)
                .build());
        two.add(KeyboardButton.builder()
                .text("Контакт")
                .requestContact(true)
                .build());
        markup.add(one);
        markup.add(two);
        return markup;
    }
}
