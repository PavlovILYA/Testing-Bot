package ru.mephi.knowledgechecker.strategy;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

import java.util.Map;

public interface ActionStrategy {
    boolean apply(Update update); // choose appropriate strategy

    void process(Update update, Map<String, Object> data) throws StrategyProcessException;  // do logic things

    void analyzeException(StrategyProcessException exception); // fix problem (e.g. send message to user)
}
