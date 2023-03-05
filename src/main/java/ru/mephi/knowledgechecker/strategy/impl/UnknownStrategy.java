package ru.mephi.knowledgechecker.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendPopupParams;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.strategy.StrategyProcessException;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class UnknownStrategy extends AbstractActionStrategy {
    // todo перевезти все константы в одно место
    private static final String NOT_IMPLEMENTED_MESSAGE = "Обработка данного действия еще не реализована 🧑🏼‍💻";


    @Override
    public boolean apply(CurrentData data, Update update) {
        return false;
    }

    @Override
    public void process(CurrentData data, Update update) throws StrategyProcessException {
        if (update.getCallbackQuery() != null) {
            SendPopupParams params = SendPopupParams.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .text(NOT_IMPLEMENTED_MESSAGE)
                    .showAlert(false)
                    .build();
            telegramApiClient.answerCallbackQuery(params);
        } else {
            SendMessageParams params = wrapMessageParams(data.getUser().getId(), NOT_IMPLEMENTED_MESSAGE, null);
            telegramApiClient.sendMessage(params);
        }
    }
}
