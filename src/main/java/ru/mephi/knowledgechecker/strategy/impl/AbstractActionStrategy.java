package ru.mephi.knowledgechecker.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
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
        MessageParams params =
                wrapMessageParams(userId, boldMessage + message,
                        List.of(new MessageEntity(TextType.BOLD, 0, boldMessage.length()),
                                new MessageEntity(TextType.ITALIC, boldMessage.length(), message.length())),
                        null);
        telegramApiClient.sendMessage(params);
        // todo: обновить keyboard?
    }
}
