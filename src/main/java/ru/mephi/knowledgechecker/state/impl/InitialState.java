package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.user.User;
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
    public void process(User user, Update update) {
        log.info("👤 Пользователь {} ({}) зарегистрирован!", user.getFirstName(), user.getUsername());
        SendMessageParams params = wrapMessageParams(user.getId(), "👤 Пользователь " + user.getFirstName()
                + "(" + user.getUsername() + ") зарегистрирован!", null);
        telegramApiClient.sendMessage(params);
        super.process(user, update);
    }
}
