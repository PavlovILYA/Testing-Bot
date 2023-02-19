package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.TextType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageEntity;
import ru.mephi.knowledgechecker.dto.telegram.outcome.params.SendMessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.model.user.CurrentData;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

import java.util.List;

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
        String message1 = "👤 Пользователь ";
        String boldMessage = data.getUser().getFirstName() + " (" + data.getUser().getUsername() + ")";
        String message2 = " зарегистрирован!";
        log.info(message1 + boldMessage + message2);
        SendMessageParams params = wrapMessageParams(data.getUser().getId(), message1 + boldMessage + message2,
                List.of(new MessageEntity(TextType.BOLD, message1.length(), boldMessage.length())),
                null);
        telegramApiClient.sendMessage(params);
        super.process(data, update);
    }
}
