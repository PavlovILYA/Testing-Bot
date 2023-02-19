package ru.mephi.knowledgechecker.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.keyboard.inline.InlineKeyboardMarkup;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.DeleteMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.EditMessageReplyMarkupParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.EditMessageTextParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
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

    protected void saveToContext(BotState state, CurrentData data) {
        stateContext.putStateAndSaveUserData(state, data);
    }

    protected void saveToContext(CurrentData data) {
        stateContext.saveUserData(data);
    }

    protected void saveToContext(Long userId, BotState state) {
        stateContext.putState(userId, state);
    }

    @Override
    public void analyzeException(StrategyProcessException exception) {
        sendError(exception.getUserId(), exception.getMessage());
    }

    protected void sendError(long userId, String message) {
        String boldMessage = "Ошибка 🥴";
        if (!message.isBlank()) {
            boldMessage += "\n\n";
        }
        SendMessageParams params =
                wrapMessageParams(userId, boldMessage + message,
                        List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                                new MessageEntity(TextType.ITALIC, boldMessage.length(), message.length())),
                        null);
        telegramApiClient.sendMessage(params);
        // todo: обновить keyboard?
    }

    protected void sendMenuAndSave(SendMessageParams params, BotState nextState, CurrentData data) {
        clearReply(data);
        Long menuMessageId = data.getMenuMessageId();
        if (data.getMenuMessageId() != null) {
            menuMessageId = telegramApiClient.editMessageText(new EditMessageTextParams(menuMessageId, params));
        } else {
            menuMessageId = telegramApiClient.sendMessage(params);
        }
        data.setMenuMessageId(menuMessageId);
        saveToContext(nextState, data);
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
//        saveToContext(nextState, data);
    }

    protected void sendMessageAndSave(SendMessageParams params, BotState nextState, CurrentData data) {
        clearReply(data);
        Long messageId = telegramApiClient.sendMessage(params);
        if (params.getReplyMarkup() != null && params.getReplyMarkup() instanceof InlineKeyboardMarkup) {
            data.setClearReplyMessageId(messageId);
        }
        if (nextState != null) {
            saveToContext(nextState, data);
        } else {
            saveToContext(data);
        }
    }

    protected void sendMessageAndSave(SendMessageParams params, CurrentData data) {
        sendMessageAndSave(params, null, data);
    }

    protected void clearReply(CurrentData data) {
        Long clearReplyMessageId = data.getClearReplyMessageId();
        if (clearReplyMessageId != null) {
            telegramApiClient.editMessageReplyMarkup(EditMessageReplyMarkupParams.builder()
                    .chatId(data.getUser().getId())
                    .messageId(clearReplyMessageId)
                    .build());
        }
        data.setClearReplyMessageId(null);
    }
}
