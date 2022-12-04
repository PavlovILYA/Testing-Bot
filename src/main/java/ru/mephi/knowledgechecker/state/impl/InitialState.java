package ru.mephi.knowledgechecker.state.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.strategy.impl.ToMainMenuStrategy;

import static ru.mephi.knowledgechecker.state.ParamsWrapper.wrapMessageParams;

@Slf4j
@Component
public class InitialState extends AbstractBotState {
    private final UserService userService;
    protected final TelegramApiClient telegramApiClient;

    public InitialState(UserService userService,
                        TelegramApiClient telegramApiClient,
                        ToMainMenuStrategy toMainMenuStrategy) {
        this.userService = userService;
        this.telegramApiClient = telegramApiClient;
        availableStrategies.add(toMainMenuStrategy);
    }

    @Override
    public void process(Update update) {
        UserDto userDto = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom()
                : update.getMessage().getFrom();
        if (userService.getUser(userDto.getId()).isEmpty()) {
            userService.saveUser(userDto);
            log.info("👤 Пользователь {} ({}) зарегистрирован!", userDto.getFirstName(), userDto.getUsername());
            MessageParams params = wrapMessageParams(userDto.getId(), "👤 Пользователь " + userDto.getFirstName()
                    + "(" + userDto.getUsername() + ") зарегистрирован!");
            telegramApiClient.sendMessage(params);
        }
        super.process(update);
    }
}
