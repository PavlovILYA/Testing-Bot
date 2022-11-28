package ru.mephi.knowledgechecker.strategy;

import ru.mephi.knowledgechecker.dto.telegram.income.Update;

public interface UpdateStrategy {
    boolean apply(Update update); // choose appropriate strategy

    void process(Update update);  // do logic things
}
