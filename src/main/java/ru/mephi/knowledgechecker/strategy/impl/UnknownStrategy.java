package ru.mephi.knowledgechecker.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;

import static ru.mephi.knowledgechecker.state.ParamsWrapper.wrapMessageParams;

@Component
@RequiredArgsConstructor
public class UnknownStrategy extends AbstractActionStrategy {
    // todo перевезти все константы в одно место
    private static final String NOT_IMPLEMENTED_MESSAGE = "Обработка данного действия еще не реализована 🧑🏼‍💻";


    @Override
    public boolean apply(Update update) {
        return false;
    }

    @Override
    public void process(Update update) {
        Long userId = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        MessageParams params = wrapMessageParams(userId, NOT_IMPLEMENTED_MESSAGE);
        telegramApiClient.sendMessage(params);
        // change state to main menu?
    }
}
