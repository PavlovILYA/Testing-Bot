package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class InitialState extends AbstractBotState {

    protected final TelegramApiClient telegramApiClient;

    public InitialState(TelegramApiClient telegramApiClient,
                        ToMainMenuStrategy toMainMenuStrategy) {
        this.telegramApiClient = telegramApiClient;
        availableStrategies.add(toMainMenuStrategy);
    }

    @Override
    public void process(CurrentData data, Update update) {
        String message = "👤 Пользователь " + data.getUser().getFirstName()
                + " (" + data.getUser().getUsername() + ") зарегистрирован!";
        log.info(message);
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message, null);
        telegramApiClient.sendMessage(params);
        super.process(data, update);
    }
}
