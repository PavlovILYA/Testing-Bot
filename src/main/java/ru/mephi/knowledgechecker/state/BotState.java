package ru.mephi.knowledgechecker.state;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import java.util.Map;

public interface BotState {
    void process(Update update, Map<String, Object> data);
}
