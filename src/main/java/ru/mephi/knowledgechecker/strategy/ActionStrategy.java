package ru.mephi.knowledgechecker.strategy;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.model.user.User;

public interface ActionStrategy {
    boolean apply(Update update); // choose appropriate strategy

    void process(User user, Update update) throws StrategyProcessException;  // do logic things

    void analyzeException(StrategyProcessException exception); // fix problem (e.g. send message to user)
}
