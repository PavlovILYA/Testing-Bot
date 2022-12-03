package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.income.CallbackQuery;
import ru.mephi.knowledgechecker.dto.telegram.income.Message;

public interface BotState {
    void processMessage(Message message);

    void processCommand(Message message);

    void processCallbackQuery(CallbackQuery callbackQuery);
}
