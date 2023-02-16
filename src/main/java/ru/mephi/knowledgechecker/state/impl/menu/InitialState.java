package ru.mephi.knowledgechecker.state.impl.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mephi.knowledgechecker.common.DataType;
import ru.mephi.knowledgechecker.dto.telegram.income.Update;
import ru.mephi.knowledgechecker.dto.telegram.income.UserDto;
import ru.mephi.knowledgechecker.dto.telegram.outcome.MessageParams;
import ru.mephi.knowledgechecker.httpclient.TelegramApiClient;
import ru.mephi.knowledgechecker.service.UserService;
import ru.mephi.knowledgechecker.state.impl.AbstractBotState;
import ru.mephi.knowledgechecker.strategy.impl.menu.ToMainMenuStrategy;

import java.util.Map;

import static ru.mephi.knowledgechecker.common.ParamsWrapper.wrapMessageParams;

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
    public void process(Update update, Map<DataType, Object> data) {
        UserDto userDto = update.getCallbackQuery() != null
                ? update.getCallbackQuery().getFrom()
                : update.getMessage().getFrom();
        if (userService.get(userDto.getId()) == null) {
            userService.save(userDto);
            log.info("👤 Пользователь {} ({}) зарегистрирован!", userDto.getFirstName(), userDto.getUsername());
            MessageParams params = wrapMessageParams(userDto.getId(), "👤 Пользователь " + userDto.getFirstName()
                    + "(" + userDto.getUsername() + ") зарегистрирован!", null);
            telegramApiClient.sendMessage(params);
        }
        super.process(update, data);
    }
}
