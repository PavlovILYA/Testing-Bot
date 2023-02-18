package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.User;

public interface BotState {
    void process(User user, Update update);
}
