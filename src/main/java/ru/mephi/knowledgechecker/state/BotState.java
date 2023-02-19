package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.CurrentData;

public interface BotState {
    void process(CurrentData data, Update update);
}
