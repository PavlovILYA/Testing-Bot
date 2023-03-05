package ru.mephi.knowledgechecker.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.KeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.*;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.state.BotState;
import ru.mephi.knowledgechecker.state.StateContext;
import ru.mephi.knowledgechecker.strategy.ActionStrategy;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;

import java.util.List;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

public abstract class AbstractActionStrategy implements ActionStrategy {
    @Lazy
    @Autowired
    private StateContext stateContext;
    @Autowired
    protected TelegramApiClient telegramApiClient;
    protected BotState nextState;

    protected void saveToContext(CurrentData data) {
        stateContext.saveUserData(data);
    }

    @Override
    public void analyzeException(StrategyProcessException e) {
        sendError(e.getUserId(), e.getMessage(), e.getCallbackQueryId());
    }

    protected void sendError(long userId, String message, String callbackQueryId) {
        String boldMessage = "Ошибка 🥴";
        if (callbackQueryId != null && message.length() <= 200) {
            SendPopupParams params = SendPopupParams.builder()
                    .callbackQueryId(callbackQueryId)
                    .text(boldMessage + "\n" + message)
                    .showAlert(true)
                    .build();
            telegramApiClient.answerCallbackQuery(params);
        } else {
            if (!message.isBlank()) {
                boldMessage += "\n\n";
            }
            SendMessageParams params = wrapMessageParams(userId, boldMessage + message,
                    List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                            new MessageEntity(TextType.ITALIC, boldMessage.length(), message.length())),
                    null);
            telegramApiClient.sendMessage(params);
        }
    }

    protected void sendMenuAndSave(CurrentData data, String text, KeyboardMarkup markup) {
        SendMessageParams params = SendMessageParams.builder()
                .chatId(data.getUser().getId())
                .text(text)
                .entities(List.of(new MessageEntity(TextType.BOLD, 0, text.length())))
                .replyMarkup(markup)
                .build();

        clearInlineKeyboard(data);
        Long menuMessageId = data.getMenuMessageId();
        if (data.getMenuMessageId() != null) {
            menuMessageId = telegramApiClient.editMessageText(new EditMessageTextParams(menuMessageId, params));
        } else {
            menuMessageId = telegramApiClient.sendMessage(params);
        }
        data.setMenuMessageId(menuMessageId);
        saveToContext(data);
    }

    protected void deleteMenu(CurrentData data) {
        Long menuMessageId = data.getMenuMessageId();
        Long chatId = data.getUser().getId();
        telegramApiClient.deleteMessage(DeleteMessageParams.builder()
                .chatId(chatId)
                .messageId(menuMessageId)
                .build()
        );
        data.setMenuMessageId(null);
    }

    protected void sendMessageAndSave(SendMessageParams params, CurrentData data) {
        clearInlineKeyboard(data);
        Long messageId = telegramApiClient.sendMessage(params);
        if (params.getReplyMarkup() != null && params.getReplyMarkup() instanceof InlineKeyboardMarkup) {
            data.setClearReplyMessageId(messageId);
        }
        saveToContext(data);
    }

    protected void sendMessageAndSave(String boldMessage, CurrentData data) {
        SendMessageParams params = SendMessageParams.builder()
                .chatId(data.getUser().getId())
                .text(boldMessage)
                .entities(List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length())))
                .build();
        sendMessageAndSave(params, data);
    }

    protected void clearInlineKeyboard(CurrentData data) {
        Long clearReplyMessageId = data.getClearReplyMessageId();
        if (clearReplyMessageId != null) {
            telegramApiClient.editMessageReplyMarkup(EditMessageReplyMarkupParams.builder()
                    .chatId(data.getUser().getId())
                    .messageId(clearReplyMessageId)
                    .build());
            data.setClearReplyMessageId(null);
        }
    }
}
