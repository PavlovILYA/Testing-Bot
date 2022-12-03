package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

public interface BotState {
    void process(Update update);
}
