package ru.mephi.knowledgechecker.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.model.user.User;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class UnknownStrategy extends AbstractActionStrategy {
    // todo перевезти все константы в одно место
    private static final String NOT_IMPLEMENTED_MESSAGE = "Обработка данного действия еще не реализована 🧑🏼‍💻";


    @Override
    public boolean apply(Update update) {
        return false;
    }

    @Override
    public void process(User user, Update update) throws StrategyProcessException {
        MessageParams params = wrapMessageParams(user.getId(), NOT_IMPLEMENTED_MESSAGE, null);
        telegramApiClient.sendMessage(params);
    }
}
